using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
//using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.IO;
using System.Threading;
using MySql.Data.MySqlClient;

namespace Lego_MindStorm_Control_Api
{
    public partial class Form1 : Form
    {

        private Thread InternetRelayChat;
        private Thread mysql_check;
            
        public Form1()
        {
            InitializeComponent();
            mysql_check = new Thread(new ThreadStart(mysql.run));
            mysql_check.Start();
            //IrcBot.run_irc();
            //InternetRelayChat = new Thread(new ThreadStart(IrcBot.run_irc));
            //InternetRelayChat.Start();
        }
       

        private void Form1_Load(object sender, EventArgs e)
        {
            

        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            richTextBox1.Text = IrcBot.log;
            TimeSpan ts = (DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0));
            double unixTime = ts.TotalSeconds;
            toolStripStatusLabel1.Text = unixTime.ToString();
        }

        
    }
    class mysql_results
    {
        public int ID;
        public double when;
        public string msg;
        public bool result;
    }
    class mysql
    {
          public static MySqlConnectionStringBuilder connBuilder =
               new MySqlConnectionStringBuilder();
          
          public static MySqlConnection connection;
          public static MySqlCommand cmd;
          public static void run()
          {
              mysql.connect();
              while (true)
              {
                  check();
                  Thread.Sleep(100);
              }
              
          }
          public static void check()
          {

              TimeSpan ts = (DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0));
              double unixTime = ts.TotalSeconds;
             
              mysql_results res = new mysql_results();
              nxt_result result = new nxt_result();
              res = mysql.QueryCommand("SELECT `ID`,`message`,`when` FROM `log_current_session` WHERE `status`='' AND `when`< '" + unixTime.ToString().Replace(',', '.') + "' AND `type`='cmd' ORDER BY `when` ASC LIMIT 0,1");
              if (res.result)
              {
                  IrcBot.log += DateTime.Now + "checking(" + res.msg + ")...";
                  result = NXT_ROVER_CONTROL.command_translation(res.msg);
                  if (result.result)
                  {
                      //true
                      cmd.CommandText = "UPDATE `log_current_session` SET `status`='"+result.value+"' WHERE `ID`=" + res.ID;
                      cmd.CommandType = CommandType.Text;
                      MySqlDataReader reader = cmd.ExecuteReader();
                      reader.Close();
                      IrcBot.log += "succed\n";
                  }
                  else
                  {
                      //false
                      cmd.CommandText = "UPDATE `log_current_session` SET `status`='not found' WHERE `ID`=" + res.ID;
                      cmd.CommandType = CommandType.Text;
                      MySqlDataReader reader = cmd.ExecuteReader();
                      reader.Close();
                      IrcBot.log += "faild\n";

                  }
              }
              
              
          }
          public static void connect()
          {




              connBuilder.Add("Database", "rover");
              connBuilder.Add("Data Source", "localhost");
              connBuilder.Add("User Id", "root");
              connBuilder.Add("Password", "");
              connection = new MySqlConnection(connBuilder.ConnectionString);
              cmd = connection.CreateCommand();
              connection.Open();
          }
        public static void close(){
              
              connection.Close();
          }
        public static mysql_results QueryCommand(string sql)
        {
            cmd.CommandText = sql;
            cmd.CommandType = CommandType.Text;

            MySqlDataReader reader = cmd.ExecuteReader();
            mysql_results res = new mysql_results();
            res.result = false;
            while (reader.Read())
            {
                res.ID = reader.GetInt32(0);
                res.msg = reader.GetString(1);
                res.when = reader.GetDouble(2);
                res.result = true;
                

            }

            reader.Close();
            return res;
        }
        
    }
    class nxt_result
    {
        public string type="set";
        public string value;
        public bool result=false;
    }
    class NXT_ROVER_CONTROL
    {
        public static string[] arg_command;
        public static nxt_result command_translation(string text_command)
        {
            nxt_result result = new nxt_result();
            
            //check type
            arg_command = text_command.Split(' ');
            if (arg_command.Length == 4)
            {
                if (arg_command[1] == "motor" && arg_command[3] == "on")
                {
                    return motor_on(arg_command[2]);
                }
                if (arg_command[1] == "motor" && arg_command[3] == "off")
                {
                    return motor_off(arg_command[2]);
                }
            }
            if (arg_command.Length == 5)
            {
                if (arg_command[1] == "get" && arg_command[2] == "sensor" && arg_command[3] == "value")
                {
                    return get_sensor(arg_command[4]);
                }
            }
            return result;
        }
        public static nxt_result get_sensor(string sensor)
        {
            nxt_result result = new nxt_result();
            result.result = true;
            result.type = "sensor";
            result.value = "56";
            return result;
        }
        public static nxt_result motor_on(string motors)
        {
            nxt_result result = new nxt_result();
            result.result = true;
            result.value = "succed";
            return result;
        }
        public static nxt_result motor_off(string motors)
        {
            nxt_result result = new nxt_result();
            result.result = true;
            result.value = "succed";
            return result;
        }
    }
class PingSender
{
static string PING = "PING :";
private Thread pingSender;
// Empty constructor makes instance of Thread
public PingSender ()
{
pingSender = new Thread (new ThreadStart (this.Run) );
}
// Starts the thread
public void Start ()
{
pingSender.Start ();
}
// Send PING to irc server every 15 seconds
public void Run ()
{
while (true)
{
    IrcBot.log += "Send: " + PING + IrcBot.SERVER;
IrcBot.writer.WriteLine (PING + IrcBot.SERVER);
IrcBot.writer.Flush ();
Thread.Sleep (15000);
}
}
}
class IrcBot
{
    public static void ident(){
        writer.WriteLine("NICK " + NICK);
writer.Flush();
    //get ping

writer.WriteLine ("USER " + USER);
writer.Flush ();
writer.WriteLine ("JOIN " + CHANNEL);
writer.Flush ();
    }
    public static string log = "";
// Irc server to connect
public static string SERVER = "localhost";
// Irc server's port (6667 is default port)
private static int PORT = 6667;
// User information defined in RFC 2812 (Internet Relay Chat: Client Protocol) is sent to irc server
private static string USER = "Capi 127.0.0.1 " + SERVER + " :Capi echo";
    //send("USER " + username + " " + socket.getLocalAddress() + " " + host + " :" + realname); 
// Bot's nickname
private static string NICK = "Capi";
// Channel to join
private static string CHANNEL = "#control";
private static string mes_buffer = "";
// StreamWriter is declared here so that PingSender can access it
public static StreamWriter writer;
   
public static void run_irc()
{
   
NetworkStream stream;
TcpClient irc;
string inputLine;
StreamReader reader;
string nickname;
try
{
irc = new TcpClient (SERVER, PORT);
stream = irc.GetStream ();
reader = new StreamReader (stream);
writer = new StreamWriter (stream);
// Start PingSender thread
//PingSender ping = new PingSender ();
//ping.Start();
inputLine = reader.ReadLine();
log += inputLine + "\n";
//MessageBox.Show(inputLine);
ident();
while (true)
{
while ( (inputLine = reader.ReadLine () ) != null )
{
    //log += inputLine + "\n";
    if (inputLine.Contains("PING"))
    {
        inputLine.Split(' ');
        //MessageBox.Show(inputLine);
        //MessageBox.Show(inputLine.Substring(inputLine.IndexOf(" ") + 2));
        //log += "send: " + "PONG " + inputLine.Substring(inputLine.IndexOf(" ") + 2) + "\n";
        writer.WriteLine("PONG " + inputLine.Substring(inputLine.IndexOf(" ") + 2));
        writer.Flush();
    }
    if (inputLine.Contains("Register first."))
    {
        ident();
    }
    if (inputLine.Contains(CHANNEL + " :"))
    {
        //echo
        // :php!~mbrakels@my.server.name PRIVMSG #control :hello
        mes_buffer = inputLine.Substring(inputLine.IndexOf(CHANNEL + " :")+ CHANNEL.Length + 2);
        log += mes_buffer + "\n";
        if (mes_buffer.StartsWith("cmd "))
        {
            if (true)//NXT_ROVER_CONTROL.command_translation(mes_buffer)
            {

                writer.WriteLine(":" + NICK + "! PRIVMSG " + CHANNEL + " :Result true");
                writer.Flush();
            }
            else
            {
                writer.WriteLine(":" + NICK + "! PRIVMSG " + CHANNEL + " :Result false");
                writer.Flush();
            }
        }
        else
        {
            writer.WriteLine(":" + NICK + "! PRIVMSG " + CHANNEL + " :Result false, Not a command");
            writer.Flush();
        }
        
    }
    
}
// Close all streams
writer.Close ();
reader.Close ();
irc.Close ();
}
     
}
catch (Exception e)
{
// Show the exception, sleep for a while and try to establish a new connection to irc server
MessageBox.Show(e.ToString () );
Thread.Sleep (1000);
run_irc ();
}
}
}


}



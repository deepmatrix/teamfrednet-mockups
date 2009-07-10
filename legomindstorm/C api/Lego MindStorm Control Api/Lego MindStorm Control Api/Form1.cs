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
using System.Xml;


namespace Lego_MindStorm_Control_Api
{
    public partial class Form1 : Form
    {

        private Thread InternetRelayChat;
        private Thread mysql_check;
            
        public Form1()
        {
            InitializeComponent();
            config.run();
            mysql_check = new Thread(new ThreadStart(mysql.run));
            mysql_check.Start();
            //IrcBot.run_irc();
            InternetRelayChat = new Thread(new ThreadStart(IrcBot.run_irc));
            //InternetRelayChat.Start();
            timer2.Enabled = true;
        }
       

        private void Form1_Load(object sender, EventArgs e)
        {
            

        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            richTextBox1.Text = IrcBot.log;
            TimeSpan ts = (DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0));
            double unixTime = ts.TotalSeconds;
            toolStripStatusLabel1.Text = "Unix time span: " + unixTime.ToString();
        }

        private void clearLogToolStripMenuItem_Click(object sender, EventArgs e)
        {
            IrcBot.log = "";
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            InternetRelayChat.Abort();
            mysql_check.Abort();
            
            Application.Exit();
        }

        private void recontIRCToolStripMenuItem_Click(object sender, EventArgs e)
        {
            InternetRelayChat.Resume();
        }

        private void richTextBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void timer2_Tick(object sender, EventArgs e)
        {
            string temp,sql;
            
            TimeSpan ts = (DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0));
            double unixTime = ts.TotalSeconds;
            //send commands
            NXT_ROVER_CONTROL.command_translation("cmd sensor value " + config.Rover_distance.ToString());
            NXT_ROVER_CONTROL.command_translation("cmd sensor value " + config.Rover_light.ToString());
            NXT_ROVER_CONTROL.command_translation("cmd sensor value " + config.Rover_sound.ToString());
            //build
            temp = "Distantes: " + NXT_ROVER_CONTROL.Rover_distance + " cm\n";
            temp += "Light: " + NXT_ROVER_CONTROL.Rover_light + "%\n";
            temp += "Sound: " + NXT_ROVER_CONTROL.Rover_sound + "%\n";
            //update database
            sql = "INSERT INTO `rover`.`sensors` (`ID`, `when`, `result`) VALUES (NULL, '"+unixTime.ToString().Replace(',','.')+"', '"+temp+"');";
            mysql.QueryCommand(sql);
            //show
            status.Text = temp;

            
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
              //check mysql conntection
              if (mysql.connection.State == ConnectionState.Open || mysql.connection.State == ConnectionState.Connecting)
              {
                  res = mysql.QueryCommand("SELECT `ID`,`message`,`when` FROM `log_current_session` WHERE `status`='' AND `when`< '" + unixTime.ToString().Replace(',', '.') + "' AND `type`='cmd' ORDER BY `when` ASC LIMIT 0,1");
                  if (res.result)
                  {
                      IrcBot.log += DateTime.Now + "checking(" + res.msg + ")...";
                      result = NXT_ROVER_CONTROL.command_translation(res.msg);
                      if (result.result)
                      {
                          //true
                          cmd.CommandText = "UPDATE `log_current_session` SET `status`='" + result.value + "', `when`='" + unixTime.ToString().Replace(',', '.') + "' WHERE `ID`=" + res.ID;
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
              else
              {
                  //make connecion
                  IrcBot.log += "Mysql connection faild";
                  mysql.connect();
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
              IrcBot.log += "MYSQL connection ready\n";
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
    class config
    {
        public static string IRC_server;
        public static int IRC_port;
        public static string IRC_channel;
        public static string IRC_nickname;
        public static string IRC_user;
        public static string IRC_password;
        public static string Mysql_server;
        public static string Mysql_database;
        public static string Mysql_user;
        public static string Mysql_password;
        public static int Rover_distance;
        public static int Rover_light;
        public static int Rover_touch;
        public static int Rover_sound;
        public static void run()
        {

                  XmlDocument doc = new XmlDocument();

      doc.Load("settings.xml");

      XmlNodeList userNodes = doc.SelectNodes("/settings/IRC");
      XmlNode userNode = userNodes[0];
      IRC_channel = userNode.SelectSingleNode("channel").InnerText;
      IRC_port = Convert.ToInt32(userNode.SelectSingleNode("port").InnerText);
      IRC_password = userNode.SelectSingleNode("password").InnerText;
      IRC_user = userNode.SelectSingleNode("user").InnerText;
      IRC_server= userNode.SelectSingleNode("server").InnerText;
      IRC_nickname = userNode.SelectSingleNode("nickname").InnerText;
      userNodes = doc.SelectNodes("/settings/mysql");
      userNode = userNodes[0];
      Mysql_database = userNode.SelectSingleNode("database").InnerText;
      Mysql_password = userNode.SelectSingleNode("password").InnerText;
      Mysql_user = userNode.SelectSingleNode("user").InnerText;
      Mysql_server = userNode.SelectSingleNode("server").InnerText;
      userNodes = doc.SelectNodes("/settings/rover");
      userNode = userNodes[0];
      Rover_distance = Convert.ToInt32(userNode.SelectSingleNode("distance").InnerText);
      Rover_light = Convert.ToInt32(userNode.SelectSingleNode("light").InnerText);
      Rover_touch = Convert.ToInt32(userNode.SelectSingleNode("touch").InnerText);
      Rover_sound = Convert.ToInt32(userNode.SelectSingleNode("sound").InnerText);
      
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
        public static int Rover_distance;
        public static int Rover_light;
        public static int Rover_touch;
        public static int Rover_sound;
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
            if (arg_command.Length == 4)
            {
                if (arg_command[1] == "sensor" && arg_command[2] == "value")
                {
                    return get_sensor(arg_command[3]);
                }
            }
            return result;
        }
        public static nxt_result get_sensor(string sensor)
        {
            nxt_result result = new nxt_result();
            result.result = true;
            result.type = "sensor";
            result.value = "0";
            TimeSpan ts = (DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0));
              int unixTime = (int)ts.TotalSeconds;
              Random test = new Random(Convert.ToInt32(sensor) + unixTime);
            
            //set 
            if (sensor == config.Rover_distance.ToString())
            {
                NXT_ROVER_CONTROL.Rover_distance = test.Next(255);
                result.value = NXT_ROVER_CONTROL.Rover_distance.ToString();
            }
            if (sensor == config.Rover_sound.ToString())
            {
                NXT_ROVER_CONTROL.Rover_sound = test.Next(100);
                result.value = NXT_ROVER_CONTROL.Rover_sound.ToString();
            }
            if (sensor == config.Rover_light.ToString())
            {
                NXT_ROVER_CONTROL.Rover_light = test.Next(100);
                result.value = NXT_ROVER_CONTROL.Rover_light.ToString();
            }
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

class IrcBot
{
    public static void ident(){
        writer.WriteLine("NICK " + NICK);
writer.Flush();
    //get ping

writer.WriteLine ("USER " + USER);
writer.Flush ();
if (PASS != "")
{
    writer.WriteLine("JOIN " + CHANNEL + " " + PASS);
    writer.Flush();
}
else
{
    writer.WriteLine("JOIN " + CHANNEL);
    writer.Flush();
}
    }
    public static string log = "";
// Irc server to connect
public static string SERVER = config.IRC_server;
public static int timesleep = 5000;
// Irc server's port (6667 is default port)
private static int PORT = config.IRC_port;
private static string PASS = config.IRC_password;
// User information defined in RFC 2812 (Internet Relay Chat: Client Protocol) is sent to irc server
private static string USER = config.IRC_user;//"Capi 127.0.0.1 " + SERVER + " :Capi echo";
    //send("USER " + username + " " + socket.getLocalAddress() + " " + host + " :" + realname); 
// Bot's nickname
private static string NICK = config.IRC_nickname;
// Channel to join
private static string CHANNEL = config.IRC_channel;
private static string mes_buffer = "";
// StreamWriter is declared here so that PingSender can access it
public static StreamWriter writer;
   
public static void run_irc()
{
   
NetworkStream stream;
TcpClient irc;
string inputLine;
StreamReader reader;
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
IrcBot.log += "IRC chat ready\n";
nxt_result result = new nxt_result();
while (true)
{
while ( (inputLine = reader.ReadLine () ) != null )
{
    //log += inputLine + "\n";
    if (inputLine.Contains("PING"))
    {
//        inputLine.Split(' ');
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
            result = NXT_ROVER_CONTROL.command_translation(mes_buffer);
           if (result.result)
            {

                writer.WriteLine(":" + NICK + "! PRIVMSG " + CHANNEL + " :Result " + result.value);
                writer.Flush();
            }
            else
            {
                writer.WriteLine(":" + NICK + "! PRIVMSG " + CHANNEL + " :Result faild");
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
log += e.Message.ToString () + "\n";
timesleep *= 2;

Thread.Sleep (timesleep);
run_irc ();
}
}
}


}



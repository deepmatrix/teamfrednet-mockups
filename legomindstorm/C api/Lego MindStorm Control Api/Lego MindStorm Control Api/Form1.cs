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

namespace Lego_MindStorm_Control_Api
{
    public partial class Form1 : Form
    {

        private Thread InternetRelayChat;
            
        public Form1()
        {
            InitializeComponent();
            //IrcBot.run_irc();
            InternetRelayChat = new Thread(new ThreadStart(IrcBot.run_irc));
            InternetRelayChat.Start();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            richTextBox1.Text = IrcBot.log;
        }

        
    }
/*
* Class that sends PING to irc server every 15 seconds
*/
    class NXT_ROVER_CONTROL
    {
        public static string[] arg_command;
        public static Boolean command_translation(string text_command)
        {
            //check type
            arg_command = text_command.Split(' ');
            if (arg_command[1] == "motor" && arg_command[3] == "on")
            {
                return motor_on(arg_command[2]);
            }
            if (arg_command[1] == "motor" && arg_command[3] == "off")
            {
                return motor_off(arg_command[2]);
            }
            return false;
        }
        public static Boolean motor_on(string motors)
        {
            return true;
        }
        public static Boolean motor_off(string motors)
        {
            return true;
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
            if (NXT_ROVER_CONTROL.command_translation(mes_buffer))
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



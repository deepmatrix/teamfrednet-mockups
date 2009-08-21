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
using AForge.Robotics.Lego;

namespace Lego_MindStorm_Control_Api
{
    public partial class Form1 : Form
    {

        private Thread InternetRelayChat;
        private Thread mysql_check;
        private Thread rover;
            
        public Form1()
        {
            InitializeComponent();
            config.run();
            mysql_check = new Thread(new ThreadStart(mysql.run));
            mysql_check.Start();
            rover = new Thread(new ThreadStart(NXT_ROVER_CONTROL.run));
            
            InternetRelayChat = new Thread(new ThreadStart(IrcBot.run_irc));
            if (config.IRC_on_off)
            {
                
                InternetRelayChat.Start();
            }
            timer2.Enabled = true;
        }
       
        private void Form1_unLoad(object sender, EventArgs e)
        {
            rover.Abort();
            mysql_check.Abort();
            InternetRelayChat.Abort();
            Application.Exit();
        }
        

        private void timer1_Tick(object sender, EventArgs e)
        {
            if (richTextBox1.Text != IrcBot.log)
            {
                richTextBox1.Text = IrcBot.log;
        }
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
            
            if (MessageBox.Show("Really Quit?", "Exit", MessageBoxButtons.OKCancel) == DialogResult.OK)
            {
                rover.Abort();
                mysql_check.Abort();
                InternetRelayChat.Abort();
                Application.Exit();
            }
        }

        private void recontIRCToolStripMenuItem_Click(object sender, EventArgs e)
        {
            InternetRelayChat.Start();
        }

        private void richTextBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void timer2_Tick(object sender, EventArgs e)
        {
            if (config.sensor_auto_on_off)
            {
            string temp,sql;
            
            TimeSpan ts = (DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0));
            double unixTime = ts.TotalSeconds;
            //send commands
            nxt_result[] cmd = new nxt_result[4];
            temp = "";
            if (sensor1type.SelectedIndex != -1)
            {
                cmd[0] = NXT_ROVER_CONTROL.command_translation("cmd sensor value 1");
                temp += "Sensor 1: " + cmd[0].value + "\n";
            }
            if (sensor2type.SelectedIndex != -1)
            {
                cmd[1] = NXT_ROVER_CONTROL.command_translation("cmd sensor value 2");
                temp += "Sensor 2: " + cmd[1].value + "\n";
            }
            if (sensor3type.SelectedIndex != -1)
            {
                cmd[2] = NXT_ROVER_CONTROL.command_translation("cmd sensor value 3");
                temp += "Sensor 3: " + cmd[2].value + "\n";
            }
            if (sensor4type.SelectedIndex != -1)
            {
                cmd[3] = NXT_ROVER_CONTROL.command_translation("cmd sensor value 4");
                temp += "Sensor 4: " + cmd[3].value + "\n";
            }   
                
                
                
                
                
                //update database
                sql = "INSERT INTO `rover`.`sensors` (`ID`, `when`, `result`) VALUES (NULL, '" + unixTime.ToString().Replace(',', '.') + "', '" + temp + "');";
                mysql.QueryCommand(sql);
                //show
                if (temp.Length < 5)
                {
                    status.Text = "Please select type of sensor below.";
                }
                else
                {
                    status.Text = temp;
                }
            }
            
        }

        private void send_command_Click(object sender, EventArgs e)
        {
            command.Text = NXT_ROVER_CONTROL.command_translation(command.Text).value; 
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            foreach (NXTBrick.SensorType type in NXT_ROVER_CONTROL.sensorTypes)
            {
                sensor1type.Items.Add(type.ToString());
                sensor2type.Items.Add(type.ToString());
                sensor3type.Items.Add(type.ToString());
                sensor4type.Items.Add(type.ToString());
            }

            comport.Text = config.Rover_port;
        }

        private void sensor1type_SelectedIndexChanged(object sender, EventArgs e)
        {
            config.sensorType[0] = NXT_ROVER_CONTROL.sensorTypes[sensor1type.SelectedIndex];
            
            //update
            NXT_ROVER_CONTROL.setSensors();
        }

        private void sensor2type_SelectedIndexChanged(object sender, EventArgs e)
        {
            config.sensorType[1] = NXT_ROVER_CONTROL.sensorTypes[sensor2type.SelectedIndex];
            //update
            NXT_ROVER_CONTROL.setSensors();
        }

        private void sensor3type_SelectedIndexChanged(object sender, EventArgs e)
        {
            config.sensorType[2] = NXT_ROVER_CONTROL.sensorTypes[sensor3type.SelectedIndex];
            //update
            NXT_ROVER_CONTROL.setSensors();
        }

        private void sensor4type_SelectedIndexChanged(object sender, EventArgs e)
        {
            config.sensorType[3] = NXT_ROVER_CONTROL.sensorTypes[sensor4type.SelectedIndex];
            //update
            NXT_ROVER_CONTROL.setSensors();
        }

        private void values_CheckedChanged(object sender, EventArgs e)
        {
            config.sensor_auto_on_off = values.Checked;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            config.Rover_port = comport.Text;
            rover.Start();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            rover.Abort();
        }

        

        
    }
    class mysql_results
    {
        public int ID;
        public double when;
        public string msg;
        public bool result;
    }
    class mysql_todo
    {
        public string sql;
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
                  res = mysql.QueryCommand("SELECT `ID`,`message`,`when` FROM `log_current_session` WHERE `status`='' AND `when`< '" + unixTime.ToString().Replace(',', '.') + "' AND `when`> '" + (unixTime-100).ToString().Replace(',', '.') + "' AND `type`='cmd' ORDER BY `when` ASC LIMIT 0,1");
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
                          cmd.CommandText = "UPDATE `log_current_session` SET `status`='" + result.value + "' WHERE `ID`=" + res.ID;
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
              try
              {

                  connection.Open();
                  IrcBot.log += "MYSQL connection ready\n";
              }
              catch
              {
                  IrcBot.log += "MYSQL isn't running!!!\n";
              }
              
              
          }
        public static void close(){
              
              connection.Close();
          }
        public static mysql_results pre_program(string id)
        {
            cmd.CommandText = "SELECT * FROM `pre_program` WHERE `masterID` = " + id;
            cmd.CommandType = CommandType.Text;
            MySqlDataReader reader = cmd.ExecuteReader();
            mysql_todo[] list = new mysql_todo[50];
            mysql_results res = new mysql_results();
            TimeSpan ts = (DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0));
            double unixTime = ts.TotalSeconds;
            int i=0,j=0;
            res.result = false;
            while (reader.Read())
            {
                if (reader.GetString(2).Length > 2)
                {
                    list[i] = new mysql_todo();
                    list[i].sql = "INSERT `log_current_session` SET `type`='cmd', `message`='" + reader.GetString(2) + "',`when`=" + (reader.GetInt32(1) + unixTime).ToString().Replace(',', '.') + ",`who_ID`=" + reader.GetUInt32(5);
                    res.result = true;
                    i++;
                }

            }
            
            reader.Close();
            for (j = 0; j < i; j++)
            {
                QueryCommand(list[j].sql);
                
            }
            return res;
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
        public static Boolean IRC_on_off;
        public static Boolean sensor_auto_on_off;
        public static string Mysql_server;
        public static string Mysql_database;
        public static string Mysql_user;
        public static string Mysql_password;
        public static int Rover_distance;
        public static int Rover_light;
        public static int Rover_touch;
        public static int Rover_sound;
        public static string Rover_port;
        public static NXTBrick.SensorType[] sensorType = new NXTBrick.SensorType[4];
        public static NXTBrick.SensorMode[] sensorMode = new NXTBrick.SensorMode[4];
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
      if (userNode.SelectSingleNode("enable").InnerText == "true")
      {
          IRC_on_off = true;
      }
      else
      {
          IRC_on_off = false;
      }
      userNodes = doc.SelectNodes("/settings/mysql");
      userNode = userNodes[0];
      Mysql_database = userNode.SelectSingleNode("database").InnerText;
      Mysql_password = userNode.SelectSingleNode("password").InnerText;
      Mysql_user = userNode.SelectSingleNode("user").InnerText;
      Mysql_server = userNode.SelectSingleNode("server").InnerText;
      userNodes = doc.SelectNodes("/settings/rover");
      userNode = userNodes[0];
      // TODO check sensor
      Rover_distance = Convert.ToInt32(userNode.SelectSingleNode("distance").InnerText);
      sensorType[Rover_distance - 1] = NXTBrick.SensorType.Reflection;
      sensorMode[Rover_distance - 1] = NXTBrick.SensorMode.PeriodicCounter;
      Rover_light = Convert.ToInt32(userNode.SelectSingleNode("light").InnerText);
      sensorType[Rover_light - 1] = NXTBrick.SensorType.LightInactive;
      sensorMode[Rover_light - 1] = NXTBrick.SensorMode.Raw;
      Rover_touch = Convert.ToInt32(userNode.SelectSingleNode("touch").InnerText);
      Rover_sound = Convert.ToInt32(userNode.SelectSingleNode("sound").InnerText);
      Rover_port = userNode.SelectSingleNode("port").InnerText;
      if (userNode.SelectSingleNode("auto").InnerText == "true")
      {
          sensor_auto_on_off = true;
      }
      else
      {
          sensor_auto_on_off = false;
      }
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
        // NXT brick
        public static NXTBrick nxt = new NXTBrick();
        // rugulation modes
        public static NXTBrick.MotorRegulationMode[] regulationModes = new NXTBrick.MotorRegulationMode[] {
            NXTBrick.MotorRegulationMode.Idle,
            NXTBrick.MotorRegulationMode.Speed,
            NXTBrick.MotorRegulationMode.Sync };
        // run states
        public static NXTBrick.MotorRunState[] runStates = new NXTBrick.MotorRunState[] {
            NXTBrick.MotorRunState.Idle,
            NXTBrick.MotorRunState.RampUp,
            NXTBrick.MotorRunState.Running,
            NXTBrick.MotorRunState.RampDown };
        // sensor types
        public static NXTBrick.SensorType[] sensorTypes = new NXTBrick.SensorType[] {
            NXTBrick.SensorType.NoSensor, NXTBrick.SensorType.Switch,
            NXTBrick.SensorType.Temperature, NXTBrick.SensorType.Reflection,
            NXTBrick.SensorType.Angle, NXTBrick.SensorType.LightActive,
            NXTBrick.SensorType.LightInactive, NXTBrick.SensorType.SoundDB,
            NXTBrick.SensorType.SoundDBA, NXTBrick.SensorType.Custom,
            NXTBrick.SensorType.Lowspeed, NXTBrick.SensorType.Lowspeed9V };
        // TODO check this
        // relative sensormode
        public static NXTBrick.SensorMode[] sensorModes_based_on_sensorTypes = new NXTBrick.SensorMode[] {
            NXTBrick.SensorMode.Raw, NXTBrick.SensorMode.Boolean, 
            NXTBrick.SensorMode.Celsius, NXTBrick.SensorMode.PeriodicCounter,
            NXTBrick.SensorMode.AngleSteps, NXTBrick.SensorMode.Raw,
            NXTBrick.SensorMode.Raw, NXTBrick.SensorMode.Raw,
            NXTBrick.SensorMode.Raw, NXTBrick.SensorMode.Raw,
            NXTBrick.SensorMode.Raw, NXTBrick.SensorMode.Raw
        };
        // sensor modes
        public static NXTBrick.SensorMode[] sensorModes = new NXTBrick.SensorMode[] {
            NXTBrick.SensorMode.Raw, NXTBrick.SensorMode.Boolean,
            NXTBrick.SensorMode.TransitionCounter, NXTBrick.SensorMode.PeriodicCounter,
            NXTBrick.SensorMode.PCTFullScale, NXTBrick.SensorMode.Celsius,
            NXTBrick.SensorMode.Fahrenheit, NXTBrick.SensorMode.AngleSteps };
        public static NXTBrick.Sensor[] sensors = new NXTBrick.Sensor[] {
            NXTBrick.Sensor.First,NXTBrick.Sensor.Second,NXTBrick.Sensor.Third,NXTBrick.Sensor.Fourth};
        // TODO Set defeult speed
        public static int[] speed_motors = {80,80,80};
        public static void setSensors()
        {
            if (nxt.SetSensorMode(NXTBrick.Sensor.First,
                config.sensorType[0],
                config.sensorMode[0]) != true)
            {
                IrcBot.log += "Failed setting input mode(1)\n";
            }
            if (nxt.SetSensorMode(NXTBrick.Sensor.Second,
                config.sensorType[1],
                config.sensorMode[1]) != true)
            {
                IrcBot.log += "Failed setting input mode(2)\n";
            }
            if (nxt.SetSensorMode(NXTBrick.Sensor.Third,
                config.sensorType[2],
                config.sensorMode[2]) != true)
            {
                IrcBot.log += "Failed setting input mode(3)\n";
            }
            if (nxt.SetSensorMode(NXTBrick.Sensor.Fourth,
                config.sensorType[3],
                config.sensorMode[3]) != true)
            {
                IrcBot.log += "Failed setting input mode(4)\n";
            }
        }
        public static void run()
        {
            
            if (nxt.Connect(config.Rover_port))
            {
                IrcBot.log += "Connected successfully\n";

                CollectInformation();
                setSensors();
                
            }
            else
            {
                
                IrcBot.log += "Failed connecting to NXT device\n";
                Thread.Sleep(5000);
                run();
            }
        }
        public static void CollectInformation()
        {
            
            // ------------------------------------------------
            // get NXT version
            string firmwareVersion;
            string protocolVersion;

            if (nxt.GetVersion(out protocolVersion, out firmwareVersion))
            {
                IrcBot.log += "firmwareVersion: " + firmwareVersion + "\n";
                IrcBot.log += "protocolVersion: " + protocolVersion + "\n";
                
            }
            else
            {
                IrcBot.log += "Failed getting verion\n";
            }

            // ------------------------------------------------
            // get device information
            string deviceName;
            byte[] btAddress;
            int btSignalStrength;
            int freeUserFlesh;

            if (nxt.GetDeviceInformation(out deviceName, out btAddress, out btSignalStrength, out freeUserFlesh))
            {
                IrcBot.log += "deviceName: " + deviceName;

                IrcBot.log += string.Format("{0} {1} {2} {3} {4} {5} {6}\n",
                    btAddress[0].ToString("X2"),
                    btAddress[1].ToString("X2"),
                    btAddress[2].ToString("X2"),
                    btAddress[3].ToString("X2"),
                    btAddress[4].ToString("X2"),
                    btAddress[5].ToString("X2"),
                    btAddress[6].ToString("X2")
                );

                IrcBot.log += btSignalStrength.ToString() + "\n";
                IrcBot.log += freeUserFlesh.ToString() + "\n";
            }
            else
            {
                IrcBot.log += "Failed getting device information\n";
            }


            // ------------------------------------------------
            // get battery level
            int batteryLevel;

            if (nxt.GetBatteryPower(out batteryLevel))
            {
                IrcBot.log += batteryLevel.ToString() + "\n";
            }
            else
            {
                IrcBot.log += "Failed getting battery level\n";
            }
             
        }
        public static nxt_result command_translation(string text_command)
        {
            nxt_result result = new nxt_result();
            arg_command = text_command.Split(' ');
            if (arg_command.Length == 4)
            {
                if (arg_command[1] == "run" && arg_command[2] == "program")
                {
                    return run_program_mysql(arg_command[3]);
                }
            }
            //check if NXT is connect
            if (!nxt.IsConnected)
            {
                result.result = false;
                result.value = "NXT is not connect!";
                return result;
            }
            //check type
            
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
                if (arg_command[1] == "motor" && arg_command[2] == "speed")
                {
                    return set_speed(arg_command[3],arg_command[4]);
                }
                if (arg_command[1] == "motor" && arg_command[2] == "degree")
                {
                    return set_speed(arg_command[3], arg_command[4]);
                }
            }
            if (arg_command.Length == 4)
            {
                if (arg_command[1] == "sensor" && arg_command[2] == "value")
                {
                    return get_sensor(arg_command[3]);
                }
            }
            if (arg_command.Length == 5)
            {
                if (arg_command[1] == "run" && arg_command[2] == "rover" && arg_command[3] == "program")
                {
                    return run_program(arg_command[4]);
                }
            }
            
            if (arg_command.Length == 4)
            {
                if (arg_command[1] == "stop" && arg_command[2] == "rover" && arg_command[2] == "program")
                {
                    return stop_program();
                }
            }
            return result;
        }
        public static nxt_result get_sensor(string sensor)
        {
            return get_sensor(Convert.ToInt32(sensor));
        }
        public static nxt_result get_sensor(int sensor)
        {
            nxt_result result = new nxt_result();
            NXTBrick.SensorValues sensorValues;
            result.result = false;
            result.type = "sensor";
            result.value = "0";
            // get input values
            if (nxt.GetSensorValue(sensors[sensor-1], out sensorValues))
            {
                result.result = sensorValues.IsValid;

                result.value = "Raw: " + sensorValues.Raw.ToString() + " normalized: " + sensorValues.Normalized.ToString();
                
               
                
            }
            else
            {
                IrcBot.log += "Failed getting input values\n";
            }
              
            
            
            return result;
        }
        public static nxt_result set_speed(string motors, string speed)
        {
            return set_speed(motors, Convert.ToInt32(speed));
        }
        public static nxt_result set_speed(string motors,int speed)
        {
            string[] motors_array;
            nxt_result motor_result = new nxt_result();
            motor_result.result = false;
            motor_result.value = "Motor not found.";
            motors_array = motors.ToLower().Split('v');
            foreach (string motor in motors_array)
            {
                if (motor.ToUpper() == "A")
                {
                    motor_result.result = true;
                    motor_result.value = "done!";
                    speed_motors[0] = speed;
                }
                if (motor.ToUpper() == "B")
                {
                    motor_result.result = true;
                    motor_result.value = "done!";
                    speed_motors[1] = speed;
                }
                if (motor.ToUpper() == "C")
                {
                    motor_result.result = true;
                    motor_result.value = "done!";
                    speed_motors[2] = speed;
                }
                if (motor.ToUpper() == "ALL")
                {
                    motor_result.result = true;
                    motor_result.value = "done!";
                    speed_motors[0] = speed;
                    speed_motors[1] = speed;
                    speed_motors[2] = speed;
                }
                if (motor_result.result == false)
                {
                    return motor_result;
                }
            }
            return motor_result;
        }
        public static nxt_result motor_on(string motors)
        {
            string[] motors_array;
            nxt_result motor_result = new nxt_result();
            motor_result.result = false;
            motor_result.value = "Motor not found.";
            motors_array = motors.ToLower().Split('v');
            foreach (string motor in motors_array)
            {
                if (motor.ToUpper() == "A")
                {
                    motor_result = motor_on(NXTBrick.Motor.A);
                }
                if (motor.ToUpper() == "B")
                {
                    motor_result = motor_on(NXTBrick.Motor.B);
                }
                if (motor.ToUpper() == "C")
                {
                    motor_result = motor_on(NXTBrick.Motor.C);
                }
                if (motor.ToUpper() == "ALL")
                {
                    motor_result = motor_on(NXTBrick.Motor.All);
                }
                if (motor_result.result == false)
                {
                    return motor_result;
                }
            }
            return motor_result;
        }
        public static nxt_result motor_on(NXTBrick.Motor motor)
        {
            nxt_result result = new nxt_result();
            NXTBrick.MotorState motorState = new NXTBrick.MotorState();
            if (motor == NXTBrick.Motor.A)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[0];
                motorState.TurnRatio = (sbyte)speed_motors[0];
            }
            if (motor == NXTBrick.Motor.B)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[1];
                motorState.TurnRatio = (sbyte)speed_motors[1];
            }
            if (motor == NXTBrick.Motor.C)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[2];
                motorState.TurnRatio = (sbyte)speed_motors[2];
            }
            // TODO If al motor's on handel correct speed
            if (motor == NXTBrick.Motor.All)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[0];
                motorState.TurnRatio = (sbyte)speed_motors[0];
            }
            motorState.Mode = NXTBrick.MotorMode.On;
            motorState.Regulation = NXTBrick.MotorRegulationMode.Speed;
            motorState.RunState = NXTBrick.MotorRunState.Running;
            // tacho limit
            // TODO edit this: number of dregree
            motorState.TachoLimit = 0;
            
            // set motor's state
            if (nxt.SetMotorState(motor, motorState) != true)
            {
                IrcBot.log += "Failed setting motor state\n";
                result.result = false;
                result.value = "Failed";
                return result;
            }
            result.result = true;
            result.value = "succed";
            return result;
        }
        public static nxt_result motor_degree(string motors, string degree)
        {
            string[] motors_array;
            nxt_result motor_result = new nxt_result();
            motor_result.result = false;
            motor_result.value = "Motor not found.";
            motors_array = motors.ToLower().Split('v');
            foreach (string motor in motors_array)
            {
                if (motor.ToUpper() == "A")
                {
                    motor_result = motor_degree(NXTBrick.Motor.A,Convert.ToInt32(degree));
                }
                if (motor.ToUpper() == "B")
                {
                    motor_result = motor_degree(NXTBrick.Motor.B, Convert.ToInt32(degree));
                }
                if (motor.ToUpper() == "C")
                {
                    motor_result = motor_degree(NXTBrick.Motor.C, Convert.ToInt32(degree));
                }
                if (motor.ToUpper() == "ALL")
                {
                    motor_result = motor_degree(NXTBrick.Motor.All, Convert.ToInt32(degree));
                }
                if (motor_result.result == false)
                {
                    return motor_result;
                }
            }
            return motor_result;
        }
        public static nxt_result motor_degree(NXTBrick.Motor motor, int degree)
        {
            nxt_result result = new nxt_result();
            NXTBrick.MotorState motorState = new NXTBrick.MotorState();
            if (motor == NXTBrick.Motor.A)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[0];
                motorState.TurnRatio = (sbyte)speed_motors[0];
            }
            if (motor == NXTBrick.Motor.B)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[1];
                motorState.TurnRatio = (sbyte)speed_motors[1];
            }
            if (motor == NXTBrick.Motor.C)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[2];
                motorState.TurnRatio = (sbyte)speed_motors[2];
            }
            // TODO If al motor's on handel correct speed
            if (motor == NXTBrick.Motor.All)
            {
                // prepare motor's state to set
                motorState.Power = (sbyte)speed_motors[0];
                motorState.TurnRatio = (sbyte)speed_motors[0];
            }
            motorState.Mode = NXTBrick.MotorMode.On;
            motorState.Regulation = NXTBrick.MotorRegulationMode.Speed;
            motorState.RunState = NXTBrick.MotorRunState.Running;
            // tacho limit
           
            motorState.TachoLimit = degree;

            // set motor's state
            if (nxt.SetMotorState(motor, motorState) != true)
            {
                IrcBot.log += "Failed setting motor state\n";
                result.result = false;
                result.value = "Failed";
                return result;
            }
            result.result = true;
            result.value = "succed";
            return result;
        }
        public static nxt_result motor_off(string motors)
        {
            string[] motors_array;
            nxt_result motor_result=new nxt_result();
            motor_result.result = false;
            motor_result.value = "Motor not found.";
            motors_array = motors.ToLower().Split('v');
            foreach (string motor in motors_array)
            {
                if (motor.ToUpper() == "A")
                {
                    motor_result = motor_off(NXTBrick.Motor.A);
                }
                if (motor.ToUpper() == "B")
                {
                    motor_result = motor_off(NXTBrick.Motor.B);
                }
                if (motor.ToUpper() == "C")
                {
                    motor_result = motor_off(NXTBrick.Motor.C);
                }
                if (motor.ToUpper() == "ALL")
                {
                    motor_result = motor_off(NXTBrick.Motor.All);
                }
                if (motor_result.result == false)
                {
                    return motor_result;
                }
            }
            return motor_result;
        }
        public static nxt_result motor_off(NXTBrick.Motor motor)
        {
            nxt_result result = new nxt_result();
            
            NXTBrick.MotorState motorState = new NXTBrick.MotorState();

            // prepare motor's state to set
            motorState.Power = (sbyte)0;
            motorState.TurnRatio = (sbyte)0;
            motorState.Mode = NXTBrick.MotorMode.None;
            motorState.Regulation = NXTBrick.MotorRegulationMode.Speed;
            motorState.RunState = NXTBrick.MotorRunState.Idle;
            // tacho limit
            motorState.TachoLimit = 0;

            // set motor's state
            if (nxt.SetMotorState(motor, motorState) != true)
            {
                IrcBot.log += "Failed setting motor state\n";
                result.result = false;
                result.value = "Failed";
                return result;
            }
            result.result = true;
            result.value = "succed";
            
            return result;
        }
        public static nxt_result run_program(string name)
        {
            nxt_result result = new nxt_result();
            if (nxt.RunProgram(name + ".RXE"))
            {
                
                result.result = true;
                result.value = "succed";
            }
            else
            {
                result.result = false;
                result.value = "Failed";
            }
            return result;

        }
        public static nxt_result stop_program()
        {
            nxt_result result = new nxt_result();
            if (nxt.StopProgram())
            {

                result.result = true;
                result.value = "succed";
            }
            else
            {
                result.result = false;
                result.value = "Failed";
            }
            return result;

        }
        public static nxt_result run_program_mysql(string name)
        {
            return run_program_mysql(Convert.ToInt32(name));
        }
        public static nxt_result run_program_mysql(int name)
        {
            nxt_result result = new nxt_result();
            mysql_results result_mysql = new mysql_results();
            
            result_mysql = mysql.pre_program(Convert.ToString(name));
           
            if (result_mysql.result == true)
            {
                result.result = true;
                result.value = "succed";
                
            }
            else
            {
                result.result = false;
                result.value = "Faild";
            }
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



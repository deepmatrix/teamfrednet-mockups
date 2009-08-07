namespace Lego_MindStorm_Control_Api
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.richTextBox1 = new System.Windows.Forms.RichTextBox();
            this.statusStrip1 = new System.Windows.Forms.StatusStrip();
            this.toolStripStatusLabel1 = new System.Windows.Forms.ToolStripStatusLabel();
            this.signal_bar = new System.Windows.Forms.ToolStripProgressBar();
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.clearLogToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.recontIRCToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.status = new System.Windows.Forms.Label();
            this.timer2 = new System.Windows.Forms.Timer(this.components);
            this.command = new System.Windows.Forms.TextBox();
            this.send_command = new System.Windows.Forms.Button();
            this.sensor1type = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.sensor2type = new System.Windows.Forms.ComboBox();
            this.label3 = new System.Windows.Forms.Label();
            this.sensor3type = new System.Windows.Forms.ComboBox();
            this.label4 = new System.Windows.Forms.Label();
            this.sensor4type = new System.Windows.Forms.ComboBox();
            this.values = new System.Windows.Forms.CheckBox();
            this.comport = new System.Windows.Forms.TextBox();
            this.label5 = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.statusStrip1.SuspendLayout();
            this.menuStrip1.SuspendLayout();
            this.SuspendLayout();
            // 
            // timer1
            // 
            this.timer1.Enabled = true;
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // richTextBox1
            // 
            this.richTextBox1.Location = new System.Drawing.Point(0, 27);
            this.richTextBox1.Name = "richTextBox1";
            this.richTextBox1.Size = new System.Drawing.Size(486, 353);
            this.richTextBox1.TabIndex = 0;
            this.richTextBox1.Text = "";
            this.richTextBox1.TextChanged += new System.EventHandler(this.richTextBox1_TextChanged);
            // 
            // statusStrip1
            // 
            this.statusStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.toolStripStatusLabel1,
            this.signal_bar});
            this.statusStrip1.Location = new System.Drawing.Point(0, 410);
            this.statusStrip1.Name = "statusStrip1";
            this.statusStrip1.Size = new System.Drawing.Size(760, 22);
            this.statusStrip1.TabIndex = 1;
            this.statusStrip1.Text = "statusStrip1";
            // 
            // toolStripStatusLabel1
            // 
            this.toolStripStatusLabel1.AutoSize = false;
            this.toolStripStatusLabel1.Name = "toolStripStatusLabel1";
            this.toolStripStatusLabel1.Size = new System.Drawing.Size(200, 17);
            this.toolStripStatusLabel1.Text = "Standby";
            // 
            // signal_bar
            // 
            this.signal_bar.Name = "signal_bar";
            this.signal_bar.Size = new System.Drawing.Size(100, 16);
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(760, 24);
            this.menuStrip1.TabIndex = 2;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.clearLogToolStripMenuItem,
            this.recontIRCToolStripMenuItem,
            this.exitToolStripMenuItem});
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(37, 20);
            this.fileToolStripMenuItem.Text = "File";
            // 
            // clearLogToolStripMenuItem
            // 
            this.clearLogToolStripMenuItem.Name = "clearLogToolStripMenuItem";
            this.clearLogToolStripMenuItem.Size = new System.Drawing.Size(208, 22);
            this.clearLogToolStripMenuItem.Text = "clear log";
            this.clearLogToolStripMenuItem.Click += new System.EventHandler(this.clearLogToolStripMenuItem_Click);
            // 
            // recontIRCToolStripMenuItem
            // 
            this.recontIRCToolStripMenuItem.Name = "recontIRCToolStripMenuItem";
            this.recontIRCToolStripMenuItem.Size = new System.Drawing.Size(208, 22);
            this.recontIRCToolStripMenuItem.Text = "Recont IRC(doesn\'t work)";
            this.recontIRCToolStripMenuItem.Click += new System.EventHandler(this.recontIRCToolStripMenuItem_Click);
            // 
            // exitToolStripMenuItem
            // 
            this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
            this.exitToolStripMenuItem.Size = new System.Drawing.Size(208, 22);
            this.exitToolStripMenuItem.Text = "exit";
            this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
            // 
            // status
            // 
            this.status.AutoSize = true;
            this.status.Location = new System.Drawing.Point(499, 33);
            this.status.Name = "status";
            this.status.Size = new System.Drawing.Size(46, 13);
            this.status.TabIndex = 3;
            this.status.Text = "Standby";
            // 
            // timer2
            // 
            this.timer2.Interval = 5000;
            this.timer2.Tick += new System.EventHandler(this.timer2_Tick);
            // 
            // command
            // 
            this.command.Location = new System.Drawing.Point(0, 386);
            this.command.Name = "command";
            this.command.Size = new System.Drawing.Size(417, 20);
            this.command.TabIndex = 4;
            // 
            // send_command
            // 
            this.send_command.Location = new System.Drawing.Point(424, 387);
            this.send_command.Name = "send_command";
            this.send_command.Size = new System.Drawing.Size(61, 20);
            this.send_command.TabIndex = 5;
            this.send_command.Text = "Do";
            this.send_command.UseVisualStyleBackColor = true;
            this.send_command.Click += new System.EventHandler(this.send_command_Click);
            // 
            // sensor1type
            // 
            this.sensor1type.FormattingEnabled = true;
            this.sensor1type.Location = new System.Drawing.Point(562, 159);
            this.sensor1type.Name = "sensor1type";
            this.sensor1type.Size = new System.Drawing.Size(121, 21);
            this.sensor1type.TabIndex = 6;
            this.sensor1type.SelectedIndexChanged += new System.EventHandler(this.sensor1type_SelectedIndexChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(499, 162);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(52, 13);
            this.label1.TabIndex = 7;
            this.label1.Text = "Sensor 1:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(499, 192);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(52, 13);
            this.label2.TabIndex = 9;
            this.label2.Text = "Sensor 2:";
            // 
            // sensor2type
            // 
            this.sensor2type.FormattingEnabled = true;
            this.sensor2type.Location = new System.Drawing.Point(562, 189);
            this.sensor2type.Name = "sensor2type";
            this.sensor2type.Size = new System.Drawing.Size(121, 21);
            this.sensor2type.TabIndex = 8;
            this.sensor2type.SelectedIndexChanged += new System.EventHandler(this.sensor2type_SelectedIndexChanged);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(499, 224);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(52, 13);
            this.label3.TabIndex = 11;
            this.label3.Text = "Sensor 3:";
            // 
            // sensor3type
            // 
            this.sensor3type.FormattingEnabled = true;
            this.sensor3type.Location = new System.Drawing.Point(562, 221);
            this.sensor3type.Name = "sensor3type";
            this.sensor3type.Size = new System.Drawing.Size(121, 21);
            this.sensor3type.TabIndex = 10;
            this.sensor3type.SelectedIndexChanged += new System.EventHandler(this.sensor3type_SelectedIndexChanged);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(499, 257);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(52, 13);
            this.label4.TabIndex = 13;
            this.label4.Text = "Sensor 4:";
            // 
            // sensor4type
            // 
            this.sensor4type.FormattingEnabled = true;
            this.sensor4type.Location = new System.Drawing.Point(562, 254);
            this.sensor4type.Name = "sensor4type";
            this.sensor4type.Size = new System.Drawing.Size(121, 21);
            this.sensor4type.TabIndex = 12;
            this.sensor4type.SelectedIndexChanged += new System.EventHandler(this.sensor4type_SelectedIndexChanged);
            // 
            // values
            // 
            this.values.AutoSize = true;
            this.values.Location = new System.Drawing.Point(502, 136);
            this.values.Name = "values";
            this.values.Size = new System.Drawing.Size(159, 17);
            this.values.TabIndex = 14;
            this.values.Text = "Enable auto sensor read out";
            this.values.UseVisualStyleBackColor = true;
            this.values.CheckedChanged += new System.EventHandler(this.values_CheckedChanged);
            // 
            // comport
            // 
            this.comport.Location = new System.Drawing.Point(562, 296);
            this.comport.Name = "comport";
            this.comport.Size = new System.Drawing.Size(121, 20);
            this.comport.TabIndex = 15;
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(499, 303);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(46, 13);
            this.label5.TabIndex = 16;
            this.label5.Text = "Comport";
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(602, 331);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(81, 22);
            this.button1.TabIndex = 17;
            this.button1.Text = "connect";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.Location = new System.Drawing.Point(518, 331);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(78, 21);
            this.button2.TabIndex = 18;
            this.button2.Text = "disconnect";
            this.button2.UseVisualStyleBackColor = true;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(760, 432);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.comport);
            this.Controls.Add(this.values);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.sensor4type);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.sensor3type);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.sensor2type);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.sensor1type);
            this.Controls.Add(this.send_command);
            this.Controls.Add(this.command);
            this.Controls.Add(this.status);
            this.Controls.Add(this.statusStrip1);
            this.Controls.Add(this.menuStrip1);
            this.Controls.Add(this.richTextBox1);
            this.MainMenuStrip = this.menuStrip1;
            this.Name = "Form1";
            this.Text = "Lego Mindstrom Remote Control";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Form1_unLoad);
            this.statusStrip1.ResumeLayout(false);
            this.statusStrip1.PerformLayout();
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.RichTextBox richTextBox1;
        private System.Windows.Forms.StatusStrip statusStrip1;
        private System.Windows.Forms.ToolStripStatusLabel toolStripStatusLabel1;
        private System.Windows.Forms.ToolStripProgressBar signal_bar;
        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem clearLogToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem recontIRCToolStripMenuItem;
        private System.Windows.Forms.Label status;
        private System.Windows.Forms.Timer timer2;
        private System.Windows.Forms.TextBox command;
        private System.Windows.Forms.Button send_command;
        private System.Windows.Forms.ComboBox sensor1type;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.ComboBox sensor2type;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.ComboBox sensor3type;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.ComboBox sensor4type;
        private System.Windows.Forms.CheckBox values;
        private System.Windows.Forms.TextBox comport;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
    }
}


; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{3F9057C7-52AA-4849-9AA1-815D692136AC}
AppName=Lego Mindstorm Remote Control
VersionInfoVersion=0.1.99.2
AppVerName=Lego Mindstorm Remote Control 0.1.99.3
AppPublisher=Frednet
AppPublisherURL=http://wiki.xprize.frednet.org/index.php/Portal:Lego_Mindstorms
AppSupportURL=http://wiki.xprize.frednet.org/index.php/Portal:Lego_Mindstorms
AppUpdatesURL=http://wiki.xprize.frednet.org/index.php/Portal:Lego_Mindstorms
DefaultDirName={pf}\Lego Mindstorm Remote Control
DefaultGroupName=Lego Mindstorm Remote Control
LicenseFile=C:\Users\marc\Documents\frednet\gpl3
OutputBaseFilename=setup
Compression=lzma
SolidCompression=yes
AppCopyright=Copyright (C) 2009 Frednet
[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: checkedonce
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Users\marc\Documents\frednet\google code source\legomindstorm\C api\Lego MindStorm Control Api\Lego MindStorm Control Api\bin\Release\Lego MindStorm Control Api.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\marc\Documents\frednet\google code source\legomindstorm\C api\Lego MindStorm Control Api\Lego MindStorm Control Api\bin\Release\*"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\marc\Documents\frednet\google code source\legomindstorm\web\rover_v_2_0\*"; DestDir: "c:\wamp\www\rover"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\Lego Mindstorm Remote Control"; Filename: "{app}\Lego MindStorm Control Api.exe"; WorkingDir: "{app}"
Name: "{group}\edit settings.xml"; Filename: "{app}\settings.xml"
Name: "{group}\edit settings.php"; Filename: "c:\wamp\www\rover\config.php"
Name: "{group}\{cm:ProgramOnTheWeb,Lego Mindstorm Remote Control}"; Filename: "http://wiki.xprize.frednet.org/index.php/Portal:Lego_Mindstorms"
Name: "{group}\local web interface"; Filename: "http://localhost/rover"
Name: "{commondesktop}\Lego Mindstorm Remote Control"; Filename: "{app}\Lego MindStorm Control Api.exe"; Tasks: desktopicon; WorkingDir: "{app}"
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\Lego Mindstorm Remote Control"; Filename: "{app}\Lego MindStorm Control Api.exe"; Tasks: quicklaunchicon; WorkingDir: "{app}"

[Run]
; Filename: "c:\wamp\wampmanager.exe"; Description: "{cm:LaunchProgram,Wampserver}"; Flags: nowait postinstall skipifsilent
; Filename: "{app}\Lego MindStorm Control Api.exe"; Description: "{cm:LaunchProgram,Lego Mindstorm Remote Control}"; Flags: nowait postinstall skipifsilent

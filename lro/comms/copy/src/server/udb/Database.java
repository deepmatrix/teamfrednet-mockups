package udb;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



public class Database {

    private final static Pattern ValidateUsername = Pattern.compile("[\\w@\\.]{3,32}");
    private final static Pattern ValidatePassword = Pattern.compile("[\\w\\p{Punct}]{3,32}");

    public final static boolean ValidateUsername(String username){
        return (ValidateUsername.matcher(username).matches());
    }
    public final static boolean ValidatePassword(String password){
        return (ValidatePassword.matcher(password).matches());
    }

    public final static String PropDriver = "jdbc.driver";
    public final static String PropUri = "jdbc.uri";
    public final static String PropUsername = "jdbc.username";
    public final static String PropPassword = "jdbc.password";
    public final static String PropShutdownSQL = "jdbc.shutdown.sql";
    public final static String PropCreatable = "jdbc.creatable";

    private final static Properties Props = new Properties();

    public final static String PropsFile = System.getProperty("user.home")+"/comms.copy.udb.properties";

    public final static String CreateFile = System.getProperty("user.home")+"/comms.copy.udb.create.sql";

    private static String Driver, Uri, Username, Password, ShutdownSQL;
    private static boolean Creatable;
    private static String[] CreateSQL;

    public final static void Init(){
        try {
            FileInputStream fin = new FileInputStream(PropsFile);
            try {
                Props.load(fin);
            }
            finally {
                fin.close();
            }
        }
        catch (IOException exc){
            throw new RuntimeException(PropsFile,exc);
        }
        Driver = Props.getProperty(PropDriver);
        Uri = Props.getProperty(PropUri);
        Username = Props.getProperty(PropUsername);
        Password = Props.getProperty(PropPassword);
        ShutdownSQL = Props.getProperty(PropShutdownSQL);
        Creatable = ("true".equalsIgnoreCase(Props.getProperty(PropCreatable)));

        try {
            if (null != Driver)
                Class.forName(Driver);
            else
                throw new RuntimeException("Missing value for configuration parameter named '"+PropDriver+"'.");
        }
        catch (ClassNotFoundException exc){
            throw new RuntimeException(Driver,exc);
        }
        if (Creatable){

            try {
                FileReader fin = new FileReader(CreateFile);
                try {
                    StringBuilder strbuf = new StringBuilder();
                    char[] iob = new char[1024];
                    int read;
                    while (0 < (read = fin.read(iob))){
                        strbuf.append(iob,0,read);
                    }
                    CreateSQL = strbuf.toString().split(";");
                }
                finally {
                    fin.close();
                }
            }
            catch (IOException exc){
                throw new RuntimeException(CreateFile,exc);
            }
            try {
                Connection con = Database.Open();
                try {
                    Statement stmt = con.createStatement();
                    try {
                        for (String expr : CreateSQL){
                            expr = expr.trim();
                            if (0 < expr.length()){
                                stmt.executeUpdate(expr);
                            }
                        }
                    }
                    finally {
                        stmt.close();
                    }
                }
                finally {
                    con.close();
                }
            }
            catch (SQLException exc){
                throw new RuntimeException(CreateFile,exc);
            }
        }
    }

    public final static Connection Open() throws SQLException {
        if (null != Uri)
            return DriverManager.getConnection(Uri,Username,Password);
        else
            throw new RuntimeException("Class not initialized");
    }

    public final static boolean Create(String username, String password, String first, String last, String gmloc, String email)
        throws SQLException
    {
        if (ValidateUsername(username) && ValidatePassword(password)){
            Connection con = Open();
            try {
                PreparedStatement stmt = con.prepareStatement("insert into COMMS_COPY_USER (USERNAME, PASSWORD, FIRSTNAME, LASTNAME, GMAPSREF, EMAIL) values (?, ?, ?, ?, ?, ?);");
                try {
                    stmt.setString(1,username);
                    stmt.setString(2,password);
                    stmt.setString(3,first);
                    stmt.setString(4,last);
                    stmt.setString(5,gmloc);
                    stmt.setString(6,email);
                    return (0 < stmt.executeUpdate());
                }
                finally {
                    stmt.close();
                }
            }
            finally {
                con.close();
            }
        }
        else
            return false;
    }

    public final static User Lookup(String username)
        throws SQLException
    {
        Connection con = Open();
        try {
            PreparedStatement stmt = con.prepareStatement("select UID, USERNAME, PASSWORD, FIRSTNAME, LASTNAME, GMAPSREF, EMAIL from COMMS_COPY_USER where USERNAME = ?;");
            try {
                stmt.setString(1,username);
                ResultSet rs = stmt.executeQuery();
                try {
                    if (rs.next())
                        return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                    else
                        return null;
                }
                finally {
                    rs.close();
                }
            }
            finally {
                stmt.close();
            }
        }
        finally {
            con.close();
        }
    }

    public final static boolean Authenticate(String username, BigInteger pad, BigInteger sig){
        try {
            User user = Lookup(username);
            if (null != user)
                return user.authenticate(pad,sig);
            else
                return false;
        }
        catch (SQLException exc){

            exc.printStackTrace();

            return false;
        }
    }

    public Database(){
        super();
    }


    public String getDriver(){
        return Driver;
    }
    public String getUri(){
        return Driver;
    }
    public String getUsername(){
        return Username;
    }
    public String getPassword(){
        return Password;
    }
    public String getShutdownSQL(){
        return ShutdownSQL;
    }
    public boolean isCreatable(){
        return Creatable;
    }
    public String[] getCreateSQL(){
        return CreateSQL;
    }


    /**
     * Test / init database
     */
    public static void main(String[] argv){
        try {
            Init();
            System.out.println("ok");
            System.exit(0);
        }
        catch (RuntimeException exc){
            System.out.println("err");
            exc.printStackTrace(System.err);
            System.exit(1);
        }
    }
}

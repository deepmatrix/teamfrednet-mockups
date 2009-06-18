package udb;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User 
    extends Object
{
    private final static Charset UTF8 = Charset.forName("UTF-8");

    public final int uid;

    public final String username, password, first, last, gmloc, email;


    User(int uid, String username, String password, String first, String last, String gmloc, String email){
        super();
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.first = first;
        this.last = last;
        this.gmloc = gmloc;
        this.email = email;
    }


    public boolean authenticate(BigInteger pad, BigInteger sig){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(pad.toByteArray());
            sha.update(this.password.getBytes(UTF8));
            BigInteger val = new BigInteger(sha.digest());

            return (val.equals(sig));
        }
        catch (NoSuchAlgorithmException exc){
            throw new RuntimeException("SHA",exc);
        }
    }
}

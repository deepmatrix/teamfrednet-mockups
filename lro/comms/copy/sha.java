
import java.math.BigInteger;
import java.security.MessageDigest;
import java.nio.charset.Charset;

public class sha {

    final static Charset UTF8 = Charset.forName("UTF-8");

    public static void main(String[] argv){
        if (3 == argv.length){
            try {
                BigInteger pad = new BigInteger(argv[0],16);
                BigInteger sig = new BigInteger(argv[1],16);
                String pas = argv[2];
                MessageDigest sha = MessageDigest.getInstance("SHA");
                sha.update(pad.toByteArray());
                sha.update(pas.getBytes(UTF8));
                BigInteger val = new BigInteger(sha.digest());
                if (val.equals(sig)){
                    System.out.println("ok");
                    System.exit(0);
                }
                else {
                    System.out.println("er");
                    System.exit(1);
                }
            }
            catch (Exception exc){
                exc.printStackTrace();
                System.exit(1);
            }
        }
        else {
            System.out.println("Usage: <pad> <hash> <pass>");
            System.exit(1);
        }
    }

}

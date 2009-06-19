import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Properties;

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
        else if (1 == argv.length){
            String test = argv[0];
            File file = new File(test);
            if (file.isFile()){
                /*
                 * test vectors in (msg)+':'+(md)+'\n' format
                 */
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String vector, vectorMessage, vectorDigest;
                    while (null != (vector = reader.readLine())){
                        int idx = vector.indexOf(':');
                        vectorMessage = vector.substring(0,idx++);
                        vectorDigest = vector.substring(idx);

                        BigInteger msg = new BigInteger(vectorMessage,16);
                        BigInteger vec = new BigInteger(vectorDigest,16);
                        MessageDigest sha = MessageDigest.getInstance("SHA-1");
                        sha.update(msg.toByteArray());
                        BigInteger val = new BigInteger(sha.digest());
                        if (val.equals(vec)){
                            System.out.println("ok "+vectorMessage);
                        }
                        else {
                            System.out.println("err "+vectorMessage);
                            System.out.println("\t"+vec.toString(16));
                            System.out.println("\t"+val.toString(16));
                            System.exit(1);
                        }
                    }
                    System.exit(0);
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                try {
                    MessageDigest sha = MessageDigest.getInstance("SHA");
                    sha.update(test.getBytes(UTF8));
                    BigInteger val = new BigInteger(sha.digest());
                    System.out.println(val.toString(16));
                    System.exit(0);
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
        }
        else if (2 == argv.length){
            String test = argv[0];
            try {
                BigInteger sig = new BigInteger(argv[1],16);
                MessageDigest sha = MessageDigest.getInstance("SHA");
                sha.update(test.getBytes(UTF8));
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

package org.teamfrednet.comms.copy;

import ps.http.Connection;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.nio.charset.Charset;

public class Client 
    extends Object
{
    public static final Charset UTF8 = Charset.forName("UTF-8"); 


    public static void main(String[] argv){
        if (3 != argv.length){
            System.err.println("Usage: <username> <password> <url>");
            System.exit(1);
        }
        else {
            try {
                String usr = argv[0];
                String pas = argv[1];
                String urp = argv[2];
                String pad = null, sig = null;
                {
                    BigInteger padInt = BigInteger.valueOf((new java.util.Random()).nextLong());
                    pad = padInt.toString(16);

                    MessageDigest sha = MessageDigest.getInstance("SHA");
                    sha.update(padInt.toByteArray());
                    sha.update(pas.getBytes(UTF8));
                    sig = new BigInteger(sha.digest()).toString(16);
                }
                urp += "?usr="+usr+"&pad="+pad+"&sig="+sig;
                URL url = new URL(urp);
                Connection connection = new Connection(url);
                try {
                    InputStream in = System.in;
                    OutputStream out = connection.getOutputStreamer();
                    try {
                        byte[] iob = new byte[0x200];
                        int read;
                        while (0 < (read = in.read(iob,0,0x200))){
                            out.write(iob,0,read);
                        }
                    }
                    finally {
                        out.close();
                    }
                }
                finally {
                    connection.disconnect();
                }
            }
            catch (Exception exc){
                synchronized(System.err){
                    System.err.print("Error> ");
                    exc.printStackTrace();
                }
                System.exit(1);
            }
        }
    }
}
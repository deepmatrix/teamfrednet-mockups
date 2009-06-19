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
    public static final Charset ASCI = Charset.forName("US-ASCII"); 


    public static void main(String[] argv){
        if (3 != argv.length){
            System.err.println("Usage:  <username> <password> <host-url>");
            System.err.println();
            System.err.println("For example");
            System.err.println();
            System.err.println("   Client  'test' 'test' 'http://uranium.applios.net:8989/'");
            System.err.println();
            System.exit(1);
        }
        else {
            try {
                String usr = argv[0];
                String pas = argv[1];
                String urp = argv[2];
                if (!urp.endsWith("/copy")){
                    if ('/' == urp.charAt(urp.length()-1))
                        urp += "copy";
                    else
                        urp += "/copy";
                }
                String pad = null, sig = null;
                {
                    pad = Long.toHexString((new java.util.Random()).nextLong());

                    MessageDigest sha = MessageDigest.getInstance("SHA");
                    sha.update(pad.getBytes(ASCI));
                    sha.update(pas.getBytes(ASCI));
                    sig = new BigInteger(1,sha.digest()).toString(16);
                }
                urp += "?usr="+usr+"&pad="+pad+"&sig="+sig;
                URL url = new URL(urp);
                Connection connection = new Connection(url);
                connection.setRequestMethod("POST");
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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class CTDP
    extends Object
{
    private volatile boolean lock;

    private volatile int tagA, tagB, tagC;

    private volatile byte[] payload;


    public CTDP(){
        super();
    }
    public CTDP(int tagA, int tagB, int tagC){
        super();
        this.setTagA(tagA);
        this.setTagB(tagB);
        this.setTagC(tagC);
    }


    public final boolean hasLock(){
        return this.lock;
    }
    public final CTDP setLock(boolean lock){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else {
            this.lock = lock;
            return this;
        }
    }
    public final CTDP lock(){
        this.lock = true;
        return this;
    }
    public final CTDP unlock(){
        this.lock = false;
        return this;
    }
    public final int getTagA(){
        return this.tagA;
    }
    public final CTDP setTagA(int value){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else if (-1 < value && 255 > value){
            this.tagA = value;
            return this;
        }
        else
            throw new IllegalArgumentException("Out of range '"+value+"'.");
    }
    public final int getTagB(){
        return this.tagB;
    }
    public final CTDP setTagB(int value){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else if (-1 < value && 255 > value){
            this.tagB = value;
            return this;
        }
        else
            throw new IllegalArgumentException("Out of range '"+value+"'.");
    }
    public final int getTagC(){
        return this.tagC;
    }
    public final CTDP setTagC(int value){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else if (-1 < value && 255 > value){
            this.tagC = value;
            return this;
        }
        else
            throw new IllegalArgumentException("Out of range '"+value+"'.");
    }
    public final byte[] getPayload(){
        if (this.lock){
            byte[] payload = this.payload;
            if (null == payload)
                return null;
            else {
                int len = payload.length;
                byte[] copy = new byte[len];
                System.arraycopy(payload,0,copy,0,len);
                return copy;
            }
        }
        else
            return this.payload;
    }
    public final byte[] getPayloadNotNull(){
        byte[] payload = this.payload;
        if (null == payload)
            return new byte[0];
        else {
            int len = payload.length;
            byte[] copy = new byte[len];
            System.arraycopy(payload,0,copy,0,len);
            return copy;
        }
    }
    public final CTDP setPayload(byte[] payload){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else {
            this.payload = payload;
            return this;
        }
    }
    public final int getPayloadLength(){
        byte[] payload = this.payload;
        if (null == payload)
            return 0;
        else 
            return payload.length;
    }
    public final int length(){
        byte[] payload = this.payload;
        if (null == payload)
            return 0;
        else 
            return payload.length;
    }
    public final InputStream readPayload(){
        return new CTDPInputStream(this);
    }
    public final OutputStream writePayload(){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else 
            return new CTDPOutputStream(this);
    }
    public final void read(InputStream in)
        throws IOException
    {
        if (this.lock)
            throw new IllegalStateException("Locked");
        else {
            int tagA = in.read();
            if (-1 < tagA){
                int tagB = in.read();
                if (-1 < tagB){
                    int tagC = in.read();
                    if (-1 < tagC){
                        int plen = SDNV.Read(in);
                        if (-1 < plen){
                            this.tagA = tagA;
                            this.tagB = tagB;
                            this.tagC = tagC;
                            if (0 == plen)
                                this.payload = null;
                            else {
                                byte[] payload = new byte[plen];
                                int read, ofs = 0;
                                while (0 < (read = in.read(payload,ofs,plen))){
                                    ofs += read;
                                    plen -= read;
                                    if (1 > plen)
                                        break;
                                }
                                if (0 == plen)
                                    this.payload = payload;
                                else
                                    throw new IOException("Error in read delta count '"+plen+"'.");
                            }
                        }
                        else
                            throw new EOFException();
                    }
                    else
                        throw new EOFException();
                }
                else
                    throw new EOFException();
            }
            else
                throw new EOFException();
        }
    }
    public final void write(OutputStream out)
        throws IOException
    {
        out.write(this.tagA);
        out.write(this.tagB);
        out.write(this.tagC);
        byte[] payload = this.payload;
        if (null == payload)
            SDNV.Write(out,0);
        else {
            int len = payload.length;
            if (0 == len)
                SDNV.Write(out,0);
            else {
                SDNV.Write(out,len);
                out.write(payload,0,len);
                out.flush();
            }
        }
    }
    public final void writeHTTPM(PrintStream out){
        out.println("Tag-A: "+this.tagA);
        out.println("Tag-B: "+this.tagB);
        out.println("Tag-C: "+this.tagC);
        int len = this.length();
        out.println("Content-Length: "+len);
        out.println();
        if (0 != len){
            byte[] payload = this.getPayloadNotNull();
            out.write(payload,0,len);
        }
        out.flush();
    }


    protected static void usage(PrintStream out){
        out.println();
        out.println("Form");
        out.println();
        out.println("   CTDP  <tag-a> <tag-b> <tag-c> ");
        out.println();
        out.println("Operation");
        out.println();
        out.println("   Wrap: read stdin until exhaustion, wrapping as payload. ");
        out.println("   Write product to stdout.");
        out.println();
        out.println("Form");
        out.println();
        out.println("   CTDP  - ");
        out.println();
        out.println("Operation");
        out.println();
        out.println("   Unwrap: read stdin for one CTDP.  To stdout write tags and");
        out.println("   payload in HTTP message format with header 'Content-Length'.");
        out.println("   Tag header names are 'Tag-A', 'Tag-B', 'Tag-C'.");
        out.println();
        out.println("Form");
        out.println();
        out.println("   CTDP  <file.in/ctdp>  <file.out/httpm> ");
        out.println();
        out.println("Operation");
        out.println();
        out.println("   Unwrap: read file.in for one CTDP.  To file.out write tags and");
        out.println("   payload in HTTP message format with header 'Content-Length'.");
        out.println("   Tag header names are 'Tag-A', 'Tag-B', 'Tag-C'.");
        out.println();
        System.exit(1);
    }
    public static void main(String[] argv){
        int argc = argv.length;
        if (3 == argc){
            try {
                int Ta = Integer.parseInt(argv[0]);
                int Tb = Integer.parseInt(argv[1]);
                int Tc = Integer.parseInt(argv[2]);
                CTDP ctdp = new CTDP(Ta, Tb, Tc);
                InputStream in = System.in;
                OutputStream payload = ctdp.writePayload();
                try {
                    int bb;
                    while (-1 < (bb = in.read())){
                        payload.write(bb);
                    }
                }
                finally {
                    payload.close();
                }
                ctdp.write(System.out);

                System.exit(0);
            }
            catch (Exception exc){
                exc.printStackTrace();
                System.exit(1);
            }
        }
        else if (2 == argc){
            File filein = new File(argv[0]);
            if (filein.isFile()){
                File fileout = new File(argv[1]);
                try {
                    InputStream fin = new FileInputStream(filein);
                    try {
                        CTDP ctdp = new CTDP();
                        ctdp.read(fin);

                        PrintStream fout = new PrintStream(new FileOutputStream(fileout));
                        try {
                            ctdp.writeHTTPM(fout);
                        }
                        finally {
                            fout.close();
                        }
                    }
                    finally {
                        fin.close();
                    }
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                usage(System.err);
            }
        }
        else if (1 == argc){
            String arg = argv[0];
            if (1 == arg.length() && '-' == arg.charAt(0)){
                CTDP ctdp = new CTDP();
                try {
                    ctdp.read(System.in);
                    ctdp.writeHTTPM(System.out);
                }
                catch (Exception exc){
                    exc.printStackTrace();
                    System.exit(1);
                }
            }
            else {
                usage(System.err);
            }
        }
        else {
            usage(System.err);
        }
    }
}

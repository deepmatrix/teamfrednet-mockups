
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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


    public boolean hasLock(){
        return this.lock;
    }
    public CTDP setLock(boolean lock){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else {
            this.lock = lock;
            return this;
        }
    }
    public CTDP lock(){
        this.lock = true;
        return this;
    }
    public CTDP unlock(){
        this.lock = false;
        return this;
    }
    public int getTagA(){
        return this.tagA;
    }
    public CTDP setTagA(int value){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else if (-1 < value && 255 > value){
            this.tagA = value;
            return this;
        }
        else
            throw new IllegalArgumentException("Out of range '"+value+"'.");
    }
    public int getTagB(){
        return this.tagB;
    }
    public CTDP setTagB(int value){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else if (-1 < value && 255 > value){
            this.tagB = value;
            return this;
        }
        else
            throw new IllegalArgumentException("Out of range '"+value+"'.");
    }
    public int getTagC(){
        return this.tagC;
    }
    public CTDP setTagC(int value){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else if (-1 < value && 255 > value){
            this.tagC = value;
            return this;
        }
        else
            throw new IllegalArgumentException("Out of range '"+value+"'.");
    }
    public byte[] getPayload(){
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
    public byte[] getPayloadNotNull(){
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
    public CTDP setPayload(byte[] payload){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else {
            this.payload = payload;
            return this;
        }
    }
    public InputStream readPayload(){
        return new CTDPInputStream(this);
    }
    public OutputStream writePayload(){
        if (this.lock)
            throw new IllegalStateException("Locked");
        else 
            return new CTDPOutputStream(this);
    }
    public void read(InputStream in)
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
    public void write(OutputStream out)
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


    protected static void usage(java.io.PrintStream out){
        out.println();
        out.println("Usage");
        out.println();
        out.println("   CTDP  <tag-a> <tag-b> <tag-c> ");
        out.println();
        out.println("Operation");
        out.println();
        out.println("   Read stdin until exhaustion, wrapping as payload. ");
        out.println("   Write product to stdout.");
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
        else {
            usage(System.err);
        }
    }
}

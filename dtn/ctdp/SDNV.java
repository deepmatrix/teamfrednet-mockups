
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SDNV
    extends Object
{

    public final static int Read(InputStream in)
        throws IOException
    {
        int value = 0;
        for (int cc = 3; ; cc--){
            int u8 = in.read();
            if (-1 == u8)
                throw new EOFException();
            else {
                int u7 = (u8 & 0x7f);
                value |= (u7 << (cc*7));
                if (0 == cc || u7 == u8)
                    return value;
            }
        }
    }
    public final static void Write(OutputStream out, int value)
        throws IOException
    {
        if (0 == value)
            out.write(0);
        else {
            boolean inline = false;
            int bb;
            for (int cc = 3; ; cc--){
                bb = ((value >>> (cc*7)) & 0x7f);
                if (inline || 0 != bb){
                    inline = true;
                    if (0 == cc){
                        out.write(bb);
                        return;
                    }
                    else {
                        bb |= 0x80;
                        out.write(bb);
                    }
                }
            }
        }
    }
}


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
        int value = 0, u7, u7a = 0, u7b = 0, u7c = 0;
        for (int cc = 3; ; cc--){
            int u8 = in.read();
            if (-1 == u8)
                throw new EOFException();
            else {
                u7 = (u8 & 0x7f);
                switch (cc){
                case 3:
                    if (u7 == u8)
                        return u7;
                    else {
                        u7a = u7;
                        break;
                    }
                case 2:
                    if (u7 == u8){
                        value |= (u7a << 7);
                        value |= u7;
                        return value;
                    }
                    else {
                        u7b = u7;
                        break;
                    }
                case 1:
                    if (u7 == u8){
                        value |= (u7a << 14);
                        value |= (u7b << 7);
                        value |= (u7);
                        return value;
                    }
                    else {
                        u7c = u7;
                        break;
                    }
                case 0:
                    value |= (u7a << 21);
                    value |= (u7b << 14);
                    value |= (u7c << 7);
                    value |= (u7);
                    return value;
                }
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


import java.io.IOException;

public class CTDPOutputStream
    extends java.io.ByteArrayOutputStream
{

    private final CTDP ctdp;


    public CTDPOutputStream(CTDP ctdp){
        super();
        if (null != ctdp)
            this.ctdp = ctdp;
        else
            throw new IllegalArgumentException();
    }


    public void flush()
        throws IOException
    {
        this.ctdp.setPayload(this.toByteArray());
    }
    public void close()
        throws IOException
    {
        this.ctdp.setPayload(this.toByteArray());
    }
}

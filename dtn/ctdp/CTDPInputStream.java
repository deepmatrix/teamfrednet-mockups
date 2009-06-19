

public class CTDPInputStream
    extends java.io.ByteArrayInputStream
{


    public CTDPInputStream(CTDP cdtp){
        super(cdtp.getPayloadNotNull());
    }

}

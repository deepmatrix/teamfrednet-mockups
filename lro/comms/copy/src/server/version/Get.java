package version;

import java.io.IOException;

import org.teamfrednet.comms.copy.Version;


public class Get 
    extends ps.Buffer
{

    public Get(){
        super();
    }

    public void init(ps.Request request)
        throws IOException
    {
        this.println(Version.Full);
        this.setContentType("text/plain");
        this.setStatusOk();
    }
}
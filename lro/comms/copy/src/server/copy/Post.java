package copy;

import ps.Output;
import ps.Request;
import ps.Welcome;

import udb.Database;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * <pre>
 * POST /copy?usr=username&amp;pad=hex&amp;sig=hex
 * </pre>
 * 
 * Write data file from streaming content body.  Response includes Location. 
 * 
 * <h3>Parameters </h3>
 * 
 * <dl>
 * <dt><tt>usr</tt></dt>
 * 
 * <dd> Comms Copy Username. </dd>
 * 
 * <dt><tt>pad</tt></dt>
 * 
 * <dd> A 64bit random nonce in hexidecimal (no prefix).</dd>
 * 
 * <dt><tt>sig</tt></dt>
 * 
 * <dd> A hexidecimal (no prefix) encoding of the <i>SHA-1</i> digest
 * of the binary value of <tt>pad</tt> and the string value of
 * <tt>password</tt>.  </dd>
 * 
 * </dl>
 * 
 * <h3>Responses </h3>
 * 
 * <dl>
 * <dt><tt>200 Ok</tt> or <tt>201 Created</tt></dt>
 * 
 * <dd> Writing (continuously in stream). </dd>
 * 
 * <dt><tt>400 Bad Request</tt></dt>
 * 
 * <dd> Missing request line parameters. </dd>
 * 
 * <dt><tt>401 Not Authorized</tt></dt>
 * 
 * <dd> Failed to authenticate from parameters.  </dd>
 * 
 * <dt><tt>403 Forbidden</tt></dt>
 * 
 * <dd> Unable to write to the implied file system location.  </dd>
 * 
 * </dl>
 */
public class Post 
    extends ps.File.Put
{
    /**
     * "YYYY +MM +DD +HH +MM +SS"
     */
    public final static java.lang.String FormatString = "yyyyMMddHHmmss";

    public final static DateFormat Format = new SimpleDateFormat(FormatString);

    public final static String ToString(long date){
        return Format.format(new java.util.Date(date));
    }



    public Post(){
        super();
    }


    @Override
    public void init(Request request)
        throws IOException
    {
        super.init(request);

        String usr = request.getParameter("usr");
        String pad = request.getParameter("pad");
        String sig = request.getParameter("sig");
        if (null != usr && null != pad && null != sig){
            BigInteger padInt = new BigInteger(pad,16);
            BigInteger sigInt = new BigInteger(sig,16);

            if (Database.Authenticate(usr,padInt,sigInt)){
                request.disableSocketTimeout();
                this.file = request.getLocationFile(ToString(System.currentTimeMillis()));
                this.setLocation(this.file);
                this.setStatusOk();

                this.writeInit(request);
            }
            else
                this.setStatusNotAuthorized();
        }
        else
            this.setStatusBadRequest();
    }
    @Override
    public void tail(Request request, Output out)
        throws IOException
    {
        if (this.isOk())
            this.writeTail(request);
    }

}

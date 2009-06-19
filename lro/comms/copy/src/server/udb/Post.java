package udb;

import ps.Output;
import ps.Request;
import ps.Welcome;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.SQLException;

/**
 * <pre>
 * POST /udb
 * </pre>
 * 
 * Create user
 * 
 * <h3>Parameters </h3>
 * 
 * <dl>
 * <dt><tt>usr</tt></dt>
 * 
 * <dd> Existing Comms Copy Username. </dd>
 * 
 * <dt><tt>pad</tt></dt>
 * 
 * <dd> A 64bit random nonce in hexidecimal (no prefix).</dd>
 * 
 * <dt><tt>sig</tt></dt>
 * 
 * <dd> A hexidecimal (no prefix) encoding of the <i>SHA-1</i> digest
 * of the string value of <tt>pad</tt> and the string value of
 * <tt>password</tt>.  </dd>
 * 
 * <dt><tt>username</tt></dt>
 * 
 * <dd> New username (required). </dd>
 * 
 * <dt><tt>password</tt></dt>
 * 
 * <dd> New user password (required). </dd>
 * 
 * <dt><tt>first</tt></dt>
 * 
 * <dd> New user first name. </dd>
 * 
 * <dt><tt>last</tt></dt>
 * 
 * <dd> New user last (family) name. </dd>
 * 
 * <dt><tt>gmloc</tt></dt>
 * 
 * <dd> New user href to google maps location. </dd>
 * 
 * <dt><tt>email</tt></dt>
 * 
 * <dd> New user email. </dd>
 * 
 * <dt><tt>success</tt></dt>
 * 
 * <dd> Redirect on success. </dd>
 * 
 * <dt><tt>failure</tt></dt>
 * 
 * <dd> Redirect on failure. </dd>
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
 * <dd> Unable to create in database.  </dd>
 * 
 * </dl>
 */
public class Post 
    extends ps.Reader
{



    public Post(){
        super();
    }


    @Override
    public void init(Request request)
        throws IOException
    {
        super.init(request);

        String success = request.getParameter("success");
        String failure = request.getParameter("failure");

        String usr = request.getParameter("usr");
        String pad = request.getParameter("pad");
        String sig = request.getParameter("sig");
        if (null != usr && null != pad && null != sig){
            usr = usr.toLowerCase().trim();
            try {
                BigInteger sigInt = new BigInteger(sig,16);

                if (Database.Authenticate(usr,pad,sigInt)){

                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    if (null != username && null != password){
                        username = username.toLowerCase().trim();
                        if (Database.ValidateUsername(username)){
                            if (Database.ValidatePassword(password)){
                                password = password.trim();
                                String first = request.getParameter("first");
                                String last = request.getParameter("last");
                                String gmloc = request.getParameter("gmloc");
                                String email = request.getParameter("email");
                                try {
                                    if (Database.Create(username,password,first,last,gmloc,email)){
                                        if (null != success)
                                            this.redirect(request,success+"?msg=User+successfully+created.&type=success");
                                        else
                                            this.setStatusOk();
                                    }
                                    else if (null != failure)
                                        this.redirect(request,failure+"?msg=Database+failed+to+create+record.&type=failure");
                                    else
                                        this.setStatusError();
                                }
                                catch (SQLException exc){

                                    exc.printStackTrace();

                                    if (null != failure)
                                        this.redirect(request,failure+"?msg="+exc.toString().replace(' ','+')+"&type=failure");
                                    else
                                        this.setStatusError();
                                }
                            }
                            else if (null != failure)
                                this.redirect(request,failure+"?msg=Invalid+password+string.&type=failure");
                            else
                                this.setStatusBadRequest();
                        }
                        else if (null != failure)
                            this.redirect(request,failure+"?msg=Invalid+username+string.&type=failure");
                        else
                            this.setStatusBadRequest();
                    }
                    else if (null != failure)
                        this.redirect(request,failure+"?msg=Missing+required+input.&type=failure");
                    else
                        this.setStatusBadRequest();
                }
                else if (null != failure)
                    this.redirect(request,failure+"?msg=Failed+to+authenticate.&type=failure");
                else
                    this.setStatusNotAuthorized();
            }
            catch (NumberFormatException exc){
                if (null != failure)
                    this.redirect(request,failure+"?msg=Missing+authentication+parameters.&type=failure");
                else
                    this.setStatusBadRequest();
            }
        }
        else if (null != failure)
            this.redirect(request,failure+"?msg=Missing+authentication+parameters.&type=failure");
        else
            this.setStatusBadRequest();
    }

}

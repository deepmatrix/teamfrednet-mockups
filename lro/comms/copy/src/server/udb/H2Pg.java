package udb;

import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * Start H2 in postgres compatibility mode.
 */
public class H2Pg {

    private final static String[] PgArgs = {
        "-pgPort",
        "5432"
    };

    static {
        try {
            Server.createPgServer(PgArgs).start();
        }
        catch (SQLException exc){
            exc.printStackTrace();
        }
    }


    public H2Pg(){
        super();
    }
}
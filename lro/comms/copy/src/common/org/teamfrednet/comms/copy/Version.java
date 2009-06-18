
package org.teamfrednet.comms.copy;

public final class Version
    extends Object
{
    public final static String Name = "teamfrednet-comms-copy";
    public final static int Major   =  0;
    public final static int Minor   =  0;
    public final static int Build   =  0;


    public final static String Number = String.valueOf(Major)+'.'+String.valueOf(Minor);

    public final static String Full = Name+'-'+Number;

    public final static boolean Release = (0 == Build);

    private Version(){
        super();
    }
}

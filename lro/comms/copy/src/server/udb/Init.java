package udb;

public class Init
    extends Object
    implements ps.Init
{

    public Init(){
        super();
    }

    public void init(ps.Server server){
        Database.Init();
    }

}

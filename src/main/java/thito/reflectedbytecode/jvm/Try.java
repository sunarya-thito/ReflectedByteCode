package thito.reflectedbytecode.jvm;

import thito.reflectedbytecode.*;

import java.lang.reflect.*;

public class Try {
    protected Runnable mainBody;
    protected Type type;
    protected Body<Caught> body;

    public Try(Runnable mainBody) {
        this.mainBody = mainBody;
    }

    public Catch Catch(Type type) {
        this.type = type;
        return new Catch(this);
    }

}

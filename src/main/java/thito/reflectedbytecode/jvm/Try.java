package thito.reflectedbytecode.jvm;

import thito.reflectedbytecode.Body;
import thito.reflectedbytecode.BodyAccessor;
import thito.reflectedbytecode.IClass;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Try {
    private Runnable mainBody;
    private Map<IClass, Body<BodyAccessor>> catchBodyMap = new HashMap<>();

    public Catch Catch(Type... throwables) {
        return new Catch(this, throwables);
    }

    public Try Finally(Runnable body) {

        return this;
    }

}

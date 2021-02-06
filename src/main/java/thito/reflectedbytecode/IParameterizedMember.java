package thito.reflectedbytecode;

import java.lang.reflect.Type;

public interface IParameterizedMember extends IMember {
    int getParameterCount();
    IClass[] getParameterTypes();
    Type[] getThrows();
}

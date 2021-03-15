package thito.reflectedbytecode;

import java.lang.reflect.*;

public class UField extends AbstractField {
    private UClass declaring;
    private String name;
    private IClass type;

    public UField(UClass declaring, String name) {
        this.declaring = declaring;
        this.name = name;
    }

    public UField hintType(IClass type) {
        this.type = type;
        return this;
    }

    @Override
    public IClass getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getModifiers() {
        return Modifier.PUBLIC;
    }

    @Override
    public IClass getDeclaringClass() {
        return declaring;
    }
}

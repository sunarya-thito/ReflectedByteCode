package thito.reflectedbytecode;

import java.lang.reflect.*;
import java.util.*;

public class LocalFieldMap {
    private ArrayList<ILocalField> preserved;
    public LocalFieldMap(GConstructor member) {
        if (!Modifier.isStatic(member.getModifiers())) {
            preserved = new ArrayList<>(member.getParameterCount() + 1);
            preserved.add(new LField(member.getDeclaringClass(), 0));
            for (int i = 0; i < member.getParameterCount(); i++) {
                preserved.add(member.getArgument(i));
            }
        } else {
            preserved = new ArrayList<>(member.getParameterCount());
            for (int i = 0; i < member.getParameterCount(); i++) {
                preserved.add(member.getArgument(i));
            }
        }
    }
    public LocalFieldMap(GMethod member) {
        if (!Modifier.isStatic(member.getModifiers())) {
            preserved = new ArrayList<>(member.getParameterCount() + 1);
            preserved.add(new LField(member.getDeclaringClass(), 0));
            for (int i = 0; i < member.getParameterCount(); i++) {
                preserved.add(member.getArgument(i));
            }
        } else {
            preserved = new ArrayList<>(member.getParameterCount());
            for (int i = 0; i < member.getParameterCount(); i++) {
                preserved.add(member.getArgument(i));
            }
        }
    }
    public ILocalField getField(int index) {
        return preserved.get(index);
    }
    public ILocalField createField(IClass type) {
        LField field = new LField(type, preserved.size());
        preserved.add(field);
        return field;
    }

    public ArrayList<ILocalField> getPreserved() {
        return preserved;
    }
}

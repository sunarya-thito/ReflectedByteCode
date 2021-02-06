package thito.reflectedbytecode;

import java.lang.reflect.Modifier;

public class GModifierLocalField<T extends ILocalField & GMember> extends GModifier<T> {
    public GModifierLocalField(T member) {
        super(member);
    }

    public GModifierLocalField<T> makeFinal() {
        return (GModifierLocalField<T>) or(Modifier.FINAL);
    }
}

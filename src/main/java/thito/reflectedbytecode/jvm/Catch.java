package thito.reflectedbytecode.jvm;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class Catch {
    private Try aTry;
    private Type[] thrown;

    public Catch(Try aTry, Type[] thrown) {
        this.aTry = aTry;
        this.thrown = thrown;
    }

    public Try Thrown(Consumer<Thrown> accessor) {
        return aTry;
    }
}

import thito.reflectedbytecode.*;
import thito.reflectedbytecode.jvm.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class APIDesignTest extends HashMap<String, Integer> {

    public static void main(String[] args) throws Throwable {
        try (Context context = Context.open()) {
            GClass exampleClass = context.createClass("a");
            exampleClass.declareMethod("main").modifier().makeStatic().makePublic().done().parameters(String[].class).body(instance -> {
                Java.Class(System.class).staticField("out").method("println", boolean.class)
                        .invokeVoid(10);
            }).exceptionThrows(Throwable.class).annotate(Deprecated.class);
            byte[] bytes = context.writeClass(exampleClass);
            new FileOutputStream("D:/test.class").write(bytes);
            ClassLoader loader = context.loadIntoMemory(APIDesignTest.class.getClassLoader());
            Class<?> ex = Class.forName("a", true, loader);
            System.out.println(Arrays.toString(ex.getMethods()));
            ex.getMethod("main", String[].class).invoke(null, new Object[] {new String[15]});
        }
    }

    public static final class A {
        public A(double a, double b, double c) {
            System.out.println(a+", "+b+", "+c);
        }
    }

    public static class OML {
        protected static void success() {
            System.out.println("success!");
        }
    }

}

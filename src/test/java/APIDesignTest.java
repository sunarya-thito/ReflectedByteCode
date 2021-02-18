import thito.reflectedbytecode.*;

import java.io.*;

import static thito.reflectedbytecode.jvm.Java.*;

public class APIDesignTest {

    public static void main(String[] args) throws Throwable {
        try (Context context = Context.open()) {

            GClass exampleClass = context.createClass("test.Example");

            exampleClass.declareMethod("main").modifier().makeStatic().done().parameters(String[].class).body(instance -> {

                If(instance.getArgument(0).asArray().length()).isLessThan(10).Then(() -> {
                    IClass.fromClass(System.class).getField("out").ifPresent(out -> {
                        IClass.fromClass(PrintStream.class).getMethod("println", String.class).ifPresent(println -> {
                            println.invoke(out.get(null), "it has many arguments!");
                        });
                    });
                }).Else(() -> {
                    IClass.fromClass(System.class).getField("out").ifPresent(out -> {
                        IClass.fromClass(PrintStream.class).getMethod("println", String.class).ifPresent(println -> {
                            println.invoke(out.get(null), "now that is weird");
                        });
                    });
                });

            }).exceptionThrows(Throwable.class).annotate(Deprecated.class);

            ClassLoader loader = context.loadIntoMemory();
            Class<?> ex = Class.forName("test.Example", true, loader);
            ex.getMethod("main", String[].class).invoke(null, new Object[] {new String[15]});

        }
    }

}

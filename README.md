# ReflectedByteCode
Generate Dynamic Class as easy as reflection

## Example

```java
package your.program;

import thito.reflectedbytecode.*;

import java.io.FileOutputStream;
import java.io.PrintStream;

import static thito.reflectedbytecode.jvm.Java.*;
import static thito.reflectedbytecode.jvm.Java.Math.*;

public class Example {

    public static void main(String[] args) throws Throwable {
        try (Context context = Context.open()) {

            GClass clazz = context.createClass("example.TestClass");

            // Create a field
            clazz.declareField("who", String.class)
                    .modifier()
                    .makeStatic()
                    .done();

            // Create a method
            clazz.declareMethod("hello")
                    .modifier()
                    .makeStatic()
                    .done()
                    .body(instance -> {
                        clazz.getDeclaredField("who").ifPresent(who -> {
                            Object whoValue = who.get(null);
                            clazz.getDeclaredConstructor(String.class).ifPresent(constructor -> {
                                Reference newInstance = constructor.newInstance(args);

                                // import static from thito.reflectedbytecode.jvm.Java
                                If(newInstance).isNotNull().Then(() -> {

                                    // careful!
                                    // toString and ToString
                                    // is different!
                                    newInstance.ToString();
                                    // Do whatever you want...
                                }).Else(() -> {
                                    If(newInstance).isNull().Then(() -> {
                                        Try(() -> {
                                            newInstance.ToString();
                                        }).Catch(NullPointerException.class).Caught(npe -> {
                                            // process the null pointer exception here
                                        }).Catch(Error.class, Exception.class).Caught(error -> {

                                            // Create a local field
                                            LField local = instance.createVariable();
                                            local.set(add(100, multiply(100, 40)));

                                            LField local2 = instance.createVariable();
                                            local2.set(NewArray(int.class, 1));

                                            local2.get().asArray().set(local.get());

                                        });
                                    });
                                });
                            });
                        });
                    });

            // Create a constructor
            clazz.declareConstructor()
                    .parameters(String.class)
                    .body(instance -> {
                        IClass.fromClass(System.class).getField("out").ifPresent(out -> {
                            IClass.fromClass(PrintStream.class).getMethod("println", String.class).ifPresent(println -> {
                                println.invoke(instance, instance.getArgument(0));
                            });
                        });
                    });

            // write it into a file?
            try (FileOutputStream outputStream = new FileOutputStream("Example.class")) {
                outputStream.write(context.writeClass(clazz));
            }

            // Load it into memory?
            ClassLoader loader = context.loadIntoMemory();
            Class<?> example = loader.getClass("example.TestClass");
            example.getMethod("hello").invoke(null);
        }
    }

}
```

## Naming Convention
### G-Prefix
Stands for "generated". 
This applies to all generated elements i.e. GAnnotation (Generated Annotation), 
GClass (Generated Class), GField (Generated Field), GMethod (Generated Method),
GConstructor (Generated Constructor).

### K-Prefix
Stands for "known". 
This applies to all known elements (that has been loaded into the memory/runtime) i.e.
KClass, KConstructor, KField, KMethod. In other words, this is a wrapper for java Classes/Fields/Methods/Constructors.

### L-Prefix
Stands for "local". This applies to all local (body) elements i.e. LField (Local Field).

### P-Prefix
Stands for "parameter". This applies to all parameter elements i.e. PField (Parameter/Argument Field).

### I-Prefix
Stands for "interface". This is the API. (e.g. IClass, IConstructor, etc)

## Important Notice
### IClass Inheritance
`thito.reflectedbytecode.IClass` extends `java.lang.reflect.Type` so you can use it in any `Type` parameter (including VarArgs and Arrays).
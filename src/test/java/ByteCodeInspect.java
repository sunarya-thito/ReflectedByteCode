import java.util.*;

public class ByteCodeInspect {
    public static void main(String[] args) {
        String.join("a", Arrays.copyOfRange(args, 0, 1));
        String na = (String) new Object();
        System.out.println(na);
    }


}

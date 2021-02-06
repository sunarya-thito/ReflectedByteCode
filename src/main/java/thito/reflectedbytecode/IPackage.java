package thito.reflectedbytecode;

import java.net.URL;

public interface IPackage {
    String getName();

    String getSpecTitle();

    String getSpecVersion();

    String getSpecVendor();

    String getImplTitle();

    String getImplVersion();

    String getImplVendor();

    URL getSealBase();
}

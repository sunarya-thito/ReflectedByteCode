package thito.reflectedbytecode;

import java.net.URL;

public class Package implements IPackage {
    private String name;
    private String specTitle;
    private String specVersion;
    private String specVendor;
    private String implTitle;
    private String implVersion;
    private String implVendor;
    private URL sealBase;

    public Package(java.lang.Package pack) {
        this(pack.getName(), pack.getSpecificationTitle(),
                pack.getSpecificationVersion(), pack.getSpecificationVendor(),
                pack.getImplementationTitle(),
                pack.getImplementationVersion(),
                pack.getImplementationVendor(),
                null);
    }

    public Package(String name) {
        this(name, null, null, null, null, null, null, null);
    }

    public Package(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) {
        this.name = name;
        this.specTitle = specTitle;
        this.specVersion = specVersion;
        this.specVendor = specVendor;
        this.implTitle = implTitle;
        this.implVersion = implVersion;
        this.implVendor = implVendor;
        this.sealBase = sealBase;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSpecTitle() {
        return specTitle;
    }

    public void setSpecTitle(String specTitle) {
        this.specTitle = specTitle;
    }

    @Override
    public String getSpecVersion() {
        return specVersion;
    }

    public void setSpecVersion(String specVersion) {
        this.specVersion = specVersion;
    }

    @Override
    public String getSpecVendor() {
        return specVendor;
    }

    public void setSpecVendor(String specVendor) {
        this.specVendor = specVendor;
    }

    @Override
    public String getImplTitle() {
        return implTitle;
    }

    public void setImplTitle(String implTitle) {
        this.implTitle = implTitle;
    }

    @Override
    public String getImplVersion() {
        return implVersion;
    }

    public void setImplVersion(String implVersion) {
        this.implVersion = implVersion;
    }

    @Override
    public String getImplVendor() {
        return implVendor;
    }

    public void setImplVendor(String implVendor) {
        this.implVendor = implVendor;
    }

    @Override
    public URL getSealBase() {
        return sealBase;
    }

    public void setSealBase(URL sealBase) {
        this.sealBase = sealBase;
    }
}

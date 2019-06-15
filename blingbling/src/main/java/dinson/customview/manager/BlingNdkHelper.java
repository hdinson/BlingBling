package dinson.customview.manager;

public class BlingNdkHelper {
    static {
        System.loadLibrary("bling-lib");
    }

    public static native String getFromC();

}

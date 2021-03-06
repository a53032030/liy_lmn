package lmn.com.lmnlibrary;

import lmn.com.lmnlibrary.imageloader.ILoader;

/**
 * Created by admin on 2017/3/9.
 */

public class CommonConfig {
    public static int HTTP_READ_TIME_OUT = 15;
    public static int HTTP_CONNECT_TIME_OUT = 15;
    public static String BASE_URL = "http://59.49.105.180:34558/";
    public static boolean DEBUG = true;
    public static String SHARE_PREFERENCE_FILE_NAME = "liylmn";
    public static boolean LOGIN = true;
    // #imageloader
    public static final int IL_LOADING_RES = ILoader.Options.RES_NONE;
    public static final int IL_ERROR_RES = ILoader.Options.RES_NONE;
}

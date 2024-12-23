package com.daffodils.psychiatry.helper;

import android.app.Application;

import java.text.DecimalFormat;

public class GlobalConst extends Application {

    public static String URL = "https://daffodilspsychiatry.com/DaffodilsServices.ashx";

    public static int MAJOR_VER = 2;
    public static int MINOR_VER = 8;

    public static String TOOLBAR_TITLE;
    public static String ERROR = "error";
    public static String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    public static String APP_COMPATABILITY_VERSION = "1.7"; // change from 1.6 to 1.7 (02-04-2023)
    public static String SERVICE_TYPE= "service_type";

    public static String SC_REGISTRATION = "1";
    public static String SC_GET_ALL_COURSES = "2";
    public static String SC_GET_COURSE_MODULES = "3";
    public static String SC_GET_SAMPLE_VIDEOS = "4";
    public static String SC_GET_COURSE_VIDEOS = "5";
    public static String SC_CHANGE_PASSWORD = "6";
    public static String SC_FORGET_PASSWORD = "7";
    public static String SC_LOGIN = "8";
    public static String SC_APP_COMPATIBILITY_VERSION = "9";
    public static String SC_UPDATE_USER_PROFILE = "10";
    public static String SC_HELP_AND_SUPPORT = "11";
    public static String SC_SUBSCRIBE_MODULES = "12";
    public static String SC_APP_IMAGES = "13";
    public static String SC_UPDATE_DEVICEID = "14";
    public static String SC_GET_SUBSCRIBED_MODULES = "15";
    public static String SC_ADD_TO_CART = "16";
    public static String SC_GET_CART = "17";
    public static String SC_SAVE_RECHARGE_DETAILS = "18";
    public static String SC_GET_RECHARGE_DETAILS = "19";
    public static String SC_APPLY_COUPON = "20";
    public static String SC_GET_PROFILE_DETAILS = "21";
    public static String SC_GET_SUBSCRIBED_VIDEO_DETAILS = "22";
    public static String SC_GET_COURSE_DURATION ="23";

    public static String Username = "", Name = "", Mobile = "", Address = "", ModuleID = "", User_id = "", Password = "", DeviceID = "";
    public static String Result = "", Description="", GetPassword ="", AppVersion = "", isDeviceChanged = "";
    public static String VIDEO_TYPE ;
    public static String DISCOUNT= "discount";
    public static String OFFLINE_MODE = "False";
    public static String FINALAMOUNT = "finalamt";
    public static String TRANCHARGES = "trancharges";
    public static String CART_ADD = "1";
    public static String CART_REMOVE = "2";
    public static String FULL_COURSE_DURATION = "";
    public static String CRASH_COURSE_DURATION = "";
    public static String MRCPSYCH_DURATION = "";
    public static String ANY_MODULE_DURATION = "2 Months";
    public static String COMBINED_COURSE_DURATION = "";
    public static String UPDATED_FOUNDATION_COURSE_DURATION = "1 year";


    public static DecimalFormat formater = new DecimalFormat("#,##,##,###.00");
    public static String SETTING_CURRENCY_SYMBOL = "Rs ";

    // Subscription Types ID--------------------------------------------------

    public static String FullCourse = "1";
    public static String CrashCourse = "2";
    public static String AnyModule = "3";
    public static String MRCPsych = "5";
    public static String MRCPsychStaticsPaperB = "6";
    public static String CombinedCourse = "7";
    //------------------------------------------------------------------------

    // Courses IDs (Not in USE)-----------------------------------------------------------

    public static String FoundationCourseID = "1";
    public static String CrashCourseID = "2";

    //--------------------------------------------------------------------------

    // Module IDs --------------------------------------------------------------

    public static String Module1ID = "1";
    public static String Module2ID = "2";
    public static String Module3ID = "3";
    public static String Module4ID = "4";
    public static String Module5ID = "5";
    public static String Module6ID = "6";
    public static String Module7ID = "7";
    public static String Module8ID = "8";
    public static String Module9ID = "9";

    public static String CModule1ID = "15";
    public static String CModule2ID = "16";
    public static String CModule3ID = "17";
    public static String CModule4ID = "18";
    public static String CModule5ID = "19";
    public static String CModule6ID = "20";
    public static String CModule7ID = "21";
    public static String CModule8ID = "22";
    public static String CModule9ID = "23";
    public static String CModule10ID = "24";

    public static String CModule11ID = "25";
}

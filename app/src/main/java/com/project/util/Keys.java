package com.project.util;

public class Keys {
    public static String HOME_PATH= "https://myfinalyearproject.in/2025/d/womenSafety/";
    //public static String HOME_PATH= "http://192.168.0.156/cproject/2025/d/womenSafety/";
    public static String ADMIN_PATH = HOME_PATH + "admin/";
    public static String USER_PATH = HOME_PATH + "user/";

    public static class URL{
        public static String ADMIN_LOGIN = ADMIN_PATH + "admin_login.php";
        public static String ADD_USER = ADMIN_PATH + "add_user.php";
        public static String USER_LOGIN = USER_PATH + "user_login.php";
        public static String GET_USER_LOCATION = USER_PATH + "get_user_location.php";
        public static String USER_LOGOUT = USER_PATH + "user_logout.php";
    }

    public static class FireKey {
        public static String F_TOKEN = "f_token";

    }
}

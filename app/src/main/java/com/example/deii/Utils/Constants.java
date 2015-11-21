package com.example.deii.Utils;

/**
 * Created by deii on 13-10-2015.
 */
public class Constants {
    public static final String TRUST_ONE_PREFERENCE = "trust_one";
    /*
    WebService Constants
     */
    public static String EMAIL_ID = "emailid";
    public static String USERNAME = "username";
    public static String PHONE_NUMBER = "phonenumber";
    public static String PROFILE_IMAGE = "profileimage";
    public static String ARRAY = "Array";
    public static String STATUS_CODE = "statuscode";
    public static String MESSAGE = "message";
    public static String PASSWORD = "password";
    public static String DATA = "data";
    public static String CATEGORIES = "categories";
    public static String SUB_CATEGORIES = "subcategories";
    public static String CATEGORY_ID = "category_id";
    public static String SUB_CATEGORY_ID = "subcategory_id";
    public static String NAME = "name";
    public static String PRODUCTS = "products";
    public static String TOPICS = "topics";

    public interface webServiceSendKeys {
        public static String EMAIL_ID = "EmailID";
    }

    public interface WebServices {

        //public static String BASE = "http://whatsupguys.in/demo/trust1_api/api/";

        public static String BASE = "http://www.trustoneapp.com/api/api/";

        public static String SIGN_UP = BASE + "registration";

        public static String LOG_IN = BASE + "login";

        public static String HOME = BASE + "home";
    }
}

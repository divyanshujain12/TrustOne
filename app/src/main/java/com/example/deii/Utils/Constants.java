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
    public static String LOG_IN = "log_in";
    public static String PHONE_NUMBER = "phonenumber";
    public static String PROFILE_IMAGE = "profileimage";
    public static String SUBSCRIPTION_ARRAY = "subscriptionArray";
    public static String PRICE = "price";
    public static String STATUS_CODE = "statuscode";
    public static String MESSAGE = "message";
    public static String PASSWORD = "password";
    public static String DATA = "data";
    public static String CATEGORIES = "categories";
    public static String SUB_CATEGORIES = "subcategories";
    public static String CATEGORY_ID = "category_id";
    public static String CITY = "City";
    public static String PROFESSIONAL_LICENSE = "professional_license";
    public static String STATE = "state";
    public static String CATEGORY_NAME = "category_name";
    public static String NAME = "name";
    public static String PRODUCTS = "products";
    public static String BANNER = "banners";
    public static String TOPICS = "topics";
    public static String TOPIC_ID = "topicid";
    public static String TYPE = "type";
    public static String PAGE_NO = "pageno";
    public static String PAGE_SIZE = "pagesize";
    public static String EMAIL = "email";
    public static String CARD = "card";
    public static String EXP = "exp";
    public static String AMOUNT = "amount";
    public static String CARD_HOLDER = "cardholder";
    public static String STREET = "street";
    public static String ZIP = "zip";
    public static String DESCRIPTION = "description";
    public static String CVV = "cvv2";
    public static String AUTO_SUBSCRIBE = "auto_subscribe";
    public static String ORDER_ID = "order_id";
    public static String IS_FIRST_CLICK_IN_MASTER_HEALER = "first_click_master_healer";


    public interface webServiceSendKeys {
        public static String EMAIL_ID = "EmailID";
        public static String SUB_CATEGORY_ID = "subcategory_id";

    }

    public interface WebServices {

        //public static String BASE = "http://whatsupguys.in/demo/trust1_api/api/";

        String BASE = "http://www.trustoneapp.com/api/api/";

        String SIGN_UP = BASE + "registration";

        String LOG_IN = BASE + "login";

        String HOME = BASE + "home";

        String TOPIC_BY_SUB_CAT_ID = BASE + "getTopicsBySubcategoryId";

        String PRODUCT_BY_ID = BASE + "getProductById";

        String FORGOT_PASSWORD = BASE + "forgetpassword";

        String UPDATE_PASSWORD = BASE + "updatePassword";

        String CONTACT_US = BASE + "contactus";

        String UPDATE_PROFILE = BASE + "updateprofile";

        String CHECKOUT = BASE + "checkout";

        String GET_ORDERS = BASE+"subscribe";

        String CANCEL_RENEWAL = BASE+"cancelsubscribe";



    }
}

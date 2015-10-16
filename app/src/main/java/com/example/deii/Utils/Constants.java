package com.example.deii.Utils;

/**
 * Created by deii on 13-10-2015.
 */
public class Constants {
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



    public interface  WebServices{

        public static String BASE ="http://whatsupguys.in/demo/trust1_api/api/";

        public static String SIGN_UP = BASE+"registration";

        public static String LOG_IN = BASE+"login";
    }
}

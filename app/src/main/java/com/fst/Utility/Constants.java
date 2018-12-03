package com.fst.Utility;


public class Constants {

    public static final String MY_BID_DETAIL = "my_bid_detail";
    public static final int EDIT_BID_REQUEST = 4000;


    public static final String PARAMETER_SEP = "&";
    public static final String FLAG = "flag";
    public static final String API_KEY = "api_key";
    public static final String TRANS_URL = "https://secure.ccavenue.com/transaction/initTrans";

    public static int socketTimeout = 30000;

    public static final String WEB_VIEW_URL="url";

    //login params
    public static String USER_ID = "user_id";
    public static String LOGIN_FLAG = "USER_LOGIN";
    public static String PASSWORD = "password";
    public static String IP_ADDRESS = "ip_address";
    public static String MAC_ADDRESS = "mac_address";
    public static String USER_AGENT = "user_agent";
    public static String DEVICE_ID = "device_id";
    public static String DEVICE_TYPE = "device_type";

    //register params
    public static String REF_ID = "ref_id";
    public static String USER_NAME = "username";

    public static String REG_FLAG_VALUE = "DEMO_USER_REG";
    public static String DEMO_USER_VALIDATE = "DEMO_USER_VALIDATE";
    public static String TNC = "t_and_c";
    public static String USER_TYPE = "user_type";
    public static String CURRENCY = "currency";
    public static String EMAIL = "email";
    public static String MOBILE_NO = "mobile_no";
    public static String COUNTRY_CODE = "country_codes";
    public static String DOB = "dob";
    public static String GENDER = "gender";
    public static String HINT_QUESTION = "hints_question";
    public static String HINT_ANSWER = "hints_answer";

    //for getting points for events
    public static String GETEVENT_FLAG = "GETEVENT";
    public static String GET_CHOICE_FLAG = "GBIDCHOICEWTHBLOCKED";
    public static String BID_CAT = "bid_cat";
    public static String EVENT_NAME = "event_name";

    //choice activity params
    public static final String CHOICE_INTENT_DATA="choice_points";
    public static final int CHOICE_INTENT_RESULT=2000;

    //bid submit params
    public static final String BIDREG_FLAG="BIDREG";
    public static final String BID_UPDATE_FLAG="BIDUPD";
    public static final String BID_DELETE_FLAG="BIDDEL";
    public static final String MY_BID_FLAG="GBDATA";
    public static final String LAST_THREE_BID_FLAG="LAST3DAYSBID";
    public static final String TODAY_BID_FLAG="TODAYTRANS";
    public static final String BID_INIT_TIME="bid_init_tm";
    public static final String SERIES_ID="series_id";
    public static final String MYPOINTS="mypoints";
    public static final String CATEGORY_NAME="category_name";
    public static final String CHOICE="choice";
    public static final String EVENT="event";
    public static final String BID_COMMENTS="mylucky_comment";

      public static final String BID_ID="bid_id";




    //Points
    public static final String MY_POINTS_FLAG="WR_POINT";

    //Win Report
    public static final String WIN_RECORD="win_records";
    public static final String WIN_REPORT_FLAG="WINRESULT";

    //account report
    public static final String START_DATE="startDate";
    public static final String END_DATE="endDate";
    public static final String US_TIME_DIFF="us_time_diff";
    public static final String REPORT="report";
    public static final String ACCOUNT_REPORT_FLAG="OLDTRANS";

    //change password
    public static final String OLD_PASSWORD="oldpassword";
    public static final String MY_NEW_PASSWORD="mynewpassword";
    public static final String CHANGE_PASSWORD_FLAG="CHANGE_PASS";
    public static final String LOG_OUT_FLAG="USER_LOGOUT";
}

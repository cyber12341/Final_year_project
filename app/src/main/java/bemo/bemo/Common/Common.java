package bemo.bemo.Common;

import android.location.Location;

import bemo.bemo.Model.ActiveOrder;
import bemo.bemo.Model.Users;
import bemo.bemo.Model.getpushid;
import bemo.bemo.Remote.FCMClient;
import bemo.bemo.Remote.IFCMService;
import bemo.bemo.Remote.IGoogleAPI;
import bemo.bemo.Remote.RetrofitClient;

public class Common {

    public static final String driver_tbl = "Drivers";
    public static final String user_driver_tbl = "Users";
    public static final String user_rider_tbl = "Riders";
    public static final String pickup_request_tbl = "PickupRequest";
    public static final String token_tbl = "Tokens";
    public static final String rate_detail_tbl = "RateDetails";
    public static final String active_order = "ActiveOrder";
    public static final String driver_history = "DriverHistory";

    public static final int PICK_IMAGE_REQUEST = 9999;

    public static Users currentuser;
    public static Users user;
    public static getpushid getPushId;
    public static ActiveOrder activeOrder;

    public static Location mLastLocation = null;

    public static double baseFare = 2.55;
    private static double timeRate = 0.35;
    private static double distanceRate = 1.75;

    public static double formulaPrice(double km, double min )
    {
        return (baseFare+(timeRate*min)+(distanceRate*km));
    }

    public static final String baseURL = "https://maps.googleapis.com";
    public static final String fcmURL = "https://fcm.googleapis.com/";
    public static final String user_field = "usr";
    public static final String pwd_field = "pwd";

    public static IGoogleAPI getGoogleAPI()
        {
            return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
        }

    public static IFCMService getFCMService()
        {
            return FCMClient.getClient(fcmURL).create(IFCMService.class);
        }
}

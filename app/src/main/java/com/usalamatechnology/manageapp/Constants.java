package com.usalamatechnology.manageapp;

import android.content.SharedPreferences;

public final class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final int USE_ADDRESS_NAME = 1;
    public static final int USE_ADDRESS_LOCATION = 2;

    public static final String latKey = "latKey";
    public static final String lonKey = "lonKey";
    public static final String addressKey = "address";
    public static final String getSubLocality = "getSubLocality";
    public static final String getLocality = "getLocality";
    public static final String getAdminArea = "getAdminArea";
    public static final String tripStatus = "tripStatus";
    public static final String user_id = "user_id";
    public static final String CREDENTIALSPREFERENCES = "CredentialsPrefs";
    public static SharedPreferences credentialsSharedPreferences;
    public static SharedPreferences.Editor credentialsEditor;


    public static final String PACKAGE_NAME =
            "com.usalamatechnology.manageapp";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME + ".RESULT_ADDRESS";
    public static final String LOCATION_LATITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LATITUDE_DATA_EXTRA";
    public static final String LOCATION_LONGITUDE_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_LONGITUDE_DATA_EXTRA";
    public static final String LOCATION_NAME_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_NAME_DATA_EXTRA";
    public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME + ".FETCH_TYPE_EXTRA";

    public static String category="category";
    public static String amount="amount";
    public static String more="more";
    public static String city="city";
    public static String address="address";
    public static String time="time";
    public static String type="type";

    public static final String name = "name";
    public static final String phone = "phone";
    public static final String email = "email";
    public static final String roles_id = "roles_id";
    public static final String vehicle_reg_no = "vehicle_reg_no";

    public static String currentTripId="currentTripId";



    public static String getManagerHeaderDetails="https://zamzam45.com/tally_driver/getManagerHeaderDetails.php";
    public static String editTrip="https://zamzam45.com/tally_driver/editTrip.php";
    public static String uploadManagerTrip="https://zamzam45.com/tally_driver/uploadManagerTrip.php";
    public static String uploadApproval="https://zamzam45.com/tally_driver/uploadApproval.php";
    public static String getManagerTrips="https://zamzam45.com/tally_driver/getManagerTrips.php";

    public static String getVehicleTrips="https://zamzam45.com/tally_driver/getVehicleTrips.php";
    public static String getVehicleHeaderTrips="https://zamzam45.com/tally_driver/getVehicleHeaderTrips.php";

    public static String getVehicleHeaderExpense="https://zamzam45.com/tally_driver/getVehicleHeaderExpense.php";
    public static String getVehicleExpense="https://zamzam45.com/tally_driver/getVehicleExpense.php";

    public static String getvehicles="https://zamzam45.com/tally_driver/get_vehicles_manager.php";
    public static String login_driver="https://zamzam45.com/tally_driver/login_manager.php";
    public static String uploadExpense="https://zamzam45.com/tally_driver/uploadExpense.php";
    public static String getAllVehicles="https://zamzam45.com/tally_driver/getAllVehicles.php";
    public static String getVehiclesCount="https://zamzam45.com/tally_driver/getAllVehiclesCount.php";
    public static String uploadExpenseImage="https://zamzam45.com/tally_driver/uploadExpenseImage.php";
    public static String imageConstant="https://zamzam45.com/tally_driver/";
    public static String deleteExpense="https://zamzam45.com/tally_driver/deleteExpense.php";
    public static String editExpense="https://zamzam45.com/tally_driver/editExpense.php";


    /*public static String getManagerHeaderDetails="https://zamzam45.com/tally_driver_test/getManagerHeaderDetails.php";
    public static String editTrip="https://zamzam45.com/tally_driver_test/editTrip.php";
    public static String uploadManagerTrip="https://zamzam45.com/tally_driver_test/uploadManagerTrip.php";
    public static String uploadApproval="https://zamzam45.com/tally_driver_test/uploadApproval.php";
    public static String getManagerTrips="https://zamzam45.com/tally_driver_test/getManagerTrips.php";

    public static String getVehicleTrips="https://zamzam45.com/tally_driver_test/getVehicleTrips.php";
    public static String getVehicleHeaderTrips="https://zamzam45.com/tally_driver_test/getVehicleHeaderTrips.php";

    public static String getVehicleHeaderExpense="https://zamzam45.com/tally_driver_test/getVehicleHeaderExpense.php";
    public static String getVehicleExpense="https://zamzam45.com/tally_driver_test/getVehicleExpense.php";

    public static String getvehicles="https://zamzam45.com/tally_driver_test/get_vehicles_manager.php";
    public static String login_driver="https://zamzam45.com/tally_driver_test/login_manager.php";
    public static String uploadExpense="https://zamzam45.com/tally_driver_test/uploadExpense.php";
    public static String getAllVehicles="https://zamzam45.com/tally_driver_test/getAllVehicles.php";
    public static String getVehiclesCount="https://zamzam45.com/tally_driver_test/getAllVehiclesCount.php";
    public static String uploadExpenseImage="https://zamzam45.com/tally_driver_test/uploadExpenseImage.php";
    public static String imageConstant="https://zamzam45.com/tally_driver_test/";
    public static String deleteExpense="https://zamzam45.com/tally_driver_test/deleteExpense.php";
    public static String editExpense="https://zamzam45.com/tally_driver_test/editExpense.php";*/

    /*public static String getManagerHeaderDetails="https://taifa-sacco.com/tally_driver/getManagerHeaderDetails.php";
    public static String editTrip="https://taifa-sacco.com/tally_driver/editTrip.php";
    public static String uploadManagerTrip="https://taifa-sacco.com/tally_driver/uploadManagerTrip.php";
    public static String uploadApproval="https://taifa-sacco.com/tally_driver/uploadApproval.php";
    public static String getManagerTrips="https://taifa-sacco.com/tally_driver/getManagerTrips.php";

    public static String getVehicleTrips="https://taifa-sacco.com/tally_driver/getVehicleTrips.php";
    public static String getVehicleHeaderTrips="https://taifa-sacco.com/tally_driver/getVehicleHeaderTrips.php";
    public static String getVehicleHeaderExpense="https://taifa-sacco.com/tally_driver/getVehicleHeaderExpense.php";
    public static String getVehicleExpense="https://taifa-sacco.com/tally_driver/getVehicleExpense.php";

    public static String getvehicles="https://taifa-sacco.com/tally_driver/get_vehicles_manager.php";
    public static String login_driver="https://taifa-sacco.com/tally_driver/login_manager.php";
    public static String uploadExpense="https://taifa-sacco.com/tally_driver/uploadExpense.php";
    public static String getAllVehicles="https://taifa-sacco.com/tally_driver/getAllVehicles.php";
    public static String getVehiclesCount="https://taifa-sacco.com/tally_driver/getAllVehiclesCount.php";
    public static String uploadExpenseImage="https://taifa-sacco.com/tally_driver/uploadExpenseImage.php";
    public static String imageConstant="https://taifa-sacco.com/tally_driver/";
    public static String deleteExpense="https://taifa-sacco.com/tally_driver/deleteExpense.php";
    public static String editExpense="https://taifa-sacco.com/tally_driver/editExpense.php";*/
}

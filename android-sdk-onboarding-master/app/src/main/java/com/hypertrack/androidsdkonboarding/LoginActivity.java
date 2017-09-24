package com.hypertrack.androidsdkonboarding;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;
import com.hypertrack.lib.models.User;
import com.nexosis.impl.NexosisClient;
import com.nexosis.model.Columns;
import com.nexosis.model.DataRole;
import com.nexosis.model.DataSetData;
import com.nexosis.model.DataSetList;
import com.nexosis.model.DataType;
import com.nexosis.model.ResultInterval;


///Vimanyu///
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.id.message;


public class LoginActivity extends BaseActivity {

    private EditText nameText, phoneNumberText;
    private LinearLayout loginBtnLoader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Check if user is logged in
        if (HyperTrack.isTracking()) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
            return;
        }

        // Initialize Toolbar
        initToolbar(getString(R.string.login_activity_title));

        // Initialize UI Views
        initUIViews();
    }

    /**
     * Call this method to initialize UI views and handle listeners for these
     * views
     */
    private void initUIViews() {
        // Initialize UserName Views
        nameText = (EditText) findViewById(R.id.login_name);

        // Initialize Password Views
        phoneNumberText = (EditText) findViewById(R.id.login_phone_number);

        // Initialize Login Btn Loader
        loginBtnLoader = (LinearLayout) findViewById(R.id.login_btn_loader);
    }

    /**
     * Call this method when User Login button has been clicked.
     * Note that this method is linked with the layout file (content_login.xml)
     * using this button's layout's onClick attribute. So no need to invoke this
     * method or handle login button's click listener explicitly.
     *
     * @param view
     */
    public void onLoginButtonClick(View view) {
        // Check if Location Settings are enabled, if yes then attempt
        // DriverLogin
        try {
            getCitiData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkForLocationSettings();
    }

    /**
     * Call this method to check Location Settings before proceeding for User
     * Login
     */
    private void checkForLocationSettings() {
        // Check for Location permission
        if (!HyperTrack.checkLocationPermission(this)) {
            HyperTrack.requestPermissions(this);
            return;
        }

        // Check for Location settings
        if (!HyperTrack.checkLocationServices(this)) {
            HyperTrack.requestLocationServices(this);
        }

        // Location Permissions and Settings have been enabled
        // Proceed with your app logic here i.e User Login in this case
        attemptUserLogin();
    }

    /**
     * Call this method to attempt user login. This method will create a User
     * on HyperTrack Server and configure the SDK using this generated UserId.
     */
    private void attemptUserLogin() {
        if (TextUtils.isEmpty(phoneNumberText.getText().toString())) {
            Toast.makeText(this, R.string.login_error_msg_invalid_params,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Show Login Button loader
        loginBtnLoader.setVisibility(View.VISIBLE);

        // Get User details, if specified
        final String name = nameText.getText().toString();
        final String phoneNumber = phoneNumberText.getText().toString();
        final String lookupId = phoneNumber;

        /**
         * Get or Create a User for given lookupId on HyperTrack Server here to
         * login your user & configure HyperTrack SDK with this generated
         * HyperTrack UserId.
         * OR
         * Implement your API call for User Login and get back a HyperTrack
         * UserId from your API Server to be configured in the HyperTrack SDK.
         */
        HyperTrack.getOrCreateUser(name, phoneNumber, lookupId,
                new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                User user = (User) successResponse.getResponseObject();
                // Handle createUser success here, if required
                // HyperTrack SDK auto-configures UserId on createUser API call,
                // so no need to call HyperTrack.setUserId() API

                // On UserLogin success
                onUserLoginSuccess();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, R.string.login_error_msg
                        + " " + errorResponse.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Call this method when user has successfully logged in
     */
    private void onUserLoginSuccess() {
        HyperTrack.startTracking(new HyperTrackCallback() {
            @Override
            public void onSuccess(@NonNull SuccessResponse successResponse) {
                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, R.string.login_success_msg,
                        Toast.LENGTH_SHORT).show();

                // Start User Session by starting MainActivity
                Intent mainActivityIntent = new Intent(
                        LoginActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivityIntent);
                finish();
            }

            @Override
            public void onError(@NonNull ErrorResponse errorResponse) {
                // Hide Login Button loader
                loginBtnLoader.setVisibility(View.GONE);

                Toast.makeText(LoginActivity.this, R.string.login_error_msg
                        + " " + errorResponse.getErrorMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handle on Grant Location Permissions request accepted/denied result
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,
                grantResults);

        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {
                // Check if Location Settings are enabled to proceed
                checkForLocationSettings();

            } else {
                // Handle Location Permission denied error
                Toast.makeText(this, "Location Permission denied.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Handle on Enable Location Services request accepted/denied result
     * @param requestCode
     * @param resultCode
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HyperTrack.REQUEST_CODE_LOCATION_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                // Check if Location Settings are enabled to proceed
                checkForLocationSettings();

            } else {
                // Handle Enable Location Services request denied error
                Toast.makeText(this, R.string.enable_location_settings,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Map<String, String> jsonToMap(JSONObject json) throws JSONException {
        Map<String, String> retMap = new HashMap<String, String>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, String> toMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value.toString());
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public void CitiAuth()throws Exception {

        CloseableHttpClient httpClient = null;

        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
            //proxy configuration
            //HttpHost proxy = new HttpHost("proxy.xxxx.com", 8080, "http");
            //RequestConfig config = RequestConfig.custom().setProxy(proxy).build();

            HttpGet request = new HttpGet("https://sandbox.apihub.citi.com/gcb/api/v2/accounts");

            //proxy configuration
            //request.setConfig(config);
            // Header parameters  addition
            request.addHeader("Authorization", "Bearer AAEkODMwN2RhYjctZjQxYS00MWU0LWI0ZGMtZWQ5NDk1NzgwNzQ4X_ui19PhL8VuKmwmplYaIzH0oaXHCytm2CdbccBHVVq1N5L0G98gUhQNngj4BpXnWg8WSwMG8kLt12kAM-xT4rQVCBnsqogIzGiMRuqt6XLUF7L19_0aCOru25gu-9zgW6omiGXEOXLHmqGmSCa7P38uhwQXL70YbjijKpZV4cprhUqU-Hu53mQt3ThOAVpjCK3VQ4S5_irwDBNemYx03g");
            request.addHeader("Accept", "application/json");
            request.addHeader("client_id", "8307dab7-f41a-41e4-b4dc-ed9495780748");
         //   request.addHeader("uuid","4c4a2900-71f5-4209-bd44-dc74d1f3890f");

            request.addHeader("uuid","d0c7da2a-7ade-4c32-806f-f5bff5ccff09");

         //   System.out.println("Executing request " + request.getRequestLine());
            response = httpClient.execute(request);
        //    System.out.println("----------------------------------------");
         //   System.out.println(response.getStatusLine());

         //   System.out.println(EntityUtils.toString(response.getEntity()));
        }
        catch(Exception e){
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }



        public static void getCitiData() throws Exception {
            final OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url("https://sandbox.apihub.citi.com/gcb/api/v2/accounts").addHeader("Authorization", "Bearer AAEkODMwN2RhYjctZjQxYS00MWU0LWI0ZGMtZWQ5NDk1NzgwNzQ4vk0jB0JFHoUUZ0ANjoAkXO_dbJWsev-rx0j7vtVKvnucu_Jl19Je9YhOxtnxTjfTuPKR44Re_V7ee8tvbGk9fRDirhXDPqLPTMSCk44dPPlajC5EcwawSE6gJDGlYy1lYzBsU3Zy7gij48QMulZQ3jZQ59VzonGch5pHy_wlc2b1RZ-DCQjtJ7g1p4tu_Nemeykj7JKGDACoipVeYB-FOg").addHeader("Accept", "application/json").addHeader("client_id", "8307dab7-f41a-41e4-b4dc-ed9495780748").addHeader("uuid","d0c7da2a-7ade-4c32-806f-f5bff5ccff09").build();

            //proxy configuration
            //request.setConfig(config);
            // Header parameters  addition
        //    request.addHeader("Authorization", "Bearer AAEkODMwN2RhYjctZjQxYS00MWU0LWI0ZGMtZWQ5NDk1NzgwNzQ4Iu84h6A-eXyyGKgGvvUAvC5HDlV7ue_tcIXd_Z5x6HjUCofZLMRhgXmnSYi9tH7w8zeZzGsFxe_6uwyNPc2TACk9lbesHcOXDbox91GoXkV0YihcJaXe9vTHXN7yyjPiGlxo86a02hRWBZtDjoQ0FjSomZdwEwgZkex8BN2kgrb8OyMjs50hiHHIGm4MgAw0lo5oiscoop9EN2vO4RF_-Q");
          //  request.addHeader("Accept", "application/json");
            //request.addHeader("client_id", "8307dab7-f41a-41e4-b4dc-ed9495780748");
            //   request.addHeader("uuid","4c4a2900-71f5-4209-bd44-dc74d1f3890f");

          //  request.addHeader("uuid","d0c7da2a-7ade-4c32-806f-f5bff5ccff09");

            final String[] resp = {""};

            Log.d("wadded", "Begining");

            final JSONObject[] Jobject = new JSONObject[1];
            final JSONArray[] Jarray = {null};
            final Boolean[] flag = {false};
            final String[] accountId = {""};
            client.newCall(request).enqueue( new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                        resp[0] =response.body().string();
                 //   System.out.print("***************************");
                    Log.d("wadded", "*********************************");
                    Log.d("wadded", resp[0]);
                    try {
                     //   Jobject[0] = new JSONObject(resp[0]);
                    //    Jarray[0] = Jobject[0].getJSONArray("accountGroupSummary");

                        JSONObject Jobject = new JSONObject(resp[0]);
                        JSONArray Jarray = Jobject.getJSONArray("accountGroupSummary");

                        JSONObject object     = Jarray.getJSONObject(0);
                        JSONArray Jarray1 = object.getJSONArray("creditCardAccountsSummary");
                        JSONObject object1     = Jarray1.getJSONObject(0);



                        Log.d("wadded", "*****************JSONOBJ****************");
                        accountId[0] = object1.get("accountId").toString();
                        Log.d("wadded", accountId[0]);
                        flag[0] =true;


                        } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }

            }
            );


            while(flag[0]==false){
                //do nothing
            }
            Log.d("TEST",accountId[0]);

            final Request request_transaction = new Request.Builder()
                    .url("https://sandbox.apihub.citi.com/gcb/api/v2/accounts/"+accountId[0]+"/transactions?transactionStatus=ALL&requestSize=1000&transactionFromDate=2017-01-01&transactionToDate=2017-03-01").addHeader("Authorization", "Bearer AAEkODMwN2RhYjctZjQxYS00MWU0LWI0ZGMtZWQ5NDk1NzgwNzQ4vk0jB0JFHoUUZ0ANjoAkXO_dbJWsev-rx0j7vtVKvnucu_Jl19Je9YhOxtnxTjfTuPKR44Re_V7ee8tvbGk9fRDirhXDPqLPTMSCk44dPPlajC5EcwawSE6gJDGlYy1lYzBsU3Zy7gij48QMulZQ3jZQ59VzonGch5pHy_wlc2b1RZ-DCQjtJ7g1p4tu_Nemeykj7JKGDACoipVeYB-FOg").addHeader("Accept", "application/json").addHeader("client_id", "8307dab7-f41a-41e4-b4dc-ed9495780748").addHeader("uuid","d0c7da2a-7ade-4c32-806f-f5bff5ccff09").build();

            //HttpGet request = new HttpGet("https://sandbox.apihub.citi.com/gcb/api/v2/accounts/"+accountId+"/transactions?transactionStatus=ALL&requestSize=1000&transactionFromDate=2017-01-01&transactionToDate=2017-03-01");

            //proxy configuration
            //request.setConfig(config);
            // Header parameters  addition
//        	request.addHeader("Authorization", "Bearer {replace with access_token}");
//        	request.addHeader("uuid", "f97c7685-9562-480c-a6d1-6919f941863b");
//        	request.addHeader("Accept", "application/json");
//        	request.addHeader("client_id", "8307dab7-f41a-41e4-b4dc-ed9495780748");


            final JSONObject[] Jobject_transaction = {null};
            final Boolean[] flag_transaction = {false};
            client.newCall(request_transaction).enqueue( new Callback() {
                                                 @Override
                                                 public void onFailure(Call call, IOException e) {

                                                 }

                                                 @Override
                                                 public void onResponse(Call call, Response response) throws IOException {
                                                     resp[0] =response.body().string();


                                                     try {
                                                         Jobject_transaction[0] = new JSONObject(resp[0]);
                                                         flag_transaction[0]=true;
                                                     } catch (JSONException e) {
                                                         e.printStackTrace();
                                                     }


                                                     //   System.out.print("***************************");
                                                     Log.d("wadded", "**************TRANSACTION_DATA*******************");
                                                     Log.d("wadded", resp[0]);
//                                                     try {
//                                                         //   Jobject[0] = new JSONObject(resp[0]);
//                                                         //    Jarray[0] = Jobject[0].getJSONArray("accountGroupSummary");
//
//                                                         JSONObject Jobject = new JSONObject(resp[0]);
//                                                         JSONArray Jarray = Jobject.getJSONArray("accountGroupSummary");
//
//                                                         JSONObject object     = Jarray.getJSONObject(0);
//                                                         JSONArray Jarray1 = object.getJSONArray("creditCardAccountsSummary");
//                                                         JSONObject object1     = Jarray1.getJSONObject(0);
//
//
//
//                                                         Log.d("wadded", "*****************JSONOBJ****************");
//                                                         accountId[0] = object1.get("accountId").toString();
//                                                         Log.d("wadded", accountId[0]);
//                                                         flag[0] =true;
//
//
//                                                     } catch (JSONException e1) {
//                                                         e1.printStackTrace();
//                                                     }

                                                 }

                                             }
            );

            while(flag_transaction[0]==false){
                //do nothing

            }


            JSONArray transactionList = (JSONArray) Jobject_transaction[0].get("creditCardAccountTransactions");


            JSONArray foodData = new JSONArray();
            HashMap<String,Integer> transactionDateMap = new HashMap();
            int counter = 0;
//		int accountID = 1;
            for(int i = 0; i< transactionList.length(); i++) {
                if ((transactionList.getJSONObject(i).get("merchantDescription")).toString().toLowerCase().contains("food")
                        || (transactionList.getJSONObject(i).get("merchantDescription")).toString().toLowerCase().contains("restaurant")
                        || (transactionList.getJSONObject(i).get("merchantDescription")).toString().toLowerCase().contains("eating")
                        ) {
                    if (transactionDateMap.containsKey(transactionList.getJSONObject(i).get("transactionDate").toString())) {
                        //    Iterator<JSONObject> iter2 = foodData.iterator();
                        int transaction_index = transactionDateMap.get(transactionList.getJSONObject(i).get("transactionDate").toString());
                        Log.d("Transaction INDEX", Integer.toString(transaction_index));
                        Log.d("TARGET_JSONOBJ", foodData.getJSONObject(transaction_index).toString());
                        double old_value = Double.parseDouble(foodData.getJSONObject(transaction_index).get("food").toString());
                        Log.d("OLD_VALUE", Double.toString(old_value));
                        double new_value = Double.parseDouble(transactionList.getJSONObject(i).get("transactionAmount").toString());
                        Log.d("NEW_VALUE", Double.toString(new_value));

                        foodData.getJSONObject(transaction_index).put("food", old_value + new_value);

                        foodData.getJSONObject(transaction_index).put("balance", transactionList.getJSONObject(i).get("runningBalanceAmount"));


//                        foodData.getJSONObject(transactionDateMap.get(transactionList.getJSONObject(i).get("transactionDate").toString())).put(foodData.getJSONObject(transactionDateMap.get(transactionList.getJSONObject(i))).get("food").toString(),Integer.parseInt(foodData.getJSONObject(transactionDateMap.get(transactionList.getJSONObject(i))).get("food").toString())
//                                +Integer.parseInt(transactionList.getJSONObject(i).get("transactionAmount").toString()));

                        //  iter2.get("food").put(iter2.get("food")+iter.get("transactionAmount"));
                        //s   iter2.get("balance").put(iter.get("runningBalanceAmount"));

                    } else {
                        JSONObject entry = new JSONObject();
                        entry.put("timeStamp", transactionList.getJSONObject(i).get("transactionDate"));
                        transactionDateMap.put(transactionList.getJSONObject(i).get("transactionDate").toString(), counter++);
                        entry.put("food", transactionList.getJSONObject(i).get("transactionAmount"));
                        entry.put("balance", transactionList.getJSONObject(i).get("runningBalanceAmount"));


                        foodData.put(entry);
                    }

                }
            }

            JSONObject customerWiseFoodData = new JSONObject();
            customerWiseFoodData.put("customerID",Integer.parseInt(accountId[0]));
            customerWiseFoodData.put("foodData",foodData);

            Log.d("wadded", "**************FOOD_DATA*******************");
            Log.d("wadded", customerWiseFoodData.toString());


//            NexosisClient nexClient = new NexosisClient("fd9e02a6c5a94e56b9a9f56b520956fc","https://ml.nexosis.com/v1/");
//
//            List<Map<String,String>> foodData_list = new ArrayList();
//
//            if(foodData != null){
//                for (int i=0;i<foodData.length();i++){
//                    Map<String,String> jMap = jsonToMap(foodData.getJSONObject(i));
//                    foodData_list.add(jMap);
//                }
//            }
//
//            DataSetData dataSet = new DataSetData();
//            dataSet.setData(foodData_list);
//
//            Columns cols = new Columns();
//            cols.setColumnMetadata("timeStamp", DataType.DATE, DataRole.TIMESTAMP);
//            cols.setColumnMetadata("food", DataType.NUMERIC, DataRole.TARGET);
//            cols.setColumnMetadata("balance", DataType.NUMERIC, DataRole.FEATURE);
//            dataSet.setColumns(cols);
//
            String dataSetName = "CitiDataSet";
//
//            nexClient.getDataSets().create(dataSetName, dataSet);
//
//            DataSetList list = nexClient.getDataSets().list();
//
//            nexClient.getSessions().createForecast(
//                    "OtherDataSet",
//                    "food",
//                    DateTime.parse("2017-03-25T0:00:00Z"),
//                    DateTime.parse("2017-04-24T0:00:00Z"),
//                    ResultInterval.DAY
//            );

            JSONObject jsonBody = new JSONObject();
            JSONObject timestampJSON = new JSONObject();

            timestampJSON.put("dataType","date");
            timestampJSON.put("role","timestamp");

            JSONObject salesJSON = new JSONObject();
            salesJSON.put("dataType","numeric");
            salesJSON.put("role","target");

            JSONObject balanceJSON = new JSONObject();
            balanceJSON.put("dataType","numeric");
            balanceJSON.put("role","none");

            JSONObject dataJSON  = new JSONObject();
            dataJSON.put("data", foodData);

            JSONObject columnJSON = new JSONObject();

            columnJSON.put("timeStamp",timestampJSON);
            columnJSON.put("food",salesJSON);
            columnJSON.put("balance",balanceJSON);
            jsonBody.put("column",columnJSON);
            jsonBody.put("data",dataJSON);

//            jsonBody.put("column","timestamp");
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, jsonBody.toString());

            final OkHttpClient nexosisClient = new OkHttpClient();
            Request request2Nexosis = new Request.Builder()
                                        .url("https://ml.nexosis.com/v1/data/dataSetName")
                                        .put(body)
                                        .addHeader("api-key","fd9e02a6c5a94e56b9a9f56b520956fc")
                                        .build();
            





        }





        }





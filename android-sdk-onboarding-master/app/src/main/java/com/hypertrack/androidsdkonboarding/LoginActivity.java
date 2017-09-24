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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
            request.addHeader("Authorization", "Bearer AAEkODMwN2RhYjctZjQxYS00MWU0LWI0ZGMtZWQ5NDk1NzgwNzQ4Vy96H0EAYtJSb9z8-xbxPs70NxFyp0UpVoh7mhstt47mKXqXRlRt0uUKpJb3hryMdBvCS25QXFnymZ90t-oswm-8fDppZqr_wSOmaump1eYeFCeNErfD1hSAfvc5qR7MxmFkd6HmmoJTHugZ7sbm4tFg4OHG8PIrA97gss4zD5xX4YaalKUJU1nAJN7m7sl-4H5VDEX9wyYlPLAL7IVvhg");
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
                    .url("https://sandbox.apihub.citi.com/gcb/api/v2/accounts").addHeader("Authorization", "Bearer AAEkODMwN2RhYjctZjQxYS00MWU0LWI0ZGMtZWQ5NDk1NzgwNzQ4McgVkrSeT25MBEu9mlD63bF-D56jHFnf8aZ14t51CQoU2-Epjl-Pq9-QFz_FAvGuDMdEuQyI99J4lMQEgri0kiUj1yBp6hDHao36Z2NlQRucdIrM6Q6Pj88F8uLY87EFJmbOy-Ut5o0vtoMdqO-2giHg3h8FLl0T3OkRZS3PiCneZU_IXgqIDq24PbFyifqMhN0cVRGylN2t8Lec0x57RQ").addHeader("Accept", "application/json").addHeader("client_id", "8307dab7-f41a-41e4-b4dc-ed9495780748").addHeader("uuid","d0c7da2a-7ade-4c32-806f-f5bff5ccff09").build();

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
                    .url("https://sandbox.apihub.citi.com/gcb/api/v2/accounts/"+accountId[0]+"/transactions?transactionStatus=ALL&requestSize=1000&transactionFromDate=2017-01-01&transactionToDate=2017-03-01").addHeader("Authorization", "Bearer AAEkODMwN2RhYjctZjQxYS00MWU0LWI0ZGMtZWQ5NDk1NzgwNzQ4McgVkrSeT25MBEu9mlD63bF-D56jHFnf8aZ14t51CQoU2-Epjl-Pq9-QFz_FAvGuDMdEuQyI99J4lMQEgri0kiUj1yBp6hDHao36Z2NlQRucdIrM6Q6Pj88F8uLY87EFJmbOy-Ut5o0vtoMdqO-2giHg3h8FLl0T3OkRZS3PiCneZU_IXgqIDq24PbFyifqMhN0cVRGylN2t8Lec0x57RQ").addHeader("Accept", "application/json").addHeader("client_id", "8307dab7-f41a-41e4-b4dc-ed9495780748").addHeader("uuid","d0c7da2a-7ade-4c32-806f-f5bff5ccff09").build();

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
            for(int i = 0; i<= transactionList.length(); i++){
                if ((transactionList.getJSONObject(i).get("merchantDescription")).toString().toLowerCase().contains("food")
                        || (transactionList.getJSONObject(i).get("merchantDescription")).toString().toLowerCase().contains("restaurant")
                        || (transactionList.getJSONObject(i).get("merchantDescription")).toString().toLowerCase().contains("eating")
                        ) {
                    if(transactionDateMap.containsKey(transactionList.getJSONObject(i).get("transactionDate").toString())) {
                    //    Iterator<JSONObject> iter2 = foodData.iterator();
//                        int transaction_index = transactionDateMap.get(transactionList.getJSONObject(i));
                        foodData.getJSONObject(transactionDateMap.get(transactionList.getJSONObject(i).get("transactionDate").toString())).put(foodData.getJSONObject(transactionDateMap.get(transactionList.getJSONObject(i))).get("food").toString(),Integer.parseInt(foodData.getJSONObject(transactionDateMap.get(transactionList.getJSONObject(i))).get("food").toString())
                                +Integer.parseInt(transactionList.getJSONObject(i).get("transactionAmount").toString()));

                      //  iter2.get("food").put(iter2.get("food")+iter.get("transactionAmount"));
                     //s   iter2.get("balance").put(iter.get("runningBalanceAmount"));

                    }
                    else {
                        JSONObject entry = new JSONObject();
                        entry.put("timeStamp",transactionList.getJSONObject(i).get("transactionDate"));
                        transactionDateMap.put(transactionList.getJSONObject(i).get("transactionDate").toString(), counter++);
                        entry.put("food","transactionAmount");
                        entry.put("balance","runningBalanceAmount");


                        foodData.put(entry);
                    }

                }
            }


            JSONObject customerWiseFoodData = new JSONObject();
            customerWiseFoodData.put("customerID",Integer.parseInt(accountId[0]));
            customerWiseFoodData.put("foodData",foodData);

            Log.d("wadded", "**************FOOD_DATA*******************");
            Log.d("wadded", customerWiseFoodData.toString());






             }





        }





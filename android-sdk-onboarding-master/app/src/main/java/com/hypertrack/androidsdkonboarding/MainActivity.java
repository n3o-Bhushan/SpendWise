package com.hypertrack.androidsdkonboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.callbacks.HyperTrackCallback;
import com.hypertrack.lib.models.ErrorResponse;
import com.hypertrack.lib.models.SuccessResponse;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Toolbar
        initToolbar(getString(R.string.app_name));

        // Initialize UI Views
        initUIViews();
    }

    private void initUIViews() {
        Log.d("LOCATION","inside main");

        Log.d("LOCATION", HyperTrack.getUserId());



         HyperTrack.getCurrentLocation(new HyperTrackCallback() {
             @Override
             public void onSuccess(@NonNull SuccessResponse successResponse) {
                 Log.d("LOCATION", successResponse.toString());
             }

             @Override
             public void onError(@NonNull ErrorResponse errorResponse) {

                 Log.d("LOCATION", errorResponse.toString());

             }
         });




        // Initialize AssignAction Button
        Button logoutButton = (Button) findViewById(R.id.logout_btn);
        if (logoutButton != null)
            logoutButton.setOnClickListener(logoutButtonClickListener);
    }

    // Click Listener for AssignAction Button
    private View.OnClickListener logoutButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, R.string.main_logout_success_msg,
                    Toast.LENGTH_SHORT).show();

            // Stop HyperTrack SDK
            HyperTrack.stopTracking();

            // Proceed to LoginActivity for a fresh User Login
            Intent loginIntent = new Intent(MainActivity.this,
                    LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
    };
}

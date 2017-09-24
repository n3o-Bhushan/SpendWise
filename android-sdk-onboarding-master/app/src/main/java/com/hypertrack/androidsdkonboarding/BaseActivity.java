package com.hypertrack.androidsdkonboarding;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by piyush on 08/05/17.
 */

public class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public void initToolbar(String title) {
        initToolbar(title, false);
    }

    public void initToolbar(String title, boolean homeButtonEnabled) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null)
            return;

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() == null)
            return;

        getSupportActionBar().setDisplayHomeAsUpEnabled(homeButtonEnabled);
        getSupportActionBar().setHomeButtonEnabled(homeButtonEnabled);
    }
}

package com.letssolvetogether.omr.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.instacart.library.truetime.TrueTime;
import com.letssolvetogether.omr.main.R;
import com.letssolvetogether.omr.truetime.InitTrueTimeAsyncTask;
import com.letssolvetogether.omr.omrkey.ui.OMRKeyActivity;
import com.letssolvetogether.omr.settings.SettingsActivity;
import com.letssolvetogether.omr.camera.ui.CameraActivity;

public class HomeActivity extends AppCompatActivity{

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.scan_omr:
                    Intent cameraIntent = new Intent(HomeActivity.this, CameraActivity.class);
                    startActivity(cameraIntent);
                    return true;
                case R.id.navigation_more:
                    Intent settingsIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                    settingsIntent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS,true);
                    settingsIntent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
                    startActivity(settingsIntent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(!TrueTime.isInitialized()){
            new InitTrueTimeAsyncTask(HomeActivity.this).execute();
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void displayAnswerKey(View view){
        Intent omrKeyActivity = new Intent(this, OMRKeyActivity.class);
        startActivity(omrKeyActivity);
    }
}
package com.letssolvetogether.omr.home.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.instacart.library.truetime.TrueTime;
import com.letssolvetogether.omr.main.R;
import com.letssolvetogether.omr.truetime.InitTrueTimeAsyncTask;
import com.letssolvetogether.omr.omrkey.ui.OMRKeyActivity;
import com.letssolvetogether.omr.settings.SettingsActivity;
import com.letssolvetogether.omr.camera.ui.CameraActivity;

public class HomeActivity extends AppCompatActivity{

    private int noOfAnswers;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.scan_omr:
                    Intent cameraIntent = new Intent(HomeActivity.this, CameraActivity.class);
                    cameraIntent.putExtra("noOfAnswers", noOfAnswers);
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

    private RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.radio_omrkey_20:
                    noOfAnswers = 20;
                    break;
                case R.id.radio_omrkey_100:
                    noOfAnswers = 100;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RadioGroup radioGroupOMRTypes = findViewById(R.id.radiogroup_omrtypes);
        radioGroupOMRTypes.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        displayValidityPeriodDialog();
    }

    public void displayAnswerKey(View view){
        Intent omrKeyActivity = new Intent(this, OMRKeyActivity.class);
        omrKeyActivity.putExtra("noOfAnswers", noOfAnswers);
        startActivity(omrKeyActivity);
    }

    public void displayValidityPeriodDialog(){
        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
            // Save the state
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();

            //Display Dialog
            AlertDialog.Builder dialogTips = new AlertDialog.Builder(HomeActivity.this);
            dialogTips.setTitle("Note:");
            dialogTips.setMessage("You can use this app for free until December 31, 2020.");
            dialogTips.setNeutralButton("Ok",null);
            dialogTips.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(!TrueTime.isInitialized()){
                        new InitTrueTimeAsyncTask(HomeActivity.this).execute();
                    }
                }
            });
            dialogTips.show();
        }else {
            if (!TrueTime.isInitialized()) {
                new InitTrueTimeAsyncTask(HomeActivity.this).execute();
            }
        }
    }
}

package com.aliworld.music.others;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.aliworld.music.R;

/**
 * Created by MujeebulHasan on 23-09-2015.
 */
public class SettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.ColorPrimaryDark));
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar1);
        setSupportActionBar(mToolbar);
        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
package com.ptmprojects.aplikacjaegzaminatora;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends SingleFragmentActivity {
    public static final String TAG = "MainActivity";


    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
}
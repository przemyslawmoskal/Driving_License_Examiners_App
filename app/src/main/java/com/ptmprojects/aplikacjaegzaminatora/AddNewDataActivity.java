package com.ptmprojects.aplikacjaegzaminatora;

import android.support.v4.app.Fragment;

public class AddNewDataActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AddNewDataFragment();
    }
}

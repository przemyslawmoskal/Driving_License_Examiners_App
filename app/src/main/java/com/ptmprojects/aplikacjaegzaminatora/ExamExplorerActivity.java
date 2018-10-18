package com.ptmprojects.aplikacjaegzaminatora;

import android.support.v4.app.Fragment;

public class ExamExplorerActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ExamExplorerChooserFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_twopane;
    }
}

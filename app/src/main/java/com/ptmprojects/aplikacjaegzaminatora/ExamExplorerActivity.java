package com.ptmprojects.aplikacjaegzaminatora;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class ExamExplorerActivity extends SingleFragmentActivity implements ExamExplorerChooserFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new ExamExplorerChooserFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_twopane;
    }


    @Override
    public void onShowExamsButtonClicked() {
         Fragment newExamsToShow = new ExamExplorerListFragment();
         getSupportFragmentManager().beginTransaction()
                 .replace(R.id.detail_fragment_container, newExamsToShow)
                 .commit();
    }
}

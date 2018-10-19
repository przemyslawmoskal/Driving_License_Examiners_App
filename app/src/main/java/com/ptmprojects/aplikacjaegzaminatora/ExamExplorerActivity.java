package com.ptmprojects.aplikacjaegzaminatora;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Calendar;

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
    public void onShowExamsButtonClicked(Integer periodOfTime, Calendar optionalStartDate, Calendar optionalEndDate) {
         Fragment newExamsToShow = ExamExplorerListFragment.newInstance(periodOfTime, optionalStartDate, optionalEndDate);
         getSupportFragmentManager().beginTransaction()
                 .replace(R.id.detail_fragment_container, newExamsToShow)
                 .commit();
    }
}

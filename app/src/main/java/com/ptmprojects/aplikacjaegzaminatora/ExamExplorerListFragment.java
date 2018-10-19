package com.ptmprojects.aplikacjaegzaminatora;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

public class ExamExplorerListFragment extends Fragment{
    public static final String ARG_TYPE_OF_CHOSEN_PERIOD_OF_TIME = "ArgTypeOfChosenPeriodOfTime";
    public static final String ARG_CHOSEN_START_DATE = "ArgStartDateChosenByUser";
    public static final String ARG_CHOSEN_END_DATE = "ArgEndDateChosenByUser";
    public static final String EXTRA_TYPE_OF_CHOSEN_PERIOD_OF_TIME = "ExtraTypeOfChosenPeriodOfTime";
    public static final String EXTRA_CHOSEN_START_DATE = "ExtraStartDateChosenByUser";
    public static final String EXTRA_CHOSEN_END_DATE = "ExtraEndDateChosenByUser";

    public static ExamExplorerListFragment newInstance(Integer periodOfTime, Calendar optionalStartDate, Calendar optionalEndDate) {
        Bundle args = new Bundle();
        if (periodOfTime > 0) {
            args.putInt(ARG_TYPE_OF_CHOSEN_PERIOD_OF_TIME, periodOfTime);
        } else {
            args.putSerializable(ARG_CHOSEN_START_DATE, optionalStartDate);
            args.putSerializable(ARG_CHOSEN_END_DATE, optionalEndDate);
        }
        ExamExplorerListFragment fragment = new ExamExplorerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam_explorer_list, container, false);
        return v;
    }
}

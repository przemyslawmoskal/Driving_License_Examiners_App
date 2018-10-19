package com.ptmprojects.aplikacjaegzaminatora;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ExamExplorerChooserFragment extends Fragment {
    private static final String DEFAULT_CHOOSE_DATE_RANGE_SPINNER_VALUE = "Dzisiaj";
    private Spinner mChooseDateRangeSpinner;
    private Button mSearchButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam_explorer_chooser, container, false);

        mChooseDateRangeSpinner = v.findViewById(R.id.date_range_chooser_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_scopes, android.R.layout.simple_spinner_item);
        int spinnerDefaultPosition = adapter.getPosition(DEFAULT_CHOOSE_DATE_RANGE_SPINNER_VALUE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseDateRangeSpinner.setAdapter(adapter);
        mChooseDateRangeSpinner.setSelection(spinnerDefaultPosition);

        mSearchButton = (Button) v.findViewById(R.id.showExamsButton);
        mSearchButton.setOnClickListener((view) -> {
            Fragment fragment = new ExamExplorerListFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.detail_fragment_container, fragment)
                    .commit();
        });

        return v;
    }
}

package com.ptmprojects.aplikacjaegzaminatora;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ExamExplorerChooserFragment extends Fragment {
    private static final String DEFAULT_CHOOSE_DATE_RANGE_SPINNER_VALUE = "Dzisiaj";
    private Callbacks mCallbacks;
    private Spinner mChooseDateRangeSpinner;
    private Button mShowExamsButton;

    //Interface needed for hosting Activities:
    public interface Callbacks {
        void onShowExamsButtonClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

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

        mShowExamsButton = (Button) v.findViewById(R.id.showExamsButton);
        mShowExamsButton.setOnClickListener((view) -> {
            // Querying db here or in the Callbacks.onShowExamsbuttonClicked() ?
            mCallbacks.onShowExamsButtonClicked();
        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}

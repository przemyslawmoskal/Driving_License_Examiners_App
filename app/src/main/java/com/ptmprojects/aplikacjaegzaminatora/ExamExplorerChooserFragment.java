package com.ptmprojects.aplikacjaegzaminatora;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExamExplorerChooserFragment extends Fragment {
    private static final String DEFAULT_CHOOSE_DATE_RANGE_SPINNER_VALUE = "Dzisiaj";
    private Callbacks mCallbacks;
    private Spinner mChooseDateRangeSpinner;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private Button mShowExamsButton;
    private int mTagFromSpinner = -1;
    private Calendar mStartDate;
    private Calendar mEndDate;



    //Interface needed for hosting Activities:
    public interface Callbacks {
        void onShowExamsButtonClicked(Integer periodOfTime, Calendar optionalStartDate, Calendar optionalEndDate);
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

//        mChooseDateRangeSpinner = v.findViewById(R.id.date_range_chooser_spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.time_scopes, android.R.layout.simple_spinner_item);
//        int spinnerDefaultPosition = adapter.getPosition(DEFAULT_CHOOSE_DATE_RANGE_SPINNER_VALUE);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mChooseDateRangeSpinner.setAdapter(adapter);
//        mChooseDateRangeSpinner.setSelection(spinnerDefaultPosition);

        Calendar today = Calendar.getInstance();
        mStartDate = today;
        mStartDateButton = (Button) v.findViewById(R.id.start_date_button);
        mStartDateButton.setText(ResultsBank.sDateFormat.format(today.getTime()));
        mEndDateButton = (Button) v.findViewById(R.id.end_date_button);
        mEndDate = today;
        mEndDateButton.setText(ResultsBank.sDateFormat.format(today.getTime()));

        mChooseDateRangeSpinner = v.findViewById(R.id.date_range_chooser_spinner);
        List<StringWithCorrespondingTag> listOfSpinnerItems = new ArrayList<StringWithCorrespondingTag>();
        listOfSpinnerItems.add(new StringWithCorrespondingTag(getString(R.string.today), ResultsBank.TODAY_EXAMS));
        listOfSpinnerItems.add(new StringWithCorrespondingTag(getString(R.string.this_week), ResultsBank.THIS_WEEK_EXAMS));
        listOfSpinnerItems.add(new StringWithCorrespondingTag(getString(R.string.this_month), ResultsBank.THIS_MONTH_EXAMS));
        listOfSpinnerItems.add(new StringWithCorrespondingTag(getString(R.string.this_year), ResultsBank.THIS_YEAR_EXAMS));
        listOfSpinnerItems.add(new StringWithCorrespondingTag(getString(R.string.all_time_exams), ResultsBank.ALL_TIME_EXAMS));
        listOfSpinnerItems.add(new StringWithCorrespondingTag(getString(R.string.choose_dates_range), ResultsBank.USER_DEFINED_PERIOD_OF_TIME_EXAMS));
        ArrayAdapter<StringWithCorrespondingTag> adapter = new ArrayAdapter<StringWithCorrespondingTag>(getActivity(),
        android.R.layout.simple_spinner_item, listOfSpinnerItems);
        StringWithCorrespondingTag defaultItem = listOfSpinnerItems.get(0);
        int spinnerDefaultPosition = adapter.getPosition(defaultItem);
        mTagFromSpinner = defaultItem.getIdPart();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseDateRangeSpinner.setAdapter(adapter);
        mChooseDateRangeSpinner.setSelection(spinnerDefaultPosition);
        mChooseDateRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StringWithCorrespondingTag newlySelectedItem = (StringWithCorrespondingTag) parent.getItemAtPosition(position);
                mTagFromSpinner = newlySelectedItem.getIdPart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mShowExamsButton = (Button) v.findViewById(R.id.showExamsButton);
        mShowExamsButton.setOnClickListener((view) -> {
            // Querying db here or in the Callbacks.onShowExamsbuttonClicked() ?

            //
            //

            if(mTagFromSpinner > 0) {
                mCallbacks.onShowExamsButtonClicked(mTagFromSpinner, null, null);
            } else {
                mCallbacks.onShowExamsButtonClicked(mTagFromSpinner, mStartDate, mEndDate);
            }

        });

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}

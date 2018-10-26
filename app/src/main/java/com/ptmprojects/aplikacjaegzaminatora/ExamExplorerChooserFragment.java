package com.ptmprojects.aplikacjaegzaminatora;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExamExplorerChooserFragment extends Fragment implements View.OnClickListener{
    private Callbacks mCallbacks;
    private Spinner mChooseDateRangeSpinner;
    private Button mStartDateButton;
    private Button mEndDateButton;
    private Button mShowExamsButton;
    private int mTagFromSpinner = -1;
    private Calendar mStartDate;
    private Calendar mEndDate;

    @Override
    public void onClick(View v) {
        int currentYear, currentMonth, currentDayOfMonth;
        Calendar c;
        DatePickerDialog datePickerDialog;
        switch(v.getId()) {
            case R.id.start_date_button:
                // Get Current Date
                c = Calendar.getInstance();
                currentYear = c.get(Calendar.YEAR);
                currentMonth = c.get(Calendar.MONTH);
                currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);


                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            Calendar chosenStartDateFromDatePicker = Calendar.getInstance();
                            chosenStartDateFromDatePicker.set(year, monthOfYear, dayOfMonth);
                            Calendar today = Calendar.getInstance();

                            int chosenStartDateComparedToToday = chosenStartDateFromDatePicker.compareTo(today);
                            int chosenStartDateComparedToChosenEndDate = chosenStartDateFromDatePicker.compareTo(mEndDate);
                            if (chosenStartDateComparedToToday > 0 || chosenStartDateComparedToChosenEndDate > 0) {
                                Toast.makeText(getActivity(), R.string.chosen_date_is_too_late, Toast.LENGTH_SHORT).show();
                                mStartDate = today;
                                mStartDateButton.setText(getString(R.string.choose_date));
                            } else {
//                                mChosenDateToDatabase = DateUtilities.convertCalendarToIntUsedInDatabase(chosenStartDateFromDatePicker);
                                mStartDate = chosenStartDateFromDatePicker;
                                mStartDateButton.setText(DateUtilities.DATE_FORMAT_USED_ON_THE_BUTTONS.format(chosenStartDateFromDatePicker.getTime()));
                            }
                        }, currentYear, currentMonth, currentDayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.end_date_button:
                // Get Current Date
                c = Calendar.getInstance();
                currentYear = c.get(Calendar.YEAR);
                currentMonth = c.get(Calendar.MONTH);
                currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            Calendar chosenEndDateFromDatePicker = Calendar.getInstance();
                            chosenEndDateFromDatePicker.set(year, monthOfYear, dayOfMonth);
                            Calendar today = Calendar.getInstance();

                            int chosenEndDateComparedToToday = chosenEndDateFromDatePicker.compareTo(today);
                            int chosenEndDateComparedToChosenStartDate = chosenEndDateFromDatePicker.compareTo(mStartDate);

                            if (chosenEndDateComparedToToday > 0) {
                                Toast.makeText(getActivity(), R.string.chosen_date_greater_than_today, Toast.LENGTH_SHORT).show();
                                mEndDate = today;
                                mEndDateButton.setText(getString(R.string.choose_date));
                            } else if (chosenEndDateComparedToChosenStartDate < 0) {
                                Toast.makeText(getActivity(), R.string.chosen_date_earlier_than_start_date, Toast.LENGTH_SHORT).show();
                                mEndDate = today;
                                mEndDateButton.setText(getString(R.string.choose_date));
                            } else {
                                mEndDate = chosenEndDateFromDatePicker;
                                mEndDateButton.setText(DateUtilities.DATE_FORMAT_USED_ON_THE_BUTTONS.format(chosenEndDateFromDatePicker.getTime()));
                            }
                        }, currentYear, currentMonth, currentDayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.showExamsButton:
                if(mTagFromSpinner > 0) {
                    mCallbacks.onShowExamsButtonClicked(mTagFromSpinner, null, null);
                } else {
                    mCallbacks.onShowExamsButtonClicked(mTagFromSpinner, mStartDate, mEndDate);
                }
                break;
        }
    }


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

        Calendar today = Calendar.getInstance();
        mStartDate = today;
        mStartDateButton = (Button) v.findViewById(R.id.start_date_button);
        mStartDateButton.setText(ResultsBank.sDateFormat.format(today.getTime()));
        mStartDateButton.setVisibility(View.GONE);
        mStartDateButton.setOnClickListener(this);

        mEndDate = today;
        mEndDateButton = (Button) v.findViewById(R.id.end_date_button);
        mEndDateButton.setText(ResultsBank.sDateFormat.format(today.getTime()));
        mEndDateButton.setVisibility(View.GONE);
        mEndDateButton.setOnClickListener(this);

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

                if(mTagFromSpinner == ResultsBank.USER_DEFINED_PERIOD_OF_TIME_EXAMS) {
                    mStartDateButton.setVisibility(View.VISIBLE);
                    mEndDateButton.setVisibility(View.VISIBLE);
                } else {
                    mStartDateButton.setVisibility(View.GONE);
                    mEndDateButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mShowExamsButton = (Button) v.findViewById(R.id.showExamsButton);
        mShowExamsButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}

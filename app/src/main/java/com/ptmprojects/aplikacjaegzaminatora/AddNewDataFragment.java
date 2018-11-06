package com.ptmprojects.aplikacjaegzaminatora;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AddNewDataFragment extends Fragment implements View.OnClickListener {
        public static final String DEFAULT_CHOOSE_CATEGORY_SPINNER_VALUE = "B";
    private Button mOkButton;
    private Button mCancelButton;
    private Button mChooseDateButton;
    private RadioGroup mRadioGroup;
    private RadioButton mChosenRadioButton;
    private Spinner mChooseCategorySpinner;
    private String mCategorySelectedFromSpinner;
    private int mYear, mMonth, mDayOfMonth, mChosenDateToDatabase;
    private ResultsBank resultsBank;
    public static final String TAG = "AddNewDataFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_new_data, container, false);
        resultsBank = ResultsBank.get(getActivity());
        mRadioGroup = v.findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            mChosenRadioButton = v.findViewById(checkedId);
            });

        mChooseCategorySpinner = v.findViewById(R.id.choose_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.categories, android.R.layout.simple_spinner_item);
        int spinnerDefaultPosition = adapter.getPosition(DEFAULT_CHOOSE_CATEGORY_SPINNER_VALUE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseCategorySpinner.setAdapter(adapter);
        mChooseCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCategorySelectedFromSpinner = parent.getSelectedItem().toString();
                Log.d(TAG, "Selected value: "  + parent.getSelectedItem().toString());
                Log.d(TAG, "mCategorySelectedFromSpinner: " + mCategorySelectedFromSpinner.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategorySelectedFromSpinner = "XYZ";
            }
        });
        mChooseCategorySpinner.setSelection(spinnerDefaultPosition);
        Log.d(TAG, "Spinner value: " + mChooseCategorySpinner.getSelectedItem().toString());
        mCategorySelectedFromSpinner = mChooseCategorySpinner.getSelectedItem().toString();
        Log.d(TAG, "mCategorySelectedFromSpinner: " + mCategorySelectedFromSpinner.toString());

        mChooseDateButton = v.findViewById(R.id.choose_date_button);
        mChooseDateButton.setOnClickListener(this);

        mCancelButton = v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(this);

        mOkButton = v.findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(this);
        return v; }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_date_button:
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        (view, year, monthOfYear, dayOfMonth) -> {
                            Calendar chosenDate = Calendar.getInstance();
                            chosenDate.set(year, monthOfYear, dayOfMonth);
                            Calendar today = Calendar.getInstance();

                            int resultOfDatesComparison = chosenDate.compareTo(today);
                            if (resultOfDatesComparison > 0) {
                                Toast.makeText(getActivity(), R.string.chosen_date_greater_than_today, Toast.LENGTH_SHORT).show();
                                mChosenDateToDatabase = 0;
                                mChooseDateButton.setText(getString(R.string.choose_date));
                            } else {
                                mChosenDateToDatabase = DateUtilities.convertCalendarToIntUsedInDatabase(chosenDate);
                                mChooseDateButton.setText(DateUtilities.DATE_FORMAT_USED_ON_THE_BUTTONS.format(chosenDate.getTime()));
                            }
                        }, mYear, mMonth, mDayOfMonth);
                datePickerDialog.show();
                break;
            case R.id.ok_button:
                int selectedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
                Log.d(TAG, "selectedRadioButtonId: " + selectedRadioButtonId);
                CharSequence textFromChooseDateButton = mChooseDateButton.getText();
                CharSequence categoryChosenFromSpinner = mCategorySelectedFromSpinner;
                if (mChosenRadioButton == null && textFromChooseDateButton.equals(getString(R.string.choose_date))) {
                    Toast.makeText(getActivity(), R.string.date_and_result_not_chosen, Toast.LENGTH_SHORT).show();
                } else if (mChosenRadioButton == null) {
                    Toast.makeText(getActivity(), R.string.result_not_chosen, Toast.LENGTH_SHORT).show();
                } else if (textFromChooseDateButton.equals(getString(R.string.choose_date))) {
                    Toast.makeText(getActivity(), R.string.date_not_chosen, Toast.LENGTH_SHORT).show();
                } else {
                    ExamResult examResult = new ExamResult();
                    examResult.setDate(mChosenDateToDatabase);
                    examResult.setResult(ResultsBank.convertStringResultToCorrespondingInt(mChosenRadioButton.getText().toString()));
                    Log.d(TAG, "OK clicked: categoryChosenFromSpinner: " + categoryChosenFromSpinner + ", mCategorySelectedFromSpinner: " + mCategorySelectedFromSpinner);
                    examResult.setCategory(categoryChosenFromSpinner.toString());

                    // Just a sample ordering, needs to be implemented yet:
                    int orderNumber = 0;
                    examResult.setOrderNumber(orderNumber++);

                    resultsBank.addResult(examResult);

                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }
                break;
            case R.id.cancel_button:
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                break;
        }
    }
}

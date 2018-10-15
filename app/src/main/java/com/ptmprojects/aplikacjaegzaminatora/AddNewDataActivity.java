package com.ptmprojects.aplikacjaegzaminatora;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AddNewDataActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DEFAULT_CHOOSE_CATEGORY_SPINNER_VALUE = "B";
    private Button mOkButton;
    private Button mCancelButton;
    private Button mChooseDateButton;
    private RadioGroup mRadioGroup;
    private Spinner mChooseCategorySpinner;
    private int mYear, mMonth, mDayOfMonth, mChosenYear, mChosenMonth, mChosenDayOfMonth;

    public static final String TAG = "AddNewDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ResultsBank resultsBank = ResultsBank.get(this);
        Log.d(TAG, "AddNewDataActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_data);

        mRadioGroup = findViewById(R.id.radio_group);

        mChooseCategorySpinner = findViewById(R.id.choose_category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        int spinnerDefaultPosition = adapter.getPosition(DEFAULT_CHOOSE_CATEGORY_SPINNER_VALUE);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChooseCategorySpinner.setAdapter(adapter);
        mChooseCategorySpinner.setSelection(spinnerDefaultPosition);

        mChooseDateButton = findViewById(R.id.choose_date_button);
        mChooseDateButton.setOnClickListener(this);

        mCancelButton = findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNewDataActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        mOkButton = findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
                CharSequence textFromChooseDateButton = mChooseDateButton.getText();
                CharSequence categoryChosenFromSpinner = mChooseCategorySpinner.getSelectedItem().toString();
                Toast.makeText(AddNewDataActivity.this, categoryChosenFromSpinner, Toast.LENGTH_SHORT).show();
                if (selectedRadioButtonId != -1 & !textFromChooseDateButton.equals(getString(R.string.choose_date))) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonId);

                    ExamResult examResult = new ExamResult();
                    examResult.setYear(mChosenYear);
                    examResult.setMonth(mChosenMonth);
                    examResult.setDay(mChosenDayOfMonth);
                    examResult.setResult(ResultsBank.convertStringResultToCorrespondingInt(selectedRadioButton.getText().toString()));
                    examResult.setCategory(categoryChosenFromSpinner.toString());
                    // Just a sample ordering, needs to be implemented yet:
                    int orderNumber = 0;
                    examResult.setOrderNumber(orderNumber++);

                    resultsBank.addResult(examResult);

                    Intent i = new Intent(AddNewDataActivity.this, MainActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(AddNewDataActivity.this, R.string.adding_new_exam_result_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == mChooseDateButton) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            mChosenYear = year;
                            mChosenMonth = monthOfYear + 1;
                            mChosenDayOfMonth = dayOfMonth;
                            mChooseDateButton.setText(mChosenDayOfMonth + "-" + (mChosenMonth + 1) + "-" + mChosenYear);

                        }
                    }, mYear, mMonth, mDayOfMonth);
            datePickerDialog.show();
        }
    }
}

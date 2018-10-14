package com.ptmprojects.aplikacjaegzaminatora;

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

public class AddNewDataActivity extends AppCompatActivity {
    private Button mOkButton;
    private Button mCancelButton;
    private DatePicker mDatePicker;
    private RadioGroup mRadioGroup;
    private Spinner mChooseCategorySpinner;

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
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        mChooseCategorySpinner.setAdapter(adapter);

        Calendar now = Calendar.getInstance();
        mDatePicker = findViewById(R.id.datePicker);
        mDatePicker.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    }
                });
        mCancelButton = findViewById(R.id.cancel_button);

        mOkButton = findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = mRadioGroup.getCheckedRadioButtonId();
                if (selectedRadioButtonId != -1) {

                    RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonId);

                    ExamResult examResult = new ExamResult();
                    examResult.setYear(mDatePicker.getYear());
                    examResult.setMonth(mDatePicker.getMonth() + 1); // + 1 needed (January == 0, December == 11), etc...
                    examResult.setDay(mDatePicker.getDayOfMonth());
                    examResult.setResult(ResultsBank.convertStringResultToCorrespondingInt(selectedRadioButton.getText().toString()));
                    // Just a sample ordering, needs to be implemented yet:
                    int orderNumber = 0;
                    examResult.setOrderNumber(orderNumber++);

                    resultsBank.addResult(examResult);

                    Intent i = new Intent(AddNewDataActivity.this, MainActivity.class);
                    startActivity(i);

                } else {
                    Toast.makeText(AddNewDataActivity.this, R.string.result_not_chosen, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}

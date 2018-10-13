package com.ptmprojects.aplikacjaegzaminatora;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView rateOfPositiveResults;
    private TextView positive;
    private TextView didNotTakeAnExam;
    private TextView negativeManeuvringArea;
    private TextView negativeTrafficArea;
    private TextView numberOfExams;
    private Button addAnExamButton;

    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MainActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rateOfPositiveResults = (TextView) findViewById(R.id.rate_of_positive_results_result);
        positive = (TextView) findViewById(R.id.positive_result);
        didNotTakeAnExam = (TextView) findViewById(R.id.did_not_take_an_exam_result);
        negativeManeuvringArea = (TextView) findViewById(R.id.negative_maneuvring_area_result);
        negativeTrafficArea = (TextView) findViewById(R.id.negative_traffic_area_result);
        numberOfExams = (TextView) findViewById(R.id.number_of_exams_result);
        addAnExamButton = (Button) findViewById(R.id.add_an_exam_button);

        ResultsBank resultsBank = ResultsBank.get(this);

        double rateOfPositiveResults = resultsBank.countRateOfPositiveResults();
        int positiveResult = resultsBank.countNumberOfGivenResults(ResultsBank.POSITIVE_RESULT);
        int didNotTakeAnExamResult = resultsBank.countNumberOfGivenResults(ResultsBank.DID_NOT_TAKE_EXAM_RESULT);
        int negativeManeuvringAreaResult = resultsBank.countNumberOfGivenResults(ResultsBank.NEGATIVE_MANEUVRING_AREA_RESULT);
        int negativeTrafficAreaResult = resultsBank.countNumberOfGivenResults(ResultsBank.NEGATIVE_TRAFFIC_AREA_RESULT);
        int numberOfAllExams = resultsBank.countNumberOfAllExams();

        this.rateOfPositiveResults.setText(String.valueOf(rateOfPositiveResults));
        positive.setText(String.valueOf(positiveResult));
        didNotTakeAnExam.setText(String.valueOf(didNotTakeAnExamResult));
        negativeManeuvringArea.setText(String.valueOf(negativeManeuvringAreaResult));
        negativeTrafficArea.setText(String.valueOf(negativeTrafficAreaResult));
        numberOfExams.setText(String.valueOf(numberOfAllExams));

        addAnExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddNewDataActivity.class);
                startActivity(i);
            }
        });

    }
}
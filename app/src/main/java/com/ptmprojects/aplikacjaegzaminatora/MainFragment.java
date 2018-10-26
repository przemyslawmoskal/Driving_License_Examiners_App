package com.ptmprojects.aplikacjaegzaminatora;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainFragment extends Fragment implements View.OnClickListener{
    private TextView rateOfPositiveResults;
    private TextView positive;
    private TextView didNotTakeAnExam;
    private TextView negativeManeuvringArea;
    private TextView negativeTrafficArea;
    private TextView numberOfExams;
    private Button addAnExamButton;
    private Button exploreResultsButton;

    public static final String TAG = "MainFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        rateOfPositiveResults = (TextView) v.findViewById(R.id.rate_of_positive_results_result);
        positive = (TextView) v.findViewById(R.id.positive_result);
        didNotTakeAnExam = (TextView) v.findViewById(R.id.did_not_take_an_exam_result);
        negativeManeuvringArea = (TextView) v.findViewById(R.id.negative_maneuvring_area_result);
        negativeTrafficArea = (TextView) v.findViewById(R.id.negative_traffic_area_result);
        numberOfExams = (TextView) v.findViewById(R.id.number_of_exams_result);
        addAnExamButton = (Button) v.findViewById(R.id.add_an_exam_button);
        exploreResultsButton = (Button) v.findViewById(R.id.explore_results);

        ResultsBank resultsBank = ResultsBank.get(getActivity());

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

        addAnExamButton.setOnClickListener(this);
        exploreResultsButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_an_exam_button:
                Intent intentAddNewData = new Intent(getActivity(), AddNewDataActivity.class);
                startActivity(intentAddNewData);
                break;
            case R.id.explore_results:
                Intent examExplorer = new Intent(getActivity(), ExamExplorerActivity.class);
                startActivity(examExplorer);
                break;
        }
    }
}

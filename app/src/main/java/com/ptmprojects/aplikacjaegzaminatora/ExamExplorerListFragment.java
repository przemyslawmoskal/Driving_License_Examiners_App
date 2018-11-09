package com.ptmprojects.aplikacjaegzaminatora;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import javax.xml.transform.Result;

public class ExamExplorerListFragment extends Fragment{
    public static final String ARG_TYPE_OF_CHOSEN_PERIOD_OF_TIME = "ArgTypeOfChosenPeriodOfTime";
    public static final String ARG_CHOSEN_START_DATE = "ArgStartDateChosenByUser";
    public static final String ARG_CHOSEN_END_DATE = "ArgEndDateChosenByUser";
    public static final String EXTRA_TYPE_OF_CHOSEN_PERIOD_OF_TIME = "ExtraTypeOfChosenPeriodOfTime";
    public static final String EXTRA_CHOSEN_START_DATE = "ExtraStartDateChosenByUser";
    public static final String EXTRA_CHOSEN_END_DATE = "ExtraEndDateChosenByUser";

    private RecyclerView mExamListRecyclerView;
    private ExamAdapter mAdapter;

    private int mTypeOfChosenPeriodOfTime;
    private Calendar startDate;
    private Calendar endDate;

    private ResultsBank mResultsBank;

    public static ExamExplorerListFragment newInstance(Integer periodOfTime, Calendar optionalStartDate, Calendar optionalEndDate) {
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE_OF_CHOSEN_PERIOD_OF_TIME, periodOfTime);
        if (periodOfTime == 0) {
            args.putSerializable(ARG_CHOSEN_START_DATE, optionalStartDate);
            args.putSerializable(ARG_CHOSEN_END_DATE, optionalEndDate);
        }
        ExamExplorerListFragment fragment = new ExamExplorerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mResultsBank = ResultsBank.get(getActivity());
        mTypeOfChosenPeriodOfTime = getArguments().getInt(ARG_TYPE_OF_CHOSEN_PERIOD_OF_TIME);
        if (mTypeOfChosenPeriodOfTime == 0) {
            startDate =(Calendar) getArguments().getSerializable(ARG_CHOSEN_START_DATE);
            endDate= (Calendar)getArguments().getSerializable(ARG_CHOSEN_END_DATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_exam_explorer_list, container, false);
        mExamListRecyclerView = (RecyclerView) v.findViewById(R.id.exam_explorer_list_recycler_view);
        mExamListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(mTypeOfChosenPeriodOfTime, startDate, endDate);
        return v;
    }

    private void updateUI(int typeOfChosenPeriodOfTime, Calendar optionalStartDate, Calendar optionalEndDate) {
        List<ExamResult> resultsForSpecifiedPeriodOfTime = mResultsBank.getResultsForSpecifiedPeriodOfTime(typeOfChosenPeriodOfTime, optionalStartDate, optionalEndDate);

        mAdapter = new ExamAdapter(resultsForSpecifiedPeriodOfTime);
        mExamListRecyclerView.setAdapter(mAdapter);
    }

    private class ExamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mDateButton;
        private Spinner mCategorySpinner;
        private Spinner mResultSpinner;
        private Button mDeleteButton;
        private ExamResult mExamResult;

        public ExamHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_exam_explorer, parent, false));
            itemView.setOnClickListener(this);
            mDateButton = (Button) itemView.findViewById(R.id.list_item_date_button);
            mCategorySpinner = (Spinner) itemView.findViewById(R.id.list_item_category_spinner);
            mResultSpinner = (Spinner) itemView.findViewById(R.id.list_item_result_spinner);
            mDeleteButton = (Button) itemView.findViewById(R.id.list_item_delete_button);
        }

        public void bind(ExamResult examResult) {
            mExamResult = examResult;
            mDateButton.setText(DateUtilities.DATE_FORMAT_USED_ON_THE_BUTTONS_SHORT.format(DateUtilities.convertIntUsedInDatabaseToCalendar(mExamResult.getDate()).getTime()));
//
            mDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Calendar c = DateUtilities.convertIntUsedInDatabaseToCalendar((mExamResult.getDate()));

                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            (view, year, monthOfYear, dayOfMonth) -> {
                                Calendar chosenDate = Calendar.getInstance();
                                chosenDate.set(year, monthOfYear, dayOfMonth);

                                int mChosenDateToDatabase = DateUtilities.convertCalendarToIntUsedInDatabase(chosenDate);
                                mExamResult.setDate(mChosenDateToDatabase);
                                mResultsBank.updateResult(examResult);
                                mDateButton.setText(DateUtilities.DATE_FORMAT_USED_ON_THE_BUTTONS.format(chosenDate.getTime()));
                                updateUI(mTypeOfChosenPeriodOfTime, startDate, endDate);
                            }, mYear, mMonth, mDayOfMonth);
                    datePickerDialog.show();
                }
            });
            //

            ArrayAdapter<CharSequence> adapterCategories = ArrayAdapter.createFromResource(getActivity(),
                    R.array.categories, android.R.layout.simple_spinner_item);
            int categoriesSpinnerDefaultPosition = adapterCategories.getPosition(mExamResult.getCategory());
            adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mCategorySpinner.setAdapter(adapterCategories);
            mCategorySpinner.setSelection(categoriesSpinnerDefaultPosition);
            mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newCategory = parent.getSelectedItem().toString();
                    examResult.setCategory(newCategory);
                    mResultsBank.updateResult(examResult);
//                    Toast.makeText(getActivity(), "Wybrano nową kategorię: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            ArrayAdapter<CharSequence> adapterResults = ArrayAdapter.createFromResource(getActivity(),
                    R.array.results, android.R.layout.simple_spinner_item);
            int resultsSpinnerDefaultPosition = adapterResults.getPosition(ResultsBank.convertIntResultToCorrespondingString(mExamResult.getResult()));
            adapterResults.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mResultSpinner.setAdapter(adapterResults);
            mResultSpinner.setSelection(resultsSpinnerDefaultPosition);
            mResultSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String newResult = parent.getSelectedItem().toString();
                    examResult.setResult(ResultsBank.convertStringResultToCorrespondingInt(newResult));
                    mResultsBank.updateResult(examResult);
//                    Toast.makeText(getActivity(), "Wybrano nowy wynik: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            mDeleteButton.setText(getString(R.string.delete));
            mDeleteButton.setOnClickListener(v -> {
                mResultsBank.deleteResult(examResult);
                Toast.makeText(getActivity(), getString(R.string.result_deleted), Toast.LENGTH_SHORT).show();
                updateUI(mTypeOfChosenPeriodOfTime, startDate, endDate);
            });
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mExamResult.getDate() + " " + mExamResult.getResult() + " " +mExamResult.getCategory() + " klik!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ExamAdapter extends RecyclerView.Adapter<ExamHolder> {
        private List<ExamResult> mExamResults;
        public ExamAdapter(List<ExamResult> examResults) {
            mExamResults = examResults;
        }

        @Override
        public ExamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ExamHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ExamHolder holder, int position) {
            ExamResult examResult = mExamResults.get(position);
            holder.bind(examResult);
        }

        @Override
        public int getItemCount() {
            return mExamResults.size();
        }
    }
}

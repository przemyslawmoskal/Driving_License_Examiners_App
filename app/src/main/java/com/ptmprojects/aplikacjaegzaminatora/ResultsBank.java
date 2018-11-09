package com.ptmprojects.aplikacjaegzaminatora;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import database.ResultsBaseHelper;
import database.ResultsCursorWrapper;

import static database.ResultsDbSchema.*;

public class ResultsBank {
    public static final int POSITIVE_RESULT = 3;
    public static final int DID_NOT_TAKE_EXAM_RESULT = 2;
    public static final int NEGATIVE_MANEUVRING_AREA_RESULT = 1;
    public static final int NEGATIVE_TRAFFIC_AREA_RESULT = 0;

    public static final int USER_DEFINED_PERIOD_OF_TIME_EXAMS = 0;
    public static final int TODAY_EXAMS = 1;
    public static final int THIS_WEEK_EXAMS = 7;
    public static final int THIS_MONTH_EXAMS = 30;
    public static final int THIS_YEAR_EXAMS = 365;
    public static final int ALL_TIME_EXAMS = 1000;

    public static final SimpleDateFormat sDateFormat = new SimpleDateFormat("dd' 'MMMM' 'yyyy");
    public static final String TAG = "ResultsBank.java";

    private static ResultsBank sResultsBank;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private ResultsBank(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new ResultsBaseHelper(mContext)
                .getWritableDatabase();
//  I can use it to fill database with 10 random results
//        Random rand = new Random();
//        for (int i = 0; i < 10; i++) {
//            ExamResult result = new ExamResult();
//            result.setYear(2018);
//            result.setMonth(10);
//            result.setDay(5);
//            result.setOrderNumber(i + 1);
//            result.setResult(rand.nextInt(4));
//            result.setId(UUID.randomUUID());
//            addResult(result);
//        }
    }

    public static void loadDatabaseFromFile() {

    }

    public static void saveDatabaseToFile() {

    }

    public static int convertStringResultToCorrespondingInt(String resultString) {
        switch (resultString) {
            case "P":
                return ResultsBank.POSITIVE_RESULT;
            case "X":
                return ResultsBank.DID_NOT_TAKE_EXAM_RESULT;
            case "N":
                return ResultsBank.NEGATIVE_MANEUVRING_AREA_RESULT;
            case "n":
                return ResultsBank.NEGATIVE_TRAFFIC_AREA_RESULT;
            default:
                throw new IllegalArgumentException("Invalid exam result!");
        }
    }

    public static String convertIntResultToCorrespondingString(int resultInt) {
        switch (resultInt) {
            case ResultsBank.POSITIVE_RESULT:
                return "P";
            case ResultsBank.DID_NOT_TAKE_EXAM_RESULT:
                return "X";
            case ResultsBank.NEGATIVE_MANEUVRING_AREA_RESULT:
                return "N";
            case ResultsBank.NEGATIVE_TRAFFIC_AREA_RESULT:
                return "n";
            default:
                throw new IllegalArgumentException("Invalid exam result!");
        }
    }

    public static ResultsBank get(Context context) {
        if (sResultsBank == null) {
            sResultsBank = new ResultsBank(context);
        }
        return sResultsBank;
    }

    public List<ExamResult> getResults() {
        List<ExamResult> results = new ArrayList<>();

        ResultsCursorWrapper cursor = queryResults(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                results.add(cursor.getExamResult());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return results;
    }

    public List<ExamResult> getResultsForSpecifiedPeriodOfTime(int typeOfChosenPeriodOfTime, Calendar optionalStartDate, Calendar optionalEndDate) {
        List<ExamResult> results = new ArrayList<>();
        ResultsCursorWrapper cursor = null;
        Calendar today = Calendar.getInstance();
        Log.d(TAG, "typeOfPeriod: " + typeOfChosenPeriodOfTime +
                ", startDate: " + optionalStartDate +
                ", endDate: " + optionalEndDate +
                ", Calendar YEAR: " + today.get(Calendar.YEAR) +
                ", Calendar MONTH (+1): " + (DateUtilities.TWO_DIGIT_MONTH_FORMAT.format(today.get(Calendar.MONTH))) +
                ", Calendar DAY: " + today.get(Calendar.DAY_OF_MONTH));


        if(typeOfChosenPeriodOfTime == USER_DEFINED_PERIOD_OF_TIME_EXAMS) {
            String formattedStartDate = DateUtilities.DATE_FORMAT_USED_IN_DATABASE.format(optionalStartDate.getTime());
            String formattedEndDate = DateUtilities.DATE_FORMAT_USED_IN_DATABASE.format(optionalEndDate.getTime());

            cursor = queryResults(ResultsTable.Cols.DATE + " >= " + formattedStartDate +
                    " AND " + ResultsTable.Cols.DATE + " <= " + formattedEndDate, null);



        } else if(typeOfChosenPeriodOfTime == TODAY_EXAMS) {
            cursor = queryResults(ResultsTable.Cols.DATE + " = " + DateUtilities.convertCalendarToIntUsedInDatabase(today)
                    , null);
        } else if(typeOfChosenPeriodOfTime == THIS_WEEK_EXAMS) {
            Calendar firstDayOfCurrentWeek = (Calendar) today.clone();

            while (!(firstDayOfCurrentWeek.get(Calendar.DAY_OF_WEEK) == firstDayOfCurrentWeek.getFirstDayOfWeek())) {
                firstDayOfCurrentWeek.add(Calendar.DAY_OF_MONTH, -1);
            }

            String formattedFirstDayOfCurrentWeek = DateUtilities.DATE_FORMAT_USED_IN_DATABASE.format(firstDayOfCurrentWeek.getTime());
            String formattedToday = DateUtilities.DATE_FORMAT_USED_IN_DATABASE.format(today.getTime());

            cursor = queryResults(ResultsTable.Cols.DATE + " >= " + formattedFirstDayOfCurrentWeek +
                    " AND " + ResultsTable.Cols.DATE + " <= " + formattedToday, null);

        } else if(typeOfChosenPeriodOfTime == THIS_MONTH_EXAMS) {
            String thisYearAndMonthStartDate = DateUtilities.YEAR_MONTH_FORMAT.format((today.getTime())) + "01";//for 26.10.2018 it's 20180900
            String thisYearAndMonthEndDate = DateUtilities.YEAR_MONTH_FORMAT.format((today.getTime())) + "31";//for 26.10.2018 it's 20180931

            cursor = queryResults(ResultsTable.Cols.DATE + " >= " + thisYearAndMonthStartDate +
                    " AND " + ResultsTable.Cols.DATE + " <= " + thisYearAndMonthEndDate, null);

        } else if(typeOfChosenPeriodOfTime == THIS_YEAR_EXAMS) {
            String thisYearStartDate = DateUtilities.YEAR_FORMAT.format((today.getTime())) + "0001";//for 26.10.2018 it's 20180900
            Calendar firstDayOfNextYearFromNow = (Calendar) today.clone();
            firstDayOfNextYearFromNow.add(Calendar.YEAR, 1);
            firstDayOfNextYearFromNow.set(Calendar.DAY_OF_YEAR, 1);
            String formattedFirstDayOfNextYearFromNow = DateUtilities.DATE_FORMAT_USED_IN_DATABASE.format(firstDayOfNextYearFromNow.getTime());

            cursor = queryResults(ResultsTable.Cols.DATE + " >= " + thisYearStartDate +
                    " AND " + ResultsTable.Cols.DATE + " < " + formattedFirstDayOfNextYearFromNow, null);

        } else if(typeOfChosenPeriodOfTime == ALL_TIME_EXAMS) {
            cursor = queryResults(null, null);
        }

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                results.add(cursor.getExamResult());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return results;
    }

    private static ContentValues getContentValues(ExamResult result) {
        ContentValues values = new ContentValues();
        values.put(ResultsTable.Cols.DATE, result.getDate());
        values.put(ResultsTable.Cols.ORDER_NUMBER, result.getOrderNumber());
        values.put(ResultsTable.Cols.CATEGORY, result.getCategory());
        values.put(ResultsTable.Cols.RESULT, result.getResult());
        values.put(ResultsTable.Cols.UUID, result.getId().toString());
        return values;
    }

    public void addResult(ExamResult result) {
        ContentValues values = getContentValues(result);
        mDatabase.insert(ResultsTable.NAME, null, values);
    }

    public ExamResult getResult(UUID id) {
        ResultsCursorWrapper cursor = queryResults(
                ResultsTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getExamResult();
        } finally {
            cursor.close();
        }
    }

    public void updateResult(ExamResult result) {
        String uuidString = result.getId().toString();
        ContentValues values = getContentValues(result);

        mDatabase.update(ResultsTable.NAME, values, ResultsTable.Cols.UUID + "= ?", new String[]{uuidString});
    }

    public void deleteResult(ExamResult result) {
        String uuidString = result.getId().toString();
        mDatabase.delete(ResultsTable.NAME, ResultsTable.Cols.UUID + "= ?", new String[]{uuidString});
    }

    private ResultsCursorWrapper queryResults(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ResultsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                ResultsTable.Cols.DATE + ", " + ResultsTable.Cols.ORDER_NUMBER
        );

        return new ResultsCursorWrapper(cursor);
    }

    public int countNumberOfAllExams() {
        ResultsCursorWrapper cursor = queryResults(null, null);
        return cursor.getCount();
    }

    public double countRateOfPositiveResults() {

        ResultsCursorWrapper cursorForPositive = queryResults(
                ResultsTable.Cols.RESULT + " = " + ResultsBank.POSITIVE_RESULT,
                null
        );
        ResultsCursorWrapper cursorForForDidNotTakeExam = queryResults(
                ResultsTable.Cols.RESULT + " = " + ResultsBank.DID_NOT_TAKE_EXAM_RESULT,
                null
        );
        ResultsCursorWrapper cursorForAllRows = queryResults(null, null);

        try {
            int positiveCount = cursorForPositive.getCount();
            int didNotTakeExamCount = cursorForForDidNotTakeExam.getCount();
            int allRowsCount = cursorForAllRows.getCount();
            int allMinusDidNotTakeExamCount = allRowsCount - didNotTakeExamCount;

            if (allMinusDidNotTakeExamCount == 0) {
                return 0.0d;
            }

            double rateOfPositiveResults = ((double) positiveCount / (double) allMinusDidNotTakeExamCount) * 100;


            return rateOfPositiveResults;
        } finally {
            cursorForPositive.close();
            cursorForForDidNotTakeExam.close();
            cursorForAllRows.close();
        }
    }

    public int countNumberOfGivenResults(int wynik) {
        if (!((((wynik == ResultsBank.POSITIVE_RESULT)) ||
                (wynik == ResultsBank.DID_NOT_TAKE_EXAM_RESULT) ||
                (wynik == ResultsBank.NEGATIVE_MANEUVRING_AREA_RESULT) ||
                (wynik == ResultsBank.NEGATIVE_TRAFFIC_AREA_RESULT)))) {
            return -1;
        }

        ResultsCursorWrapper cursor = queryResults(
                ResultsTable.Cols.RESULT + " = " + wynik,
                null
        );

        return cursor.getCount();
    }

    public int countNumberOfAllExamsDividedByDidNotTakeAnExam() {
        ResultsCursorWrapper cursorForDidNotTakeExam = queryResults(
                ResultsTable.Cols.RESULT + " = " + ResultsBank.DID_NOT_TAKE_EXAM_RESULT,
                null
        );
        ResultsCursorWrapper cursorForAllRows = queryResults(null, null);

        try {
            int didNotTakeExamCount = cursorForDidNotTakeExam.getCount();
            int allRowsCount = cursorForAllRows.getCount();
            return allRowsCount - didNotTakeExamCount;
        } finally {
            cursorForDidNotTakeExam.close();
            cursorForAllRows.close();
        }
    }

}

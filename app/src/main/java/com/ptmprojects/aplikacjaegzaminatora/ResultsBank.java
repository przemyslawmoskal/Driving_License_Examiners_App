package com.ptmprojects.aplikacjaegzaminatora;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    public static final SimpleDateFormat sdateFormat= new SimpleDateFormat("dd-MM-yyyy");

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

    // to return int 20181015 from date 15-10-2018:
//    public static int convertCalendarToInteger(Calendar calendar) {
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH) + 1; // January == 0, February == 1, etc...
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        String wholeDateAsString = "" + year + month + day;
//        return Integer.valueOf(wholeDateAsString);
//    }

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

    private static ContentValues getContentValues(ExamResult result) {
        ContentValues values = new ContentValues();
        values.put(ResultsTable.Cols.YEAR, result.getYear());
        values.put(ResultsTable.Cols.MONTH, result.getMonth());
        values.put(ResultsTable.Cols.DAY, result.getDay());
        values.put(ResultsTable.Cols.ORDER_NUMBER, result.getOrderNumber());
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

    private ResultsCursorWrapper queryResults(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ResultsTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
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

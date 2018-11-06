package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ptmprojects.aplikacjaegzaminatora.ExamResult;

import java.util.UUID;

import static database.ResultsDbSchema.*;

public class ResultsCursorWrapper extends CursorWrapper {
    public ResultsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ExamResult getExamResult() {
        int date = getInt(getColumnIndex(ResultsTable.Cols.DATE));
        int orderNumber = getInt(getColumnIndex(ResultsTable.Cols.ORDER_NUMBER));
        String category = getString(getColumnIndex(ResultsTable.Cols.CATEGORY));
        int result = getInt(getColumnIndex(ResultsTable.Cols.RESULT));
        String uuidString = getString(getColumnIndex(ResultsTable.Cols.UUID));

        ExamResult examResult = new ExamResult(UUID.fromString(uuidString));
        examResult.setDate(date);
        examResult.setOrderNumber(orderNumber);
        examResult.setCategory(category);
        examResult.setResult(result);

        return examResult;
    }
}

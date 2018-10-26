package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static database.ResultsDbSchema.*;

public class ResultsBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "resultsBase.db";

    public ResultsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ResultsTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
//                ResultsTable.Cols.YEAR + " INTEGER, " +
//                ResultsTable.Cols.MONTH + " INTEGER, " +
//                ResultsTable.Cols.DAY + " INTEGER, " +
                ResultsTable.Cols.DATE + " INTEGER, " +
                ResultsTable.Cols.ORDER_NUMBER + " INTEGER, " +
                ResultsTable.Cols.CATEGORY + " TEXT, " +
                ResultsTable.Cols.RESULT + " INTEGER, " +
                ResultsTable.Cols.UUID + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

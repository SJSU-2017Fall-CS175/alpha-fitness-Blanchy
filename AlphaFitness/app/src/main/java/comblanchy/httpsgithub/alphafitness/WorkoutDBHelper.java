package comblanchy.httpsgithub.alphafitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by blanchypolangcos on 11/26/17.
 */

public class WorkoutDBHelper extends SQLiteOpenHelper {

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String GENDER = "gender";
    static final String WEIGHT = "weight";
    static final String STEPS = "steps";
    static final String TIME = "time";

    static final String DATABASE_NAME = "workoutDB";
    static final String USER_TABLE_NAME = "user";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + USER_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " gender TEXT NOT NULL, " +
                    " weight INT NOT NULL, " +
                    " steps INT NOT NULL, " +
                    " time INT NOT NULL" +
                    ");";
    static final String POPULATE_VALUES = "INSERT INTO " + USER_TABLE_NAME + " " +
            "VALUES ('Beatrice', 'female', 150, 4000, 15000);";

    public WorkoutDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public String getRecentString(String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + value + " FROM " + USER_TABLE_NAME,null);
        if (c != null && c.moveToFirst() ) {
            c.moveToLast();
        }
        return c.getString(0);
    }

    public int getSumInt(String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + value + ") FROM " + USER_TABLE_NAME,null);
        if (c != null && c.moveToFirst() ) {
            c.moveToLast();
        }
        return c.getInt(0);
    }

    public int getNumRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + USER_TABLE_NAME,null);
        if (c != null && c.moveToFirst() ) {
            c.moveToLast();
        }
        return c.getInt(0);
    }

    /*public String getName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT + name FROM ",null);
    }

    public String getGender() {
        SQLiteDatabase db = this.getReadableDatabase();
    }

    public int getWeight() {
        SQLiteDatabase db = this.getReadableDatabase();
    }

    public int getSteps() {
        SQLiteDatabase db = this.getReadableDatabase();
    }

    public int getTime() {
        SQLiteDatabase db = this.getReadableDatabase();
    }*/

    public void addEntry(String name, String gender, int weight, int steps, int time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(GENDER, gender);
        values.put(WEIGHT, weight);
        values.put(STEPS, steps);
        values.put(TIME, time);

        long newRowId = db.insert(USER_TABLE_NAME, null, values);
    }
}

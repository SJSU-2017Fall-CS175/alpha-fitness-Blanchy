package comblanchy.httpsgithub.alphafitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

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
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_DB_TABLE =
            "CREATE TABLE " + USER_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY, " +
                    " name TEXT, " +
                    " gender TEXT, " +
                    " weight INT, " +
                    " steps INT, " +
                    " time INT" +
                    ");";
    static final String POPULATE_VALUES = "INSERT INTO " + USER_TABLE_NAME +
            " VALUES (0, 'Beatrice', 'female', 150, 4000, 15000);";

    private int workoutCount;

    public WorkoutDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
        db.execSQL(POPULATE_VALUES);
        Log.d("DATABASE", "is created");
        workoutCount = 1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(sqLiteDatabase);
    }

    public String getRecentString(String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + value + " FROM " + USER_TABLE_NAME,null);
        if (c != null && c.moveToFirst() ) {
            c.moveToLast();
        }
        return c.getString(0);
    }

    public int getSumSteps() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(" + STEPS + ") FROM " + USER_TABLE_NAME, null);
        if(c.moveToFirst())
        {
            return c.getInt(0);
        }
        else
        {
            return 0;
        }

    }

    public int getSumSeconds() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT sum(" + TIME + ") FROM " + USER_TABLE_NAME, null);
        Log.d("Cursor, sum of sec", String.valueOf(c.moveToFirst()));
        if(c.moveToFirst())
        {
            //Log.d("Cursor for seconds", "ACCESSED and int is " + c.getColumnCount());
            return c.getInt(0);
        }
        else
        {
            return 0;
        }
    }

    public WorkoutEntry getLatestProfile() {
        Log.d("Entry check", "retrieving from DB");
        WorkoutEntry we = new WorkoutEntry();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);
        Log.d("Query check", "query made");
        if(c.moveToFirst()) {
            Log.d("Cursor check", String.valueOf(c.moveToFirst()));
            c.moveToLast();
            we.set_id(c.getInt(0));
            Log.d("Entry check",c.getInt(0) +"");
            we.setName(c.getString(1));
            Log.d("Entry check",c.getString(1));
            we.setGender(c.getString(2));
            we.setWeight(c.getInt(3));
            close();
        }
        return we;
    }

    public int getNumEntries() {
        return workoutCount;
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

    public void addEntry(WorkoutEntry we) {
        SQLiteDatabase db = this.getWritableDatabase();

        workoutCount++;

        ContentValues values = new ContentValues();
        values.put(_ID, workoutCount);
        values.put(NAME, we.getName());
        values.put(GENDER, we.getGender());
        values.put(WEIGHT, we.getWeight());
        values.put(STEPS, we.getSteps());
        values.put(TIME, we.getSeconds());

        long newRowId = db.insert(USER_TABLE_NAME, null, values);

        db.close();
    }

    public void editEntry(WorkoutEntry we) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, we.getName());
        values.put(GENDER, we.getGender());
        values.put(WEIGHT, we.getWeight());
        values.put(STEPS, we.getSteps());
        values.put(TIME, we.getSeconds());

        db.update(USER_TABLE_NAME, values, _ID + " = ?", new String[] {
                String.valueOf(we.get_id())
        });

        db.close();
    }
}

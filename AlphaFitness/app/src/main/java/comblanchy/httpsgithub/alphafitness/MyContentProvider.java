package comblanchy.httpsgithub.alphafitness;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

public class MyContentProvider extends ContentProvider {

    private final static String TAG = MyContentProvider.class.getSimpleName();

    static final String PROVIDER = "com.wearable.myprovider";
    static final String URL = "content://" + PROVIDER + "/user";
    static final Uri URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String GENDER = "gender";
    static final String DISTANCE = "";
    static final String TIME = "";
    static final String WORKOUTS = "";
    static final String CALORIES = "";

    private static HashMap<String, String> USER_PROJECTION_MAP;

    static final int USERS = 1;
    static final int USER_ID = 2;

    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "devices", USERS);
        uriMatcher.addURI(PROVIDER, "devices/#", USER_ID);
    }

    Context mContext;


    public MyContentProvider() {
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "myprovider";
    static final String USER_TABLE_NAME = "user";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + USER_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " gender TEXT NOT NULL, " +
                    " distance REAL NOT NULL, " +
                    " time INT NOT NULL, " +
                    " workouts INT NOT NULL, " +
                    " calories INT NOT NULL" +
                    ");";

    private static class DB extends SQLiteOpenHelper
    {
        DB(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  USER_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        if (mContext == null) {
            Log.e(TAG, "Failed to retrieve the context.");
            return false;
        }
        DB dbHelper = new DB(mContext);
        db = dbHelper.getWritableDatabase();
        if (db == null) {
            Log.e(TAG, "Failed to create a writable database");
            return false;
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

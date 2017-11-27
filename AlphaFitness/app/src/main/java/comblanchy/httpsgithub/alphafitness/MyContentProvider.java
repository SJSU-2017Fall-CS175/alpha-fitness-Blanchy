/**
 * Used as reference:
 * https://github.com/AngusY/MyContentProvider/blob/master/app/src/main/java/com/wearable/mycontentprovider/MyContentProvider.java
 */

package comblanchy.httpsgithub.alphafitness;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
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
    static final String WEIGHT = "weight";
    static final String STEPS = "steps";
    static final String TIME = "time";


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
                    " weight INT NOT NULL, " +
                    " steps INT NOT NULL, " +
                    " time INT NOT NULL, " +
                    ");";
    static final String POPULATE_VALUES = "INSERT INTO " + USER_TABLE_NAME + " " +
            "VALUES ('Beatrice', 'female', 150, 4000, 15000);";

    private static class DB extends SQLiteOpenHelper
    {
        DB(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE);
            db.execSQL(POPULATE_VALUES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  USER_TABLE_NAME);
            onCreate(db);
        }
    }

    private void notifyChange(Uri uri) {
        ContentResolver resolver = mContext.getContentResolver();
        if (resolver != null) resolver.notifyChange(uri, null);
    }

    private int getMatchedID(Uri uri) {
        int matchedID = uriMatcher.match(uri);
        if (!(matchedID == USERS || matchedID == USER_ID))
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        return matchedID;
    }

    private String getIdString(Uri uri) {
        return (_ID + " = " + uri.getPathSegments().get(1));
    }

    private String getSelectionWithID(Uri uri, String selection) {
        String sel_str = getIdString(uri);
        if (!TextUtils.isEmpty(selection))
            sel_str +=" AND (" +selection + ')';
        return sel_str;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri)
    {
        Log.v(TAG, "content provider: getType()");

        if (getMatchedID(uri) == USERS)
            return "vnd.android.cursor.dir/vnd.wearable.user";
        else
            return "vnd.android.cursor.item/vnd.wearable.user";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "content provider: insert()");

        long row = db.insert(USER_TABLE_NAME, "", values);

        if (row > 0) {
            Uri _uri = ContentUris.withAppendedId(URI, row);
            notifyChange(_uri);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
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
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(USER_TABLE_NAME);

        if (getMatchedID(uri) == USERS) {
            sqLiteQueryBuilder.setProjectionMap(USER_PROJECTION_MAP);
        } else {
            sqLiteQueryBuilder.appendWhere( getIdString(uri) );
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = NAME;
        }
        Cursor c = sqLiteQueryBuilder.query(db,	projection,	selection,
                selectionArgs, null, null, sortOrder);

        c.setNotificationUri(mContext.getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.v(TAG, "content provider: update()");

        int count = 0;
        int matchedID = getMatchedID(uri);

        String sel_str = (matchedID == USER_ID) ?
                getSelectionWithID(uri, selection) : selection;

        count = db.update(
                USER_TABLE_NAME,
                values,
                sel_str,
                selectionArgs);

        notifyChange(uri);
        return count;
    }

    public SQLiteDatabase getDB() {
        return db;
    }
}

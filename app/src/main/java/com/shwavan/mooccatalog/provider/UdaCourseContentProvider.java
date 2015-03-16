package com.shwavan.mooccatalog.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.shwavan.mooccatalog.db.DBHelper;
import com.shwavan.mooccatalog.models.UdacityCourse;

public class UdaCourseContentProvider extends ContentProvider {

    DBHelper dbHelper ;
    public static final String AUTHORITY = "com.shwavan.provider.mooccatalog";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/courses");
    public UdaCourseContentProvider() {
    }
    private static final int ALL_COURSES = 1;
    private static final int SINGLE_COURSE = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "courses", ALL_COURSES);
        uriMatcher.addURI(AUTHORITY, "courses/*", SINGLE_COURSE);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)) {
            case ALL_COURSES:
                return "vnd.android.cursor.dir/vnd.com.shwavan.provider.mooccatalog.courses";
            case SINGLE_COURSE:
                return "vnd.android.cursor.item/vnd.com.shwavan.provider.mooccatalog.courses";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public Uri insert(Uri uri, UdacityCourse course) {
        // TODO: Implement this to handle requests to insert a new row.
        //throw new UnsupportedOperationException("Not yet implemented");
        switch (uriMatcher.match(uri)) {
            case ALL_COURSES:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        String id = dbHelper.addUdacityCourse(course);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);

    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHelper.TABLE_UDACITY);

        switch (uriMatcher.match(uri)) {
            case ALL_COURSES:
                //do nothing
                break;
            case SINGLE_COURSE:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DBHelper.KEY_CID + " = \"" + id+"\"");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {


        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

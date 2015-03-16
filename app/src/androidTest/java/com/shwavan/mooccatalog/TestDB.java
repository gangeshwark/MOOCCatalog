package com.shwavan.mooccatalog;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.shwavan.mooccatalog.db.DBHelper;

import java.util.Calendar;

/**
 * Created by GANGESHWAR on 03-03-2015.
 */
public class TestDB extends AndroidTestCase {
    public static final String TABLE_UDACITY = "udacity";
    public static final String TABLE_COURSERA = "coursera";

    // Udacity Course Columns names

    public static final String KEY_ID = "_id";
    public static final String KEY_CID = "key";// ud*** or cs***
    public static final String KEY_SUBTITLE = "subtitle";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FEATURED = "featured";
    public static final String KEY_VIDEO = "teaser_video";
    public static final String KEY_REQ = "required_knowledge";
    public static final String KEY_NEW = "new_release";
    public static final String KEY_HOMEPAGE = "homepage";
    public static final String KEY_SHORT_SUM = "short_summary";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_NOINSTRUCTORS = "n_instructors";
    public static final String KEY_PROJECT_NAME = "project_name";

    public static final String KEY_UPDATED_ON = "updated_on";
    private static final String KEY_SUM = "summary";
    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
        SQLiteDatabase db = new DBHelper(this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());
        db.close();
    }

    public void insertDb() throws Throwable{
        mContext.deleteDatabase(DBHelper.DATABASE_NAME);
        SQLiteDatabase db = new DBHelper(this.mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CID,"ud853");
        values.put(KEY_TITLE,"Test");
        values.put(KEY_SUBTITLE,"Subtitle");
        values.put(KEY_SHORT_SUM,"Short Sum");
        values.put(KEY_SUM,"Summary");
        values.put(KEY_IMAGE,"image");
        values.put(KEY_VIDEO,"video");
        values.put(KEY_HOMEPAGE,"home");
        values.put(KEY_NEW,"true");
        values.put(KEY_LEVEL,"level");
        values.put(KEY_FEATURED,"Featured");
        values.put(KEY_REQ,"Required");
        values.put(KEY_NOINSTRUCTORS,"instr");
        values.put(KEY_PROJECT_NAME,"Projname");
        values.put(KEY_UPDATED_ON, Calendar.getInstance().toString());
        db.insert(TABLE_UDACITY, null, values);


    }

}

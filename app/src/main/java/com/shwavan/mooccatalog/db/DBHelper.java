package com.shwavan.mooccatalog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shwavan.mooccatalog.models.UdacityCourse;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by GANGESHWAR on 03-03-2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    // Database Name
    public static final String DATABASE_NAME = "Mooccatalog.db";
    // Lists table name
    public static final String TABLE_UDACITY = "udacity";
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
    public static final String KEY_SUM = "summary";
    //Tables
    private static final String CREATE_UDACITY_TABLE = "CREATE TABLE " + TABLE_UDACITY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CID + " TEXT, "
            + KEY_TITLE + " TEXT,"
            + KEY_SUBTITLE + " TEXT, "
            + KEY_SHORT_SUM + " TEXT, "
            + KEY_SUM + " TEXT, "
            + KEY_IMAGE + " TEXT, "
            + KEY_VIDEO + " TEXT, "
            + KEY_HOMEPAGE + " TEXT, "
            + KEY_NEW + " TEXT, "
            + KEY_LEVEL + " TEXT, "
            + KEY_FEATURED + " TEXT, "
            + KEY_REQ + " TEXT, "
            + KEY_NOINSTRUCTORS + " INTEGER, "
            + KEY_PROJECT_NAME + " TEXT, "
            + KEY_UPDATED_ON + " TEXT, "
            + " UNIQUE (" + KEY_CID + ") ON CONFLICT REPLACE"
            + ");";

    // Database Version
    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_UDACITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_UDACITY, null, null);
        db.close();
    }

    public String addUdacityCourse(UdacityCourse course){
        String id = "-1";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CID,course.getKey());
        values.put(KEY_TITLE,course.getTitle());
        values.put(KEY_SUBTITLE,course.getSubtitle());
        values.put(KEY_SHORT_SUM,course.getShort_sum());
        values.put(KEY_SUM,course.getSummary());
        values.put(KEY_IMAGE,course.getImage_url());
        values.put(KEY_VIDEO,course.getVideo_url());
        values.put(KEY_HOMEPAGE,course.getHome_url());
        values.put(KEY_NEW,course.getNew_course());
        values.put(KEY_LEVEL,course.getLevel());
        values.put(KEY_FEATURED,course.isFeatured());
        values.put(KEY_REQ,course.getReq());
        values.put(KEY_NOINSTRUCTORS,course.getNo_of_instructors());
        values.put(KEY_PROJECT_NAME,course.getProject_name());
        values.put(KEY_UPDATED_ON, Calendar.getInstance().toString());
        try {
            db.insert(TABLE_UDACITY, null, values);
            id = course.getKey();
        }
        catch (SQLException e) {
            Log.e("Error writing new list ", e.toString());
        }
        finally {
            db.close();
        }
        return id;
    }

    /*
    * For all Courses - Regular and Nanodegree
    */
    public ArrayList<UdacityCourse> getUdacityCourseList(){
        ArrayList<UdacityCourse> udacityCourses = new ArrayList<UdacityCourse>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_UDACITY,null,null,null,null,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do {
                    UdacityCourse course = new UdacityCourse();
                    course.setId(cursor.getLong(0));
                    course.setKey(cursor.getString(1));
                    course.setTitle(cursor.getString(2));
                    course.setSubtitle(cursor.getString(3));
                    course.setShort_sum(cursor.getString(4));
                    course.setSummary(cursor.getString(5));
                    course.setImage_url(cursor.getString(6));
                    course.setVideo_url(cursor.getString(7));
                    course.setHome_url(cursor.getString(8));
                    course.setNew_course(Boolean.valueOf(cursor.getString(9)));
                    course.setLevel(cursor.getString(10));
                    course.setFeatured(Boolean.valueOf(cursor.getString(11)));
                    course.setReq(cursor.getString(12));
                    course.setNo_of_instructors(cursor.getString(13));
                    course.setProject_name(cursor.getString(14));
                    course.setUpdated_on(cursor.getString(15));
                    udacityCourses.add(course);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return udacityCourses;
    }

    /*
    * For Regular Courses
    */
    public ArrayList<UdacityCourse> getUdacityRCourseList() {
        ArrayList<UdacityCourse> udacityCourses = new ArrayList<UdacityCourse>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_UDACITY, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    UdacityCourse course = new UdacityCourse();
                    course.setId(cursor.getLong(0));
                    course.setKey(cursor.getString(1));
                    course.setTitle(cursor.getString(2));
                    course.setSubtitle(cursor.getString(3));
                    course.setShort_sum(cursor.getString(4));
                    course.setSummary(cursor.getString(5));
                    course.setImage_url(cursor.getString(6));
                    course.setVideo_url(cursor.getString(7));
                    course.setHome_url(cursor.getString(8));
                    course.setNew_course(Boolean.valueOf(cursor.getString(9)));
                    course.setLevel(cursor.getString(10));
                    course.setFeatured(Boolean.valueOf(cursor.getString(11)));
                    course.setReq(cursor.getString(12));
                    course.setNo_of_instructors(cursor.getString(13));
                    course.setProject_name(cursor.getString(14));
                    course.setUpdated_on(cursor.getString(15));
                    if (!cursor.getString(1).trim().startsWith("nd")) {
                        udacityCourses.add(course);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return udacityCourses;
    }

    /*
    * For Nanodegree Courses
    */
    public ArrayList<UdacityCourse> getUdacityDCourseList() {
        ArrayList<UdacityCourse> udacityCourses = new ArrayList<UdacityCourse>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_UDACITY, null, null, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    UdacityCourse course = new UdacityCourse();
                    course.setId(cursor.getLong(0));
                    course.setKey(cursor.getString(1));
                    course.setTitle(cursor.getString(2));
                    course.setSubtitle(cursor.getString(3));
                    course.setShort_sum(cursor.getString(4));
                    course.setSummary(cursor.getString(5));
                    course.setImage_url(cursor.getString(6));
                    course.setVideo_url(cursor.getString(7));
                    course.setHome_url(cursor.getString(8));
                    course.setNew_course(Boolean.valueOf(cursor.getString(9)));
                    course.setLevel(cursor.getString(10));
                    course.setFeatured(Boolean.valueOf(cursor.getString(11)));
                    course.setReq(cursor.getString(12));
                    course.setNo_of_instructors(cursor.getString(13));
                    course.setProject_name(cursor.getString(14));
                    course.setUpdated_on(cursor.getString(15));
                    if (cursor.getString(1).trim().startsWith("nd")) {
                        udacityCourses.add(course);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        db.close();
        return udacityCourses;
    }
    public UdacityCourse getCourseDetails(String courseId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_UDACITY+" WHERE " + KEY_CID + " = " +"'" + courseId + "'" + ";",null);
        if(cursor!=null){
            cursor.moveToFirst();
            UdacityCourse course = new UdacityCourse();
            course.setId(cursor.getLong(0));
            course.setKey(cursor.getString(1));
            course.setTitle(cursor.getString(2));
            course.setSubtitle(cursor.getString(3));
            course.setShort_sum(cursor.getString(4));
            course.setSummary(cursor.getString(5));
            course.setImage_url(cursor.getString(6));
            course.setVideo_url(cursor.getString(7));
            course.setHome_url(cursor.getString(8));
            course.setNew_course(Boolean.valueOf(cursor.getString(9)));
            course.setLevel(cursor.getString(10));
            course.setFeatured(Boolean.valueOf(cursor.getString(11)));
            course.setReq(cursor.getString(12));
            course.setNo_of_instructors(cursor.getString(13));
            course.setProject_name(cursor.getString(14));
            course.setUpdated_on(cursor.getString(15));
            Log.e("getCourseDetails", course.toString());
            cursor.close();
            return  course;
        }
        db.close();
        return null;
    }
}

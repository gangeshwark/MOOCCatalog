package com.shwavan.mooccatalog.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.shwavan.mooccatalog.Constants;
import com.shwavan.mooccatalog.db.DBHelper;
import com.shwavan.mooccatalog.models.UdacityCourse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchCourseListService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public FetchCourseListService(){
        super(FetchCourseListService.class.getSimpleName());

    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public FetchCourseListService(String name) {
        super(FetchCourseListService.class.getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                String[] results = downloadData(url);

                /* Sending result back to activity */
                if (null != results && results.length > 0) {
                    bundle.putStringArray("result", results);
                    receiver.send(STATUS_FINISHED, bundle);
                }
            } catch (Exception e) {

                /* Sending error message back to activity */
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private String[] downloadData(String requestUrl) throws IOException, DownloadException {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        /* for Get request */
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            String[] results = parseResult(response);
            return results;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    private String[] parseResult(String result) {

        String[] courseTitles = null, degTitles = null;
        String[] titles = null;
        try {
            JSONObject jsonObj = new JSONObject(result);

            // Getting JSON Array node
            JSONArray courses = jsonObj.getJSONArray(Constants.Udacity.COURSES_FIELD);
            courseTitles = new String[courses.length()];
            Toast.makeText(getApplication(),"No of courses"+courses.length(),Toast.LENGTH_SHORT).show();
            // looping through All Courses
            for (int i = 0; i < courses.length(); i++) {
                JSONObject c = courses.getJSONObject(i);
                //JSONArray in = c.getJSONArray(Constants.Udacity.INSTRUCTORS);
                //JSONObject instr = c.getJSONObject(Constants.Udacity.INSTRUCTORS);
                //Log.e("INSTRUCTOR",in.toString());
                String key = c.getString(Constants.Udacity.UDA_PARAM_COURSE_KEY);
                String title = c.getString(Constants.Udacity.UDA_PARAM_TITLE);
                String subtitle = c.getString(Constants.Udacity.UDA_PARAM_SUBTITLE);
                String image = c.getString(Constants.Udacity.UDA_PARAM_IMAGE);
                String projname = c.getString(Constants.Udacity.UDA_PARAM_PROJECTNAME);
                String level = c.getString(Constants.Udacity.UDA_PARAM_LEVEL);
                String shortsum = c.getString(Constants.Udacity.UDA_PARAM_SHORTSUM);
                String summary = c.getString(Constants.Udacity.UDA_PARAM_SUMMARY);
                JSONObject vlink = c.getJSONObject(Constants.Udacity.UDA_PARAM_TEASER_VID);

                String video = vlink.getString(Constants.Udacity.UDA_PARAM_VIDEO);
                String featured = c.getString(Constants.Udacity.UDA_PARAM_FEATURED);
                String req = c.getString(Constants.Udacity.UDA_PARAM_REQ);
                String newstat = c.getString(Constants.Udacity.UDA_PARAM_NEW);
                String url = c.getString(Constants.Udacity.UDA_PARAM_LINK);

                courseTitles[i] = title;
                UdacityCourse course = new UdacityCourse();
                course.setKey(key.trim());
                course.setFeatured(Boolean.getBoolean(featured.trim()));
                course.setTitle(title.trim());
                course.setSubtitle(subtitle.trim());
                course.setImage_url(image);
                course.setHome_url(url);
                course.setProject_name(projname.trim());
                course.setLevel(level.trim());
                course.setShort_sum(shortsum.trim());
                course.setReq(req.trim());
                course.setNew_course(Boolean.getBoolean(newstat));
                course.setVideo_url(video);
                course.setSummary(summary.trim());
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.addUdacityCourse(course);
                Log.e("COURSE",key+" "+title+" "+url+" " + video);
            }
            JSONArray nanoDegs = jsonObj.getJSONArray(Constants.Udacity.COURSES_FIELD);
            degTitles = new String[nanoDegs.length()];
            for (int i = 0; i < nanoDegs.length(); i++) {
                JSONObject c = nanoDegs.getJSONObject(i);
                //JSONArray in = c.getJSONArray(Constants.Udacity.INSTRUCTORS);
                //JSONObject instr = c.getJSONObject(Constants.Udacity.INSTRUCTORS);
                //Log.e("INSTRUCTOR",in.toString());
                String key = c.getString(Constants.Udacity.UDA_PARAM_COURSE_KEY);
                String title = c.getString(Constants.Udacity.UDA_PARAM_TITLE);
                String subtitle = c.getString(Constants.Udacity.UDA_PARAM_SUBTITLE);
                String image = c.getString(Constants.Udacity.UDA_PARAM_IMAGE);
                String projname = c.getString(Constants.Udacity.UDA_PARAM_PROJECTNAME);
                String level = c.getString(Constants.Udacity.UDA_PARAM_LEVEL);
                String shortsum = c.getString(Constants.Udacity.UDA_PARAM_SHORTSUM);
                String summary = c.getString(Constants.Udacity.UDA_PARAM_SUMMARY);
                JSONObject vlink = c.getJSONObject(Constants.Udacity.UDA_PARAM_TEASER_VID);

                String video = vlink.getString(Constants.Udacity.UDA_PARAM_VIDEO);
                String featured = c.getString(Constants.Udacity.UDA_PARAM_FEATURED);
                String req = c.getString(Constants.Udacity.UDA_PARAM_REQ);
                String newstat = c.getString(Constants.Udacity.UDA_PARAM_NEW);
                String url = c.getString(Constants.Udacity.UDA_PARAM_LINK);

                degTitles[i] = title;
                UdacityCourse course = new UdacityCourse();
                course.setKey(key.trim());
                course.setFeatured(Boolean.getBoolean(featured.trim()));
                course.setTitle(title.trim());
                course.setSubtitle(subtitle.trim());
                course.setImage_url(image);
                course.setHome_url(url);
                course.setProject_name(projname.trim());
                course.setLevel(level.trim());
                course.setShort_sum(shortsum.trim());
                course.setReq(req.trim());
                course.setNew_course(Boolean.getBoolean(newstat));
                course.setVideo_url(video);
                course.setSummary(summary.trim());
                DBHelper dbHelper = new DBHelper(getApplicationContext());
                dbHelper.addUdacityDCourse(course);
                Log.e("COURSE", key + " " + title + " " + url + " " + video);

            }

            titles = new String[degTitles.length + courseTitles.length];
            System.arraycopy(courseTitles, 0, titles, 0, courseTitles.length);
            System.arraycopy(degTitles, courseTitles.length + 1, titles, 0, degTitles.length);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return titles;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
package com.shwavan.mooccatalog.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.shwavan.mooccatalog.Constants;
import com.shwavan.mooccatalog.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;


/**
 * Created by
 * GANGESHWAR on 22-02-2015.
 */
public class FetchCourseList extends AsyncTask<Void, Void, Void> {

    InputStream inputStream = null;
    String result = "";

    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(Void... params) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(Constants.Udacity.BASE_URL, ServiceHandler.GET);

        Log.d("Response: ", "--> " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray courses = jsonObj.getJSONArray(Constants.Udacity.COURSES_FIELD);

                // looping through All Contacts
                for (int i = 0; i < courses.length(); i++) {
                    JSONObject c = courses.getJSONObject(i);
                    JSONArray in = c.getJSONArray(Constants.Udacity.INSTRUCTORS);
                    //JSONObject instr = c.getJSONObject(Constants.Udacity.INSTRUCTORS);
                    Log.e("INSTRUCTOR",in.toString());
                    String key = c.getString(Constants.Udacity.UDA_PARAM_COURSE_KEY);
                    String title = c.getString(Constants.Udacity.UDA_PARAM_TITLE);
                    String url = c.getString(Constants.Udacity.UDA_PARAM_LINK);

                    Log.e("COURSE",key+" "+title+" "+url+" ");

                    // tmp hashmap for single contact
                    HashMap<String, String> contact = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    //contact.put(TAG_ID, id);
                    //contact.put(TAG_NAME, name);
                    //contact.put(TAG_EMAIL, email);
                    //contact.put(TAG_PHONE_MOBILE, mobile);

                    // adding contact to contact list
                    //contactList.add(contact);
                }
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

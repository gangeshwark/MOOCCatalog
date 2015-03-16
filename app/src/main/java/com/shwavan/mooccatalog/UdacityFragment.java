package com.shwavan.mooccatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.shwavan.mooccatalog.adapter.UdacityCourseAdapter;
import com.shwavan.mooccatalog.db.DBHelper;
import com.shwavan.mooccatalog.models.UdacityCourse;
import com.shwavan.mooccatalog.receivers.DownloadResultReceiver;
import com.shwavan.mooccatalog.services.FetchCourseListService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;


public class UdacityFragment extends Fragment implements DownloadResultReceiver.Receiver {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private DownloadResultReceiver mReceiver;
    private LinearLayoutManager layoutManager;

    public UdacityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //new FetchCourseList().execute();
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        //listView = (ListView) rootView.findViewById(R.id.listView);
        DBHelper helper = new DBHelper(getActivity());
        List<UdacityCourse> list = helper.getUdacityCourseList();
        UdacityCourseAdapter adapter = new UdacityCourseAdapter(list);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings :
                break;
            case R.id.load:
                startSer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void startSer(){
        /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), FetchCourseListService.class);

        /* Send optional extras to Download IntentService */

        intent.putExtra("url", Constants.Udacity.BASE_URL);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        getActivity().startService(intent);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
    UdacityCourseAdapter adapter;
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case FetchCourseListService.STATUS_RUNNING:
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                break;
            case FetchCourseListService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                progressDialog.dismiss();
                String[] results = resultData.getStringArray("result");

                /* Update ListView with result */
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_2,
                        android.R.id.text1,
                        results);
                DBHelper helper = new DBHelper(getActivity());
                List<UdacityCourse> list = helper.getUdacityCourseList();

                adapter = new UdacityCourseAdapter(list);
                recyclerView.setAdapter(adapter);
                break;
            case FetchCourseListService.STATUS_ERROR:
                /* Handle the error */
                progressDialog.dismiss();
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                break;
        }
    }

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
                        HashMap<String, String> coursesList = new HashMap<String, String>();

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


}
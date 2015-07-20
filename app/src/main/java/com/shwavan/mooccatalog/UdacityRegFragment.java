package com.shwavan.mooccatalog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shwavan.mooccatalog.adapter.UdacityCourseAdapter;
import com.shwavan.mooccatalog.db.DBHelper;
import com.shwavan.mooccatalog.models.UdacityCourse;
import com.shwavan.mooccatalog.receivers.DownloadResultReceiver;
import com.shwavan.mooccatalog.services.FetchCourseListService;

import java.util.List;

/*
In this fragment, I'm not using Loader because the views' data is downloaded from the internet and is being processed
in the background service which is a separate Thread other than the UI thread. And then i use an AsyncTask to move the data to
the views.
*/

public class UdacityRegFragment extends Fragment implements DownloadResultReceiver.Receiver {

    RecyclerView recyclerView, recyclerView1, recyclerView2;
    ProgressDialog progressDialog;
    int mode;
    UdacityCourseAdapter adapter;
    private DownloadResultReceiver mReceiver;
    private LinearLayoutManager layoutManager;

    public UdacityRegFragment() {
        // Required empty public constructor
    }

    public static UdacityRegFragment newInstance(int mode) {
        UdacityRegFragment f = new UdacityRegFragment();
        Bundle bdl = new Bundle(2);
        bdl.putInt("MODE", mode);
        f.setArguments(bdl);
        return f;
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //shows Gridview on Tablets and Listview on Phones
        if (isTablet(getActivity())) {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            layoutManager = new GridLayoutManager(getActivity(), 2);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        Load load = new Load();
        load.execute();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
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
        mode = getArguments().getInt("MODE");
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("UdacityCatalog", Context.MODE_PRIVATE);
        Boolean str = sharedpreferences.getBoolean("first", true);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (str) {
            startSer();
            editor.putBoolean("first", false);
            editor.apply();
        }
    }

    void startSer() {
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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case FetchCourseListService.STATUS_RUNNING:
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Downloading...");
                progressDialog.show();
                break;
            case FetchCourseListService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                progressDialog.dismiss();
                String[] results = resultData.getStringArray("result");

                /* Update ListView with result */
                DBHelper helper = new DBHelper(getActivity());
                List<UdacityCourse> list;
                if (mode == 1) {
                    list = helper.getUdacityRCourseList();

                } else if (mode == 2) {
                    list = helper.getUdacityDCourseList();

                } else {
                    list = helper.getUdacityCourseList();

                }

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

    /* Used AsyncTask instead of Loader since its just a list that is being populated.
       downloading the content from the internet is done in the service.
    */
    class Load extends AsyncTask<Void, Void, List<UdacityCourse>> {


        @Override
        protected List<UdacityCourse> doInBackground(Void... params) {
            DBHelper helper = new DBHelper(getActivity());
            List<UdacityCourse> list;
            if (mode == 1) {
                list = helper.getUdacityRCourseList();

            } else if (mode == 2) {
                list = helper.getUdacityDCourseList();

            } else {
                list = helper.getUdacityCourseList();

            }

            return list;
        }

        @Override
        protected void onPostExecute(List<UdacityCourse> list) {

            adapter = new UdacityCourseAdapter(list);
            recyclerView.setAdapter(adapter);

        }
    }
}
package com.shwavan.mooccatalog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;


public class UdacityRegFragment extends Fragment implements DownloadResultReceiver.Receiver {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    int mode;
    UdacityCourseAdapter adapter;
    private DownloadResultReceiver mReceiver;
    private LinearLayoutManager layoutManager;

    public UdacityRegFragment() {
        // Required empty public constructor
    }

    public UdacityRegFragment(int mode) {
        // Required empty public constructor
        this.mode = mode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //new FetchCourseList().execute();
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        //listView = (ListView) rootView.findViewById(R.id.listView);
        DBHelper helper = new DBHelper(getActivity());
        List<UdacityCourse> list;
        if (mode == 1) {
            list = helper.getUdacityCourseList();
        } else if (mode == 2) {
            list = helper.getUdacityCourseList();
        } else {
            list = helper.getUdacityCourseList();
        }

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
                List<UdacityCourse> list;
                if (mode == 1) {
                    list = helper.getUdacityCourseList();
                } else if (mode == 2) {
                    list = helper.getUdacityCourseList();
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

}
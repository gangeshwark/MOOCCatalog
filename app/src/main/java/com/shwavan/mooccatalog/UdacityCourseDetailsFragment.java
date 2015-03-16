package com.shwavan.mooccatalog;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shwavan.mooccatalog.db.DBHelper;
import com.shwavan.mooccatalog.provider.UdaCourseContentProvider;

/**
 * Created by GANGESHWAR on 05-03-2015.
 */
public class UdacityCourseDetailsFragment extends Fragment {

    View rootView;
    String id;
    TextView courseid, desc;
    public UdacityCourseDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_udacity_course_details, container, false);
        Intent i = getActivity().getIntent();
        id = i.getStringExtra("id");
        courseid = (TextView) rootView.findViewById(R.id.tv);
        desc = (TextView) rootView.findViewById(R.id.desc);
        init();
        return rootView;
    }

    public void init(){
        String[] projection = {
                DBHelper.KEY_ID,
                DBHelper.KEY_CID,
                DBHelper.KEY_TITLE,
                DBHelper.KEY_SUBTITLE,
                DBHelper.KEY_SHORT_SUM,
                DBHelper.KEY_SUM ,
                DBHelper.KEY_IMAGE,
                DBHelper.KEY_VIDEO,
                DBHelper.KEY_HOMEPAGE,
                DBHelper.KEY_NEW,
                DBHelper.KEY_LEVEL,
                DBHelper.KEY_FEATURED,
                DBHelper.KEY_REQ,
                DBHelper.KEY_NOINSTRUCTORS,
                DBHelper.KEY_PROJECT_NAME,
                DBHelper.KEY_UPDATED_ON,
        };
        Uri uri = Uri.parse(UdaCourseContentProvider.CONTENT_URI + "/" + id);

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null,
                null);
        if(cursor!=null){
            cursor.moveToFirst();
            courseid.setText(cursor.getString(1));
            desc.setText(cursor.getString(5));
            cursor.close();
        }
    }
}
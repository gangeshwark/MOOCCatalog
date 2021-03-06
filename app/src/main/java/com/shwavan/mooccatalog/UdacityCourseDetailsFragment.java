package com.shwavan.mooccatalog;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shwavan.mooccatalog.db.DBHelper;
import com.shwavan.mooccatalog.provider.UdaCourseContentProvider;

/**
 * Created by GANGESHWAR on 05-03-2015.
 */
public class UdacityCourseDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADERID = 0;
    View rootView;
    String id;
    TextView courseid, desc, courseSub, coursetit, reqknow, projectname;
    Button link;
    ImageView imageView;
    LinearLayout linearLayout;
    Uri uri;
    String[] projection = {
            DBHelper.KEY_ID,//0
            DBHelper.KEY_CID,//1
            DBHelper.KEY_TITLE,//2
            DBHelper.KEY_SUBTITLE,//3
            DBHelper.KEY_SHORT_SUM,//4
            DBHelper.KEY_SUM,//5
            DBHelper.KEY_IMAGE,//6
            DBHelper.KEY_VIDEO,//7
            DBHelper.KEY_HOMEPAGE,//8
            DBHelper.KEY_NEW,//9
            DBHelper.KEY_LEVEL,//10
            DBHelper.KEY_FEATURED,//11
            DBHelper.KEY_REQ,//12
            DBHelper.KEY_NOINSTRUCTORS,//13
            DBHelper.KEY_PROJECT_NAME,//14
            DBHelper.KEY_UPDATED_ON,//15
    };
    private ImageLoadingListener animateFirstListener;

    public UdacityCourseDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_udacity_course_details, container, false);
        animateFirstListener = new SimpleImageLoadingListener();
        linearLayout = (LinearLayout) rootView.findViewById(R.id.prolay);
        Intent i = getActivity().getIntent();
        id = i.getStringExtra("id");
        coursetit = (TextView) rootView.findViewById(R.id.course_title);
        courseid = (TextView) rootView.findViewById(R.id.course_id);
        desc = (TextView) rootView.findViewById(R.id.desc);
        link = (Button) rootView.findViewById(R.id.link);
        courseSub = (TextView) rootView.findViewById(R.id.course_subt);
        reqknow = (TextView) rootView.findViewById(R.id.knowledge);
        projectname = (TextView) rootView.findViewById(R.id.projectname);
        imageView = (ImageView) rootView.findViewById(R.id.courseImg);
        uri = Uri.parse(UdaCourseContentProvider.CONTENT_URI + "/" + id);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADERID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),   // Parent activity context
                uri,            // Uri to query
                projection,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            if (getActivity().getActionBar() != null)
                getActivity().setTitle(cursor.getString(2));
            if (cursor.getString(1).startsWith("nd")) {
                courseid.setText("Course Key : " + cursor.getString(1) + " - Nanodegree");
            } else {
                courseid.setText("Course Key : " + cursor.getString(1));
            }

            coursetit.setText(cursor.getString(2));
            courseSub.setText(cursor.getString(3));
            if (!cursor.getString(14).equals(""))
                projectname.setText(projectname.getText() + " : " + cursor.getString(14));
            else
                linearLayout.setVisibility(View.GONE);
            desc.setText(android.text.Html.fromHtml(cursor.getString(5)));
            reqknow.setText(android.text.Html.fromHtml(cursor.getString(12)));

            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_empty)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(5))
                    .build();
            ImageLoader.getInstance().displayImage(cursor.getString(6), imageView, options, animateFirstListener);
            link.setText("Visit Course");
            final String url = cursor.getString(8);
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            //cursor.close();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }
}
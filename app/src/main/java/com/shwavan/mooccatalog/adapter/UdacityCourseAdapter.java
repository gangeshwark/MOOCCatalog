package com.shwavan.mooccatalog.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shwavan.mooccatalog.R;
import com.shwavan.mooccatalog.UdacityCourseDetailsActivity;
import com.shwavan.mooccatalog.models.UdacityCourse;

import java.util.List;

/**
 * Created by GANGESHWAR on 05-03-2015.
 */
public class UdacityCourseAdapter extends RecyclerView.Adapter<UdacityCourseAdapter.UdacityCourseViewHolder> {

    private List<UdacityCourse> udacityCourseList ;
    private ImageLoadingListener animateFirstListener = new SimpleImageLoadingListener();

    public UdacityCourseAdapter(List<UdacityCourse> udacityCourseList) {
        this.udacityCourseList = udacityCourseList;
    }

    @Override
    public UdacityCourseViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cards_layout, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), UdacityCourseDetailsActivity.class);
                TextView tv = (TextView) v.findViewById(R.id.course_id);
                i.putExtra("id",tv.getText().toString());
                parent.getContext().startActivity(i);
            }
        });
        return new UdacityCourseViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(UdacityCourseViewHolder holder, int position) {
        UdacityCourse course = udacityCourseList.get(position);
        holder.title.setText(course.getTitle());
        holder.subtitle.setText(course.getSubtitle());
        holder.id.setText(course.getKey());
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
        ImageLoader.getInstance().displayImage(course.getImage_url(), holder.imageView, options, animateFirstListener);

    }

    @Override
    public int getItemCount() {
        return udacityCourseList.size();
    }

    public static class UdacityCourseViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle, id;
        ImageView imageView ;

        public UdacityCourseViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.course_id);
            title = (TextView)itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtit);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }
}

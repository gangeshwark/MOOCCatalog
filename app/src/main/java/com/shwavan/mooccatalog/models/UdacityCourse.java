package com.shwavan.mooccatalog.models;

/**
 * Created by GANGESHWAR on 03-03-2015.
 */
public class UdacityCourse {

    long _id;
    String key;
    String subtitle;
    String title;
    String image_url;
    String summary;
    boolean featured;
    String video_url;
    String req;
    boolean new_course ;
    String home_url;
    String short_sum;
    String level;
    String no_of_instructors;
    String project_name;
    String updated_on;
    public UdacityCourse(){

    }
    public UdacityCourse(String key, String subtitle, String title, String image_url,
                         boolean featured, String video_url, boolean new_course, String req,
                         String home_url, String short_sum, String level, String no_of_instructors, String project_name) {

        this.key = key;
        this.subtitle = subtitle;
        this.title = title;
        this.image_url = image_url;
        this.featured = featured;
        this.video_url = video_url;
        this.new_course = new_course;
        this.req = req;
        this.home_url = home_url;
        this.short_sum = short_sum;
        this.level = level;
        this.no_of_instructors = no_of_instructors;
        this.project_name = project_name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public boolean getNew_course() {
        return new_course;
    }

    public void setNew_course(boolean new_course) {
        this.new_course = new_course;
    }

    public String getHome_url() {
        return home_url;
    }

    public void setHome_url(String home_url) {
        this.home_url = home_url;
    }

    public String getShort_sum() {
        return short_sum;
    }

    public void setShort_sum(String short_sum) {
        this.short_sum = short_sum;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNo_of_instructors() {
        return no_of_instructors;
    }

    public void setNo_of_instructors(String no_of_instructors) {
        this.no_of_instructors = no_of_instructors;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}

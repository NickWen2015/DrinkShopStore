package drinkshop.cp102.drinkshopstore.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class News implements Serializable {

    private int activity_id;
    private String activity_name;
    private String activity_date_start;
    private String activity_date_end;
    private String activity_desc;
    private int activity_sort;
    private String activity_status;

    public News(int id, String activityName, String activityDateStart, String activityDateEnd){
        this.activity_id = id;
        this.activity_name = activityName;
        this.activity_date_start = activityDateStart;
        this.activity_date_end = activityDateEnd;
    }

    public News(int id){
        this.activity_id = id;

    }

    public int getActivity_id() {
        return activity_id;
    }
    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }
    public String getActivity_name() {
        return activity_name;
    }
    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }
    public String getActivity_date_start() {
        return activity_date_start;
    }
    public void setActivity_date_start(String activity_date_start) {
        this.activity_date_start = activity_date_start;
    }
    public String getActivity_date_end() {
        return activity_date_end;
    }
    public void setActivity_date_end(String activity_date_end) {
        this.activity_date_end = activity_date_end;
    }
    public String getActivity_desc() {
        return activity_desc;
    }
    public void setActivity_desc(String activity_desc) {
        this.activity_desc = activity_desc;
    }
    public int getActivity_sort() {
        return activity_sort;
    }
    public void setActivity_sort(int activity_sort) {
        this.activity_sort = activity_sort;
    }
    public String getActivity_status() {
        return activity_status;
    }
    public void setActivity_status(String activity_status) {
        this.activity_status = activity_status;
    }
}

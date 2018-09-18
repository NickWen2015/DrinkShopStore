package drinkshop.cp102.drinkshopstore;

import java.io.Serializable;

@SuppressWarnings("serial")
public class News implements Serializable {

    private int id;
    private String activityName;
    private String activityDateStart;
    private String activityDateEnd;

    public News(int id, String activityName, String activityDateStart, String activityDateEnd) {
        this.id = id;
        this.activityName = activityName;
        this.activityDateStart = activityDateStart;
        this.activityDateEnd = activityDateEnd;

    }

    @Override
    public boolean equals(Object obj) {   //設定刪除的依據
        return this.id == ((News)obj).id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDateStart() {
        return activityDateStart;
    }

    public void setActivityDateStart(String activityDateStart) {
        this.activityDateStart = activityDateStart;
    }

    public String getActivityDateEnd() {
        return activityDateEnd;
    }

    public void setActivityDateEnd(String activityDateEnd) {
        this.activityDateEnd = activityDateEnd;
    }


}

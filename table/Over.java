package com.aktivo.table;

import com.aktivo.db.Aktivodb;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by techiestown on 10/1/18.
 */
@Table(name = Over.TABLE_NAME, database = Aktivodb.class)
public class Over extends BaseModel{
    public static final String TABLE_NAME = "over";

    @Column
    @PrimaryKey
    private String _id;
    @Column
    private String company_id;
    @Column
    private String category;
    @Column
    private String title;

    @Column
    private String startdate;
    @Column
    private String days;

    @Column
    private String video_url;

    @Column
    private String description;


    private List<Competitors> competitors;

    @Column
    private String target;
    @Column
    private String dayRange;
    @Column
    private String enddate;
    @Column
    private String photo;


    public static List<Over> getOverList(){
        List<Over> overList = SQLite.select().distinct().from(Over.class).queryList();
        if(overList!=null){
            return overList;
        }
        return null;
    }
    public static Over getOverDetail(String id){
        Over over = SQLite.select().distinct().from(Over.class).where(Over_Table._id.eq(id)).querySingle();
        if(over!=null){
            return over;
        }
        return null;
    }
    public String getCompany_id ()
    {
        return company_id;
    }

    public void setCompany_id (String company_id)
    {
        this.company_id = company_id;
    }

    public String getCategory ()
    {
        return category;
    }

    public void setCategory (String category)
    {
        this.category = category;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getStartdate ()
    {
        return startdate;
    }

    public void setStartdate (String startdate)
    {
        this.startdate = startdate;
    }

    public String getDays ()
    {
        return days;
    }

    public void setDays (String days)
    {
        this.days = days;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getVideo_url ()
    {
        return video_url;
    }

    public void setVideo_url (String video_url)
    {
        this.video_url = video_url;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public List<Competitors> getCompetitors ()
    {
        return competitors;
    }

    public void setCompetitors (List<Competitors> competitors)
    {
        this.competitors = competitors;
    }

    public String getTarget ()
    {
        return target;
    }

    public void setTarget (String target)
    {
        this.target = target;
    }

    public String getDayRange ()
    {
        return dayRange;
    }

    public void setDayRange (String dayRange)
    {
        this.dayRange = dayRange;
    }

    public String getEnddate ()
    {
        return enddate;
    }

    public void setEnddate (String enddate)
    {
        this.enddate = enddate;
    }

    public String getPhoto ()
    {
        return photo;
    }

    public void setPhoto (String photo)
    {
        this.photo = photo;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [company_id = "+company_id+", category = "+category+", title = "+title+", startdate = "+startdate+", days = "+days+", _id = "+_id+", video_url = "+video_url+", description = "+description+", competitors = "+competitors+", target = "+target+", dayRange = "+dayRange+", enddate = "+enddate+", photo = "+photo+"]";
    }
}

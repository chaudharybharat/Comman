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
@Table(name = Competitors.TABLE_NAME, database = Aktivodb.class)
public class Competitors extends BaseModel {

    public static final String TABLE_NAME = "competitors";

    @Column
    @PrimaryKey()
    private String uniqueid;

    @Column
    private String id;

    @Column
    private String name;
    @Column
    private String achieve;

    public static List<Competitors> getCompetitorsList(String id){
        List<Competitors> almostOverList = SQLite.select().distinct().from(Competitors.class).where(Competitors_Table.id.eq(id)).queryList();
        if(almostOverList!=null){
            return almostOverList;
        }
        return null;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getAchieve ()
    {
        return achieve;
    }

    public void setAchieve (String achieve)
    {
        this.achieve = achieve;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", achieve = "+achieve+"]";
    }
}

package se.miun.kran1800.dt031g.bathingsites;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

// BathingSite object for use with database
// Implements serializable to enable passing in bundle.

@Entity(tableName = "bathing_site")
public class BathingSite implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String description;
    public String address;
    public double latitude;
    public double longitude;
    public float grade;
    public double waterTemp;
    public String dateForTemp;

    //Constructor, primary key is autogenerated id.
    @Ignore
    public BathingSite(int id, String name, String address, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        // Set default value to rest of variables.
        this.description = "";
        this.grade = 0;
        this.waterTemp = 0;
        this.dateForTemp = "";
    }

    // Constructor, primary key is autogenerated id.
    public BathingSite(int id, String name, String description, String address,
                       double latitude, double longitude, float grade,
                        double waterTemp, String dateForTemp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.grade = grade;
        this.waterTemp = waterTemp;
        this.dateForTemp = dateForTemp;
    }
}

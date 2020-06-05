package se.miun.kran1800.dt031g.bathingsites;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BathingSiteDao {
    // Add bathing site to database.
    @Insert
    public void insertBathingSite(BathingSite bathingSite);

    // Get total number of bathing sites in DB.
    @Query("SELECT COUNT(*) FROM bathing_site")
    public int getTotalBathingSites();

    // Check if bathing site already exist by comparing lat/lon.
    @Query("SELECT COUNT(*) FROM bathing_site WHERE latitude = :lat AND longitude = :lon")
    public int checkForDuplicate(double lat, double lon);

    // Return a random bathing site.
    @Query("SELECT * FROM bathing_site ORDER BY RANDOM() LIMIT 1")
    public BathingSite getRandomBathingSite();
}

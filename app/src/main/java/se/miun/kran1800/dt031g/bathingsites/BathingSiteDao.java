package se.miun.kran1800.dt031g.bathingsites;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BathingSiteDao {
    @Insert
    public void insertBathingSite(BathingSite bathingSite);

    @Query("SELECT COUNT(*) FROM bathing_site")
    public int getTotalBathingSites();

    @Query("SELECT COUNT(*) FROM bathing_site WHERE latitude = :lat AND longitude = :lon")
    public int checkForDuplicate(double lat, double lon);

}

package se.miun.kran1800.dt031g.bathingsites;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface BathingSiteDao {
    @Insert
    public void insertPhoneCall(BathingSite bathingSite);

    @Query("SELECT * FROM bathing_site")
    public BathingSite[] getBathingSites();
}

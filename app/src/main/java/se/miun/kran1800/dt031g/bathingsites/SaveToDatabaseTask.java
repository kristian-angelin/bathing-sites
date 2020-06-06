package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class SaveToDatabaseTask extends AsyncTaskLoader<Boolean> {

    private BathingSite bathingSite;

    // Constructor for saving bathingSite object to database.
    SaveToDatabaseTask(@NonNull Context context, BathingSite bathingSite) {
        super(context);
        this.bathingSite = bathingSite;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    // Runs the save query and return TRUE/FALSE if it was saved or not.
    @Nullable
    @Override
    public Boolean loadInBackground() {
        BathingSiteDatabase database = BathingSiteDatabase.getInstance(getContext());
        // TODO: Make a better use then 1
        // Check if lat/lon are 0, use this value as NULL. (Its the location of NULL ISLAND in the ocean).
        if(bathingSite.latitude != 0 && bathingSite.longitude != 0) {
            if(database.bathingSiteDao().checkForDuplicate(bathingSite.latitude, bathingSite.longitude) == 1) {
                return Boolean.FALSE;
            }
        }
        database.bathingSiteDao().insertBathingSite(bathingSite);
        return Boolean.TRUE;
    }
}

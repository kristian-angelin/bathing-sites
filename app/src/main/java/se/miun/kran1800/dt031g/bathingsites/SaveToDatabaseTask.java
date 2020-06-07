package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

/**
 * AsyncTaskLoader for saving a bathing site to database.
 */
public class SaveToDatabaseTask extends AsyncTaskLoader<Boolean> {

    private final static int TRUE_INT = 1;
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

        // Check if lat/lon are 0, use this value as NULL, to allow for duplicates. Is in the middle of the ocean.
        if(bathingSite.latitude != 0 && bathingSite.longitude != 0) {
            if(database.bathingSiteDao().checkForDuplicate(bathingSite.latitude, bathingSite.longitude) == TRUE_INT) {
                return false;
            }
        }
        database.bathingSiteDao().insertBathingSite(bathingSite);
        return true;
    }
}

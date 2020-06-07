package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

/**
 * AsyncTaskLoader for getting random bathing site from database.
 */
public class LoadRandomBathingSiteTask extends AsyncTaskLoader<BathingSite> {

    public LoadRandomBathingSiteTask(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public BathingSite loadInBackground() {
        BathingSiteDatabase database = BathingSiteDatabase.getInstance(getContext());
        return database.bathingSiteDao().getRandomBathingSite();
    }
}

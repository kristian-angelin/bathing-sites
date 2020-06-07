package se.miun.kran1800.dt031g.bathingsites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Fragment holding BathingSiteView.
 * Handles the creation of dialog showing random bathing site from database.
 * Implements LoaderCallbacks to create asyncTaskLoader
 */
public class BathingSitesFragment extends Fragment implements LoaderManager.LoaderCallbacks<BathingSite> {

    public BathingSitesFragment() {}    // Empty constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        return inflater.inflate(R.layout.fragment_bathing_sites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView image = getView().findViewById(R.id.bath_image);
        // Add listener for clicks on view.
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomBathingSite();
            }
        });
    }

    // Show random bathing site dialog by calling AsyncTaskLoader
    private void showRandomBathingSite() {
        getLoaderManager().restartLoader(1, null, this);
    }

    //--------------- LoaderCallbacks required methods ---------------------
    @NonNull
    @Override
    public Loader<BathingSite> onCreateLoader(int id, @Nullable Bundle args) {
        return new LoadRandomBathingSiteTask(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<BathingSite> loader, BathingSite bathingSite) {
        // Create and show instance of dialog
        ShowRandomBathingSiteDialog showRandomBathingSiteDialog = ShowRandomBathingSiteDialog.newInstance(bathingSite);
        showRandomBathingSiteDialog.show(getActivity().getSupportFragmentManager(), "showRandomSite");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<BathingSite> loader) {

    }
}

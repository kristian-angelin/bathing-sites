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

public class BathingSitesFragment extends Fragment implements LoaderManager.LoaderCallbacks<BathingSite> {
    /*// TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    public static BathingSitesFragment newInstance(String param1, String param2) {
        BathingSitesFragment fragment = new BathingSitesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/
    public BathingSitesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bathing_sites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView image = getView().findViewById(R.id.bath_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomBathingSite();
            }
        });
    }

    private void showRandomBathingSite() {
        getLoaderManager().restartLoader(1, null, this);
    }


    @NonNull
    @Override
    public Loader<BathingSite> onCreateLoader(int id, @Nullable Bundle args) {
        return new LoadRandomBathingSiteTask(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<BathingSite> loader, BathingSite bathingSite) {
        ShowRandomBathingSiteDialog showRandomBathingSiteDialog = ShowRandomBathingSiteDialog.newInstance(bathingSite);
        showRandomBathingSiteDialog.show(getActivity().getSupportFragmentManager(), "showRandomSite");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<BathingSite> loader) {

    }
}

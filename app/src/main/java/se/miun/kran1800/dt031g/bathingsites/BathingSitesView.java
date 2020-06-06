package se.miun.kran1800.dt031g.bathingsites;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

// View keeping track of number of bathing sites stored in database.
public class BathingSitesView extends ConstraintLayout {

    private int sitesCount = 0; // Default value
    private BathingSiteDatabase database;
    private Activity activity;  // Is used to start the UI thread.

    public BathingSitesView(Context context) {
        super(context);
        init(context, null);
    }

    public BathingSitesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BathingSitesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Load attributes
        activity = (Activity) getContext();
        inflate(context, R.layout.view_bathing_sites, this);
        database = BathingSiteDatabase.getInstance(getContext());
        displaySitesCount();
        updateBathCount();
    }

    private void displaySitesCount() {
        TextView titleText = findViewById(R.id.bathing_title);
        CharSequence bathText = sitesCount + " " + getResources().getString(R.string.bathing_sites);
        titleText.setText(bathText);
    }

    // Creates threads to update bathing sites count from database.
    private void updateBathCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sitesCount = database.bathingSiteDao().getTotalBathingSites();
                 activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        displaySitesCount();
                    }
                });
            }
        }).start();
    }
}

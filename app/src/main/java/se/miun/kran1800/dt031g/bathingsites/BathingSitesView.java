package se.miun.kran1800.dt031g.bathingsites;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * TODO: document your custom view class.
 * TODO: FIX ROTATING SCREEN ISSUE?!
 */
public class BathingSitesView extends ConstraintLayout {

    private int sitesCount = 0;
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
        database = BathingSiteDatabase.getDatabase(getContext());
        displaySitesCount();
        updateBathCount();

        // Setup onclick listener on image.
        ImageView image = findViewById(R.id.bath_image);
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRandomBathingSite();
            }
        });
    }

    private void displaySitesCount() {
        TextView titleText = findViewById(R.id.bathing_title);
        CharSequence bathText = sitesCount + " " + getResources().getString(R.string.bathing_sites);
        titleText.setText(bathText);

    }
    // TODO: probably remove.
    private void setSitesCount(int count) {
        sitesCount = count;
    }

    private int getSitesCount() {
        return sitesCount;
    }

    // Show alert dialog with info about a random bathing site.
    public void showRandomBathingSite() {
        getRandomBathingSite();
    }

    // Creates threads to get from database and update UI thread.
    private void getRandomBathingSite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final BathingSite randomSite = database.bathingSiteDao().getRandomBathingSite();
                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        showBathingSiteDialog(randomSite);
                    }
                });
            }
        }).start();
    }

    // Create an alert dialog with info about a bathingSite.
    private void showBathingSiteDialog (BathingSite bathingSite) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        // Add text to dialog
        alertDialog.setTitle(activity.getString(R.string.title_show_bathing_site));
        if(bathingSite != null) {
            alertDialog.setMessage(activity.getString(R.string.form_name) + ": " + bathingSite.name + "\n" +
                    activity.getString(R.string.form_description) + ": " + bathingSite.description + "\n" +
                    activity.getString(R.string.form_address) + ": " + bathingSite.address + "\n" +
                    activity.getString(R.string.form_latitude) + ": " + bathingSite.latitude + "\n" +
                    activity.getString(R.string.form_longitude) + ": " + bathingSite.longitude + "\n" +
                    activity.getString(R.string.form_grade) + ": " + bathingSite.grade + "\n" +
                    activity.getString(R.string.form_water_temp) + ": " + bathingSite.waterTemp + "\n" +
                    activity.getString(R.string.form_date_for_temp) + ": " + bathingSite.dateForTemp);
        } else
        alertDialog.setMessage(activity.getString(R.string.error_no_bathing_sites));
        // Create the ok button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            activity.getString(R.string.button_ok),
                            new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });

        alertDialog.show();
    }

    // Creates threads to update count on page.
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

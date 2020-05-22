package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * TODO: document your custom view class.
 */
public class BathingSitesView extends ConstraintLayout {

    private int sitesCount = 0;

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
        inflate(context, R.layout.view_bathing_sites, this);
        displaySitesCount();
    }

    private void displaySitesCount() {
        TextView titleText = findViewById(R.id.bathing_title);
        CharSequence bathText = sitesCount + " " + getResources().getString(R.string.bathing_sites);
        titleText.setText(bathText);
    }

    private void setSitesCount(int count) {
        sitesCount = count;
    }

    private int getSitesCount() {
        return sitesCount;
    }
}

package se.miun.kran1800.dt031g.bathingsites;

import android.content.Context;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * TODO: document your custom view class.
 */
public class BathingSitesView extends ConstraintLayout {

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
        inflate(context, R.layout.bathing_sites_view, this);
    }
}

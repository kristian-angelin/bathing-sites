package se.miun.kran1800.dt031g.bathingsites;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NewBathingSiteActivity extends AppCompatActivity {

    private NewBathingSiteActivityFragment bathingSiteForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bathing_site);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up actionbar
        ActionBar ac = getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);

        initFormFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_bathing_site, menu);

        return true;
    }

    private void initFormFields() {
        bathingSiteForm = (NewBathingSiteActivityFragment) getSupportFragmentManager()
                            .findFragmentById(
                                    R.id.fragment_new_bathing_site);
    }

    // When menu is clicked.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                clearForm();
                return true;
            case R.id.menu_item_save:
                saveForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clearForm() {
        bathingSiteForm.clearForm();
    }

    private void saveForm() {
        bathingSiteForm.saveBatingSite();
    }
}

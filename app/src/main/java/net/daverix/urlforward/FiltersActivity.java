package net.daverix.urlforward;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

/**
 * Created by daverix on 12/19/13.
 */
public class FiltersActivity extends AppCompatActivity implements FiltersFragment.FilterSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filters);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddFilter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_INSERT, UrlFilters.CONTENT_URI));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentFilterList);
        if(fragment == null) {
            fragment = new FiltersFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentFilterList, fragment)
                    .commit();
        }
    }

    @Override
    public void onFilterSelected(long id) {
        startActivity(new Intent(Intent.ACTION_EDIT, Uri.withAppendedPath(UrlFilters.CONTENT_URI, String.valueOf(id))));
    }
}

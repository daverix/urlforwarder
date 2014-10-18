package net.daverix.urlforward;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

/**
 * Created by daverix on 12/19/13.
 */
public class FiltersActivity extends ActionBarActivity implements FiltersFragment.FilterSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filters);
    }

    @Override
    public void onFilterSelected(long id) {
        startActivity(new Intent(Intent.ACTION_EDIT, Uri.withAppendedPath(UrlFilters.CONTENT_URI, String.valueOf(id))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_filters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCreateFilter:
                startActivity(new Intent(Intent.ACTION_INSERT, UrlFilters.CONTENT_URI));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

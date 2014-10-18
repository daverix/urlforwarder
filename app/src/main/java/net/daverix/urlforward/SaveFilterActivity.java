package net.daverix.urlforward;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

/**
 * Created by daverix on 12/28/13.
 */
public class SaveFilterActivity extends ActionBarActivity implements SaveFilterFragment.SaveFilterListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        SaveFilterFragment saveFilterFragment = (SaveFilterFragment) getFragmentManager().findFragmentById(android.R.id.content);

        if(intent != null && Intent.ACTION_INSERT.equals(intent.getAction())) {
            getSupportActionBar().setTitle(R.string.create_filter);

            if(saveFilterFragment == null) {
                saveFilterFragment = SaveFilterFragment.newCreateInstance();
                getFragmentManager().beginTransaction().add(android.R.id.content, saveFilterFragment).commit();
            }
        } else if(intent !=null && Intent.ACTION_EDIT.equals(intent.getAction())) {
            getSupportActionBar().setTitle(R.string.edit_filter);

            if(saveFilterFragment == null) {
                saveFilterFragment = SaveFilterFragment.newUpdateInstance(intent.getData());
                getFragmentManager().beginTransaction().add(android.R.id.content, saveFilterFragment).commit();
            }
        }
    }

    @Override
    public void onCreateFilter(String title, String filter, String replacableText, long created) {
        LinkFilter linkFilter = new LinkFilter(title, filter, replacableText, created, System.currentTimeMillis());
        Intent saveIntent = new Intent(this, FilterService.class);
        saveIntent.setAction(Intent.ACTION_INSERT);
        saveIntent.putExtra(FilterService.EXTRA_LINK_FILTER, linkFilter);
        startService(saveIntent);

        finish();
    }

    @Override
    public void onUpdateFilter(Uri uri, String title, String filter, String replacableText, long created) {
        LinkFilter linkFilter = new LinkFilter(title, filter, replacableText, created, System.currentTimeMillis());
        Intent saveIntent = new Intent(this, FilterService.class);
        saveIntent.setAction(Intent.ACTION_EDIT);
        saveIntent.setData(uri);
        saveIntent.putExtra(FilterService.EXTRA_LINK_FILTER, linkFilter);
        startService(saveIntent);

        finish();
    }

    @Override
    public void onDeleteFilter(Uri uri) {
        Intent deleteIntent = new Intent(this, FilterService.class);
        deleteIntent.setAction(Intent.ACTION_DELETE);
        deleteIntent.setData(uri);
        startService(deleteIntent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Intent.ACTION_VIEW, UrlFilters.CONTENT_URI);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package net.daverix.urlforward;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by daverix on 12/28/13.
 */
public class SaveFilterActivity extends AppCompatActivity {
    private SaveFilterFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_save_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveFilterActivity.this, FiltersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        fragment = (SaveFilterFragment) getSupportFragmentManager().findFragmentById(R.id.saveFilterFragment);

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_INSERT.equals(intent.getAction())) {
            toolbar.setTitle(R.string.create_filter);
            toolbar.inflateMenu(R.menu.fragment_save_filter);

            if (fragment == null) {
                fragment = SaveFilterFragment.newCreateInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.saveFilterFragment, fragment).commit();
            }
        }
        else if(intent != null && Intent.ACTION_EDIT.equals(intent.getAction())) {
            toolbar.setTitle(R.string.edit_filter);
            toolbar.inflateMenu(R.menu.fragment_edit_filter);

            if (fragment == null) {
                fragment = SaveFilterFragment.newUpdateInstance(intent.getData());
                getSupportFragmentManager().beginTransaction().add(R.id.saveFilterFragment, fragment).commit();
            }
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = getIntent();

                switch (item.getItemId()) {
                    case R.id.menuSave:
                        if (intent != null && Intent.ACTION_INSERT.equals(intent.getAction())) {
                            createFilter(fragment.getEditTitleText(),
                                    fragment.getEditFilterText(),
                                    fragment.getReplacableText(),
                                    fragment.getCreated());
                        } else if (intent != null && Intent.ACTION_EDIT.equals(intent.getAction())) {
                            updateFilter(intent.getData(),
                                    fragment.getEditTitleText(),
                                    fragment.getEditFilterText(),
                                    fragment.getReplacableText(),
                                    fragment.getCreated());
                        }
                        return true;
                    case R.id.menuDelete:
                        deleteFilter(intent.getData());
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void createFilter(String title, String filter, String replacableText, long created) {
        LinkFilter linkFilter = new LinkFilter(title, filter, replacableText, created, System.currentTimeMillis());
        Intent saveIntent = new Intent(this, FilterService.class);
        saveIntent.setAction(Intent.ACTION_INSERT);
        saveIntent.putExtra(FilterService.EXTRA_LINK_FILTER, linkFilter);
        startService(saveIntent);

        finish();
    }

    private void updateFilter(Uri uri, String title, String filter, String replacableText, long created) {
        LinkFilter linkFilter = new LinkFilter(title, filter, replacableText, created, System.currentTimeMillis());
        Intent saveIntent = new Intent(this, FilterService.class);
        saveIntent.setAction(Intent.ACTION_EDIT);
        saveIntent.setData(uri);
        saveIntent.putExtra(FilterService.EXTRA_LINK_FILTER, linkFilter);
        startService(saveIntent);

        finish();
    }

    private void deleteFilter(Uri uri) {
        Intent deleteIntent = new Intent(this, FilterService.class);
        deleteIntent.setAction(Intent.ACTION_DELETE);
        deleteIntent.setData(uri);
        startService(deleteIntent);

        finish();
    }
}

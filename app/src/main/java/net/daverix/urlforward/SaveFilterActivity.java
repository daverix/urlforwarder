package net.daverix.urlforward;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.daverix.urlforward.databinding.SaveFilterActivityBinding;

public class SaveFilterActivity extends AppCompatActivity {
    private SaveFilterFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SaveFilterActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.save_filter_activity);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
            binding.toolbar.setTitle(R.string.create_filter);
            binding.toolbar.inflateMenu(R.menu.fragment_save_filter);

            if (fragment == null) {
                fragment = SaveFilterFragment.newCreateInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.saveFilterFragment, fragment).commit();
            }
        }
        else if(intent != null && Intent.ACTION_EDIT.equals(intent.getAction())) {
            binding.toolbar.setTitle(R.string.edit_filter);
            binding.toolbar.inflateMenu(R.menu.fragment_edit_filter);

            if (fragment == null) {
                fragment = SaveFilterFragment.newUpdateInstance(intent.getData());
                getSupportFragmentManager().beginTransaction().add(R.id.saveFilterFragment, fragment).commit();
            }
        }

        binding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = getIntent();

                switch (item.getItemId()) {
                    case R.id.menuSave:
                        if (intent != null && Intent.ACTION_INSERT.equals(intent.getAction())) {
                            createFilter(fragment.getFilter());
                        } else if (intent != null && Intent.ACTION_EDIT.equals(intent.getAction())) {
                            updateFilter(fragment.getFilter(), intent.getData());
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

    private void createFilter(LinkFilter linkFilter) {
        Intent saveIntent = new Intent(this, FilterService.class);
        saveIntent.setAction(Intent.ACTION_INSERT);
        saveIntent.putExtra(FilterService.EXTRA_LINK_FILTER, linkFilter);
        startService(saveIntent);

        finish();
    }

    private void updateFilter(LinkFilter linkFilter, Uri uri) {
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

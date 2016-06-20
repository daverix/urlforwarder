package net.daverix.urlforward;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.daverix.urlforward.databinding.FiltersActivityBinding;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

public class FiltersActivity extends AppCompatActivity implements FiltersFragment.FilterSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FiltersActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.filters_activity);
        binding.btnAddFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_INSERT, UrlFilters.CONTENT_URI));
            }
        });

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

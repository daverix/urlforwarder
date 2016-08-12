package net.daverix.urlforward;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class FilterRowViewModel extends BaseObservable{
    private final FiltersFragment.FilterSelectedListener listener;
    private String title;
    private String filterUrl;
    private long id;

    public FilterRowViewModel(FiltersFragment.FilterSelectedListener listener, String title, String filterUrl, long id) {
        this.listener = listener;
        this.title = title;
        this.filterUrl = filterUrl;
        this.id = id;
    }

    public void onClick() {
        listener.onFilterSelected(id);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public String getFilterUrl() {
        return filterUrl;
    }
}

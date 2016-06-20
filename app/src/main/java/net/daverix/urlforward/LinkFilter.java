package net.daverix.urlforward;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daverix on 12/23/13.
 */
public class LinkFilter extends BaseObservable implements Parcelable {
    private long id;
    private String title;
    private String filterUrl;
    private String replaceText;
    private long created;
    private long updated;

    public LinkFilter(Parcel in) {
        id = in.readLong();
        title = in.readString();
        filterUrl = in.readString();
        replaceText = in.readString();
        created = in.readLong();
        updated = in.readLong();
    }

    public LinkFilter() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(filterUrl);
        dest.writeString(replaceText);
        dest.writeLong(created);
        dest.writeLong(updated);
    }

    public static final Creator<LinkFilter> CREATOR = new Creator<LinkFilter>() {
        @Override
        public LinkFilter createFromParcel(Parcel source) {
            return new LinkFilter(source);
        }

        @Override
        public LinkFilter[] newArray(int size) {
            return new LinkFilter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Bindable
    public String getFilterUrl() {
        return filterUrl;
    }

    @Bindable
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }

    @Bindable
    public String getReplaceText() {
        return replaceText;
    }

    @Bindable
    public long getCreated() {
        return created;
    }

    @Bindable
    public long getUpdated() {
        return updated;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(net.daverix.urlforward.BR.id);
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(net.daverix.urlforward.BR.title);
    }

    public void setFilterUrl(String filterUrl) {
        this.filterUrl = filterUrl;
        notifyPropertyChanged(net.daverix.urlforward.BR.filterUrl);
    }

    public void setReplaceText(String replaceText) {
        this.replaceText = replaceText;
        notifyPropertyChanged(net.daverix.urlforward.BR.replaceText);
    }

    public void setCreated(long created) {
        this.created = created;
        notifyPropertyChanged(net.daverix.urlforward.BR.created);
    }

    public void setUpdated(long updated) {
        this.updated = updated;
        notifyPropertyChanged(net.daverix.urlforward.BR.updated);
    }
}

package net.daverix.urlforward;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by daverix on 12/23/13.
 */
public class LinkFilter implements Parcelable {
    private long mId;
    private final String mTitle;
    private final String mFilterUrl;
    private final String mReplaceText;
    private final long mCreated;
    private final long mUpdated;

    public LinkFilter(String title, String filterUrl, String replaceText, long created, long updated) {
        mTitle = title;
        mFilterUrl = filterUrl;
        mReplaceText = replaceText;
        mCreated = created;
        mUpdated = updated;
    }

    public LinkFilter(long id, String title, String filterUrl, String replaceText, long created, long updated) {
        mId = id;
        mTitle = title;
        mFilterUrl = filterUrl;
        mReplaceText = replaceText;
        mCreated = created;
        mUpdated = updated;
    }

    public LinkFilter(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mFilterUrl = in.readString();
        mReplaceText = in.readString();
        mCreated = in.readLong();
        mUpdated = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mFilterUrl);
        dest.writeString(mReplaceText);
        dest.writeLong(mCreated);
        dest.writeLong(mUpdated);
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

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getFilterUrl() {
        return mFilterUrl;
    }

    public long getId() {
        return mId;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public String getReplaceText() {
        return mReplaceText;
    }

    public long getCreated() {
        return mCreated;
    }

    public long getUpdated() {
        return mUpdated;
    }
}

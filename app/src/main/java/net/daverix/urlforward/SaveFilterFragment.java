package net.daverix.urlforward;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static net.daverix.urlforward.db.UrlForwarderContract.UrlFilters;

/**
 * Created by daverix on 12/28/13.
 */
public class SaveFilterFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_LOAD_FILTER = 0;
    private static final String ARG_STATE = "state";
    private static final String ARG_URI = "uri";
    private static final int STATE_CREATE = 0;
    private static final int STATE_UPDATE = 1;
    private static final String SAVE_TITLE = "title";
    private static final String SAVE_FILTER = "filter";
    private static final String SAVE_REPLACABLE_TEXT = "replacableText";
    private static final String SAVE_CREATED = "created";

    private EditText mEditTitle;
    private EditText mEditFilter;
    private EditText mEditReplaceText;
    private SaveFilterListener mListener;
    private Uri mUri;
    private int mState;
    private long mCreated;

    public static SaveFilterFragment newCreateInstance() {
        SaveFilterFragment saveFilterFragment = new SaveFilterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATE, STATE_CREATE);
        saveFilterFragment.setArguments(args);
        return saveFilterFragment;
    }

    public static SaveFilterFragment newUpdateInstance(Uri uri) {
        SaveFilterFragment saveFilterFragment = new SaveFilterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_STATE, STATE_UPDATE);
        args.putParcelable(ARG_URI, uri);
        saveFilterFragment.setArguments(args);
        return saveFilterFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (SaveFilterListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle args = getArguments();
        if(args != null) {
            mUri = args.getParcelable(ARG_URI);
            mState = args.getInt(ARG_STATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVE_TITLE, getEditTitleText());
        outState.putString(SAVE_FILTER, getEditFilterText());
        outState.putString(SAVE_REPLACABLE_TEXT, getReplacableText());
        outState.putLong(SAVE_CREATED, mCreated);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_save_filter, container, false);

        if(v != null) {
            mEditTitle = (EditText) v.findViewById(R.id.editTitle);
            mEditFilter = (EditText) v.findViewById(R.id.editFilter);
            mEditReplaceText = (EditText) v.findViewById(R.id.editReplacableText);
            if(mState == STATE_CREATE && savedInstanceState == null) {
                mEditFilter.setText("http://example.com/?url=@url");
                mEditReplaceText.setText("@url");
            }
        }

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mState == STATE_UPDATE && savedInstanceState == null) {
            getLoaderManager().restartLoader(LOADER_LOAD_FILTER, null, this);
        }
        else if(mState == STATE_CREATE && savedInstanceState == null) {
            mCreated = System.currentTimeMillis();
        }
        else {
            mCreated = savedInstanceState.getLong(SAVE_CREATED);
            mEditTitle.setText(savedInstanceState.getString(SAVE_TITLE));
            mEditFilter.setText(savedInstanceState.getString(SAVE_FILTER));
            mEditReplaceText.setText(savedInstanceState.getString(SAVE_REPLACABLE_TEXT));
        }
    }

    public String getEditTitleText() {
        return getNullCheckedString(mEditTitle);
    }

    public String getEditFilterText() {
        return getNullCheckedString(mEditFilter);
    }

    public String getReplacableText() {
        return getNullCheckedString(mEditReplaceText);
    }

    private String getNullCheckedString(EditText editText) {
        if (editText == null) return null;

        Editable editable = editText.getText();
        String text = editable != null ? editable.toString() : null;
        return text != null ? text.trim() : "";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_save_filter, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.menuDelete);
        if(item != null && mState == STATE_UPDATE) {
            item.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSave:
                if(mListener != null && mState == STATE_CREATE) {
                    mListener.onCreateFilter(getEditTitleText(),
                            getEditFilterText(),
                            getReplacableText(),
                            mCreated);
                } else if(mListener != null && mState == STATE_UPDATE) {
                    mListener.onUpdateFilter(mUri,
                            getEditTitleText(),
                            getEditFilterText(),
                            getReplacableText(),
                            mCreated);
                }
                return true;
            case R.id.menuDelete:
                if(mListener != null && mState == STATE_UPDATE) {
                    mListener.onDeleteFilter(mUri);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_LOAD_FILTER:
                return new CursorLoader(getActivity(), mUri, new String[] {
                        UrlFilters.TITLE,
                        UrlFilters.FILTER,
                        UrlFilters.REPLACE_TEXT,
                        UrlFilters.CREATED
                }, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_LOAD_FILTER:
                if(data != null && data.moveToFirst()) {
                    mEditTitle.setText(data.getString(0));
                    mEditFilter.setText(data.getString(1));
                    mEditReplaceText.setText(data.getString(2));
                    mCreated = data.getLong(3);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface SaveFilterListener {
        public void onCreateFilter(String title, String filter, String replacableText, long created);
        public void onUpdateFilter(Uri uri, String title, String filter, String replacableText, long created);
        public void onDeleteFilter(Uri uri);
    }
}

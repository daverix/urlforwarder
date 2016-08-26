package net.daverix.urlforward;


import android.content.ContentValues;
import android.database.Cursor;

public interface LinkFilterMapper {
    String[] getColumns();

    LinkFilter mapFilter(Cursor cursor);

    ContentValues getValues(LinkFilter filter);
}

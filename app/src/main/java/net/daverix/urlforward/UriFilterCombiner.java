package net.daverix.urlforward;

import android.net.Uri;

import java.io.UnsupportedEncodingException;

/**
 * Created by daverix on 12/23/13.
 */
public interface UriFilterCombiner {
    public Uri create(LinkFilter linkFilter, String url) throws UriCombinerException;
}

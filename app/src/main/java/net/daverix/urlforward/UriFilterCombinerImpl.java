package net.daverix.urlforward;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UriFilterCombinerImpl implements UriFilterCombiner {
    @Override
    public Uri create(LinkFilter linkFilter, String url) throws UriCombinerException {
        if(linkFilter == null) throw new IllegalArgumentException("linkFilter is null");
        if(url == null) throw new IllegalArgumentException("url is null");

        try {
            String filterUrl = linkFilter.getFilterUrl();
            String filteredUrl = filterUrl.replace(linkFilter.getReplaceText(), URLEncoder.encode(url, "UTF-8"));

            return Uri.parse(filteredUrl);
        } catch (UnsupportedEncodingException e) {
            throw new UriCombinerException(e);
        }
    }
}

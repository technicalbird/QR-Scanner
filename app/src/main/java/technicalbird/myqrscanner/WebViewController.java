package technicalbird.myqrscanner;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Ankit on 19-Apr-16.
 */
public class WebViewController extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
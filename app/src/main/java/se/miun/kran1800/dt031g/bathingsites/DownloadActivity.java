package se.miun.kran1800.dt031g.bathingsites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Activity for downloading new bathing sites from a web page.
 * Implements LoaderCallbacks to create asyncTaskLoader for downloading in separate thread.
 */
public class DownloadActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean>{

    private DownloadingDialog downloadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        init();
    }

    // Initialize stuff
    private void init() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView webView = findViewById(R.id.web_view);
        webView.setWebViewClient(new customWebViewClient()); // Custom inner class
        // Add listener for download start
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                downloadBathingSites(url);
            }
        });
        webView.loadUrl(url);
    }

    // Start download and show download dialog.
    private void downloadBathingSites (String url) {
        Bundle argsBundle = new Bundle();
        argsBundle.putString("url", url);
        downloadingDialog = DownloadingDialog.newInstance(getString(R.string.downloading_bathing_site_message));
        downloadingDialog.show(getSupportFragmentManager(), "downloadBathingSitesDialog");
        getSupportLoaderManager().restartLoader(0, argsBundle, this);
    }

    //--------------- LoaderCallbacks required methods ---------------------

    @NonNull
    @Override
    public Loader<Boolean> onCreateLoader(int id, @Nullable Bundle args) {
        return new DownloadBathingSitesTask(this, args.getString("url"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Boolean> loader, Boolean data) {
        if(downloadingDialog != null) {
            downloadingDialog.dismiss();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Boolean> loader) {}

    // Inner class for custom WebViewClient that disable device browser opening links.
    public static class customWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}

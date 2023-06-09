package com.aradhita.onlinemarketplace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    String websiteURL = "https://ecommerce-abhinabade01.vercel.app/"; // sets web url
    private WebView webview;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //start point
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Swipe to refresh functionality
        mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webview.reload();
                    }
                }

        );

        checkInternetConnection();
    }

    private void load() { //loads the webview
        //Webview stuff
        webview = findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl(websiteURL);
        webview.setWebViewClient(new WebViewClientDemo());
    }

    private void checkInternetConnection(){
        // checks for internet connection
        // if present calls load()
        // If not gives an error dialouge

        if(CheckNetwork.isInternetAvailable(this))
            load();

        else{
            //if there is no internet do this
            setContentView(R.layout.activity_main);

            new AlertDialog.Builder(this) //alert the person knowing internet not available
                    .setTitle("No internet connection available")
                    .setMessage("Please Check you're Mobile data or Wifi network.")
                    .setPositiveButton("close", new DialogInterface.OnClickListener() { //closes the app
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Retry", new DialogInterface.OnClickListener() { //calls checkInternetConnection()
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           checkInternetConnection();

                        }
                    })
                    .show();
        }
    }

    private class WebViewClientDemo extends WebViewClient {
        @Override
        //Keep webview in app when clicking links
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mySwipeRefreshLayout.setRefreshing(false);

        }

    };

    //set back button functionality
    @Override
    public void onBackPressed() { //if user presses the back button do this
        if (webview.isFocused() && webview.canGoBack()) { //check if in webview and the user can go back
            webview.goBack(); //go back in webview
        } else { //do this if the webview cannot go back any further
            new AlertDialog.Builder(this) //alert the person knowing they are about to close
                    .setTitle("EXIT")
                    .setMessage("Are you sure. You want to close this app?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        //closes the app
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", null) //removes AlertDialog
                    .show();
        }

    }

}
/**
 * Utility class to check the availability of internet connection.
 */
class CheckNetwork {
    // Tag for logging purposes
    private static final String TAG = CheckNetwork.class.getSimpleName();

    public static boolean isInternetAvailable(Context context)

    {
        // retrieve the currently active network information
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)

                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)

        {
            Log.d(TAG,"no internet connection");

            return false;

        }
        else
        {

            if(info.isConnected())

            {

                Log.d(TAG," internet connection available...");

                return true;

            }

            else

            {

                Log.d(TAG," internet connection");

                return true;
            }
        }
    }
}

package club.cartoleirosfutebol.cartoleirosclub;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Arrays;

import dmax.dialog.SpotsDialog;
import im.delight.android.webview.AdvancedWebView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdvancedWebView.Listener {

    private AdvancedWebView mWebView;
    public static final String _URLMAIN = "http://cartoleirosfutebol.club/";
    public static final String _URL_MESSENGER_MAIN = "http://messenger.cartoleirosfutebol.club/website";
    public static String[] URLsFriendly = {
            "cartoleirosfutebol.club",
            "accounts.google.com"
    };
    public static final String _URLOFFLINE = "file:///android_asset/offline.html";
    Menu mMenuNavigation;
    private SwipeRefreshLayout swipeLayout;
    private ImageView imageProfile;
    private AlertDialog dialog;
    private TextView nameProfile;
    private TextView titleProfile;
    private User user = new User();
    private View mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = findViewById(R.id.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("javascript:$('#icon-notifications').click();");
            }
        });

        toolbar.findViewById(R.id.title2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("javascript:$('#icon-messages').click();");
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mMenuNavigation = navigationView.getMenu();

        View hView = navigationView.getHeaderView(0);
        imageProfile = (ImageView) hView.findViewById(R.id.imageView);
        nameProfile = (TextView) hView.findViewById(R.id.txtName);
        titleProfile = (TextView) hView.findViewById(R.id.txtTitle);
        Glide.with(this).load(R.drawable.default_user).transform(new CircleTransform(this)).into(imageProfile);

        dialog = new SpotsDialog(this, R.style.CustomDialogProgress);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Insert your code here
                mWebView.reload(); // refreshes the WebView
            }

        });

        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setListener(this, this);

        mWebView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mWebView.loadUrl(_URLOFFLINE);
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                if (dialog.isShowing() && dialog != null) {
                    dialog.dismiss();
                }
                if (!url.equals(_URLOFFLINE)) {
                    mWebView.loadUrl("javascript:alert(dataProfile())");
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);

                if (!dialog.isShowing() && dialog != null) {

                    dialog.show();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(seConectado()){
                    if(isFriendlyURL(url,URLsFriendly)) {
                        view.loadUrl(url);
                    } else {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                    }
                } else{
                    Snackbar.make(mainView, "Você não está conectado com a internet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    mWebView.loadUrl(_URLOFFLINE);
                }

                return true;
            }

        });

        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.loadUrl("http://cartoleirosfutebol.club");

    }

    @Override
    public void onBackPressed() {

        if (!mWebView.onBackPressed()) {
            return;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            mWebView.loadUrl("javascript:$('.topbar-actions.pull-right a.dropdown-toggle').click()");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_enter) {
            mWebView.loadUrl(_URLMAIN + "?r=user%2Fauth%2Flogin");
        } else if (id == R.id.nav_feed) {
            mWebView.loadUrl(_URLMAIN + "?r=dashboard%2Fdashboard");
        } else if (id == R.id.nav_profile) {
            mWebView.loadUrl("javascript:$(\".topbar-actions .dropdown-menu.pull-right li:not('.divider') a\")[0].click()");
        } else if (id == R.id.nav_settings) {
            mWebView.loadUrl("javascript:$(\".topbar-actions .dropdown-menu.pull-right li:not('.divider') a\")[1].click()");
        } else if (id == R.id.nav_spaces) {
            mWebView.loadUrl(_URLMAIN + "?r=directory%2Fdirectory%2Fspaces");
        } else if (id == R.id.nav_logout) {
            mWebView.loadUrl(_URLMAIN + "?r=user%2Fauth%2Flogout");
        } else if (id == R.id.nav_members) {
            mWebView.loadUrl(_URLMAIN + "?r=directory%2Fdirectory%2Fmembers");
        } else if (id == R.id.nav_posts) {
            mWebView.loadUrl(_URLMAIN + "?r=directory%2Fdirectory%2Fuser-posts");
        } else if (id == R.id.nav_selecao) {
            mWebView.loadUrl(_URLMAIN + "?r=custom_pages%2Fview&id=3");
        } else if (id == R.id.nav_messenger) {
            Intent intent = new Intent(this, MessengerActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        hideSoftKeyboard();
    }

    @Override
    public void onPageFinished(String url) {

        swipeLayout.setRefreshing(false);

        if (url.contains("?r=user%2Fauth%2Flogin")) {
            getSupportActionBar().hide();
        } else {
            getSupportActionBar().show();
        }

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        dialog.dismiss();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    public String getCookie(String siteName, String CookieName) {
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);

        if (cookies != null) {
            String[] temp = cookies.split(";");
            for (String ar1 : temp) {
                if (ar1.contains(CookieName)) {
                    String[] temp1 = ar1.split("=");
                    CookieValue = temp1[1];
                }
            }
        }

        return CookieValue;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    public boolean seConectado() {
        return isConnected(this);
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected(); // isConnectedOrConnecting()
        return isConnected;
    }

    public static boolean isFriendlyURL(String inputStr, String[] items)
    {
        for(int i =0; i < items.length; i++)
        {
            if(inputStr.contains(items[i]))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        return isWiFi;
    }

    private void setVisibilityItems(String url){

        Boolean isLogadoByCookie = getCookie(url, "_identity") == null ? false : true;
        String labelVisitante = user.getName() == null ? "" : user.getName();
        boolean isVisitante = true;

        if (labelVisitante != null) {
            // Usuário que logam assim que registram, não possuem cookie '+identity', então identificamos se ele está logado usando o nome 'Visitante'
            isVisitante = labelVisitante.equals("") || labelVisitante.toLowerCase().equals("visitante");
        }

        boolean logado = isLogadoByCookie || !isVisitante;

        MenuItem itemEntrar = mMenuNavigation.findItem(R.id.nav_enter);
        MenuItem itemFeed = mMenuNavigation.findItem(R.id.nav_feed);
        MenuItem itemEspacos = mMenuNavigation.findItem(R.id.nav_spaces);
        MenuItem itemProfile = mMenuNavigation.findItem(R.id.nav_profile);
        MenuItem itemSettings = mMenuNavigation.findItem(R.id.nav_settings);
        MenuItem itemLogout = mMenuNavigation.findItem(R.id.nav_logout);
        MenuItem itemMembers = mMenuNavigation.findItem(R.id.nav_members);
        MenuItem itemPosts = mMenuNavigation.findItem(R.id.nav_posts);

        if (logado) {
            itemEntrar.setVisible(false);
            itemEspacos.setVisible(true);
            itemFeed.setVisible(true);
            itemProfile.setVisible(true);
            itemSettings.setVisible(true);
            itemLogout.setVisible(true);
            itemMembers.setVisible(true);
            itemPosts.setVisible(true);
        } else {
            itemEntrar.setVisible(true);
            itemEspacos.setVisible(true);
            itemFeed.setVisible(true);
            itemProfile.setVisible(false);
            itemSettings.setVisible(false);
            itemLogout.setVisible(false);
            itemMembers.setVisible(true);
            itemPosts.setVisible(true);
        }

    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void handleDataInformation(String json) {
        try {
            Log.i("JSON", json);
            if (json != null && json != "") {
                Gson gson = new Gson();
                user = gson.fromJson(json, User.class);
                setVisibilityItems(mWebView.getUrl());

                String img = user.getImg();
                String name = user.getName();
                String title = user.getTitle();

                if (name != "") {
                    nameProfile.setText(name);
                }

                if (title != "") {
                    titleProfile.setText(title);
                }

                if (img != "") {
                    String imageUrl = _URLMAIN + img;
                    Glide.with(this).load(imageUrl).transform(new CircleTransform(this)).into(imageProfile);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyWebChromeClient
            extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public MyWebChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (MainActivity.this == null) {
                return null;
            }
            return BitmapFactory.decodeResource(MainActivity.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) MainActivity.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            MainActivity.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mCustomView.setBackgroundColor(Color.BLACK);
            this.mOriginalSystemUiVisibility = MainActivity.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = MainActivity.this.getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) MainActivity.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            handleDataInformation(message);
            result.confirm();
            return true;
        }

    }

}



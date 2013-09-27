package org.luke54.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class ArticleActivity extends SherlockActivity {

    private WebView webView;
    private AQuery aq;
    private Article article;

    private ShareActionProvider sherlockActionProvider;
    private ProgressDialog progressBar;

    public static List<Article> entrys = new ArrayList<Article>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.article_activity);

        webView = (WebView)findViewById(R.id.article);

        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        webView.setInitialScale(230);
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAppCacheEnabled(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setPluginsEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClient());

        aq = new AQuery(this);

        String className = getIntent().getStringExtra("className");
        aq.id(R.id.article_title).text(initArticle().getTitle());
        progressBar = ProgressDialog.show(ArticleActivity.this, article.getTitle(), getResources().getString(R.string.loading));

        if (className.equalsIgnoreCase("DailyBibleActivity")) {
            webSettings.setBlockNetworkImage(true);
        }


        String url = "http://www.luke54.org/index.php?option=com_mobileapp&task=item&itemId=" + initArticle().getId();

        aq.progress(R.id.progress_bar).ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                try {

                        String html = jo.getString("content");
                        aq.id(R.id.article).getWebView().loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

                        progressBar.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public Article initArticle() {
        article = new Article(getIntent().getStringExtra("id"),
                              getIntent().getStringExtra("title"),
                              getIntent().getStringExtra("link"),
                              getIntent().getStringExtra("thumbImgSrc"),
                              getIntent().getStringExtra("description")
                               );
        return article;
    }



    public static String replaceAllReadMore(String content) {
        String regStr = "延伸閱讀.*<([^>]*)>";
        String result = content.replaceAll(regStr, "");

        return result;
    }

    public List<Article> favoriteList() {
        Article article = initArticle();
        entrys.add(article);

        return entrys;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.article, menu);

        sherlockActionProvider = (ShareActionProvider)menu.findItem(R.id.share).getActionProvider();
        sherlockActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        Intent intent = getDefaultShareIntent();

        if (intent != null) {
            sherlockActionProvider.setShareIntent(intent);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public Intent getDefaultShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, article.getTitle() + "\n\n" + article.getDescription() + "\n\n" + article.getLink());
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

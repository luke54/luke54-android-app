package org.luke54.app;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Build;
import android.os.Bundle;


import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.widget.HListView;


public class MainActivity extends SherlockActivity implements it.sephiroth.android.library.widget.AdapterView.OnItemClickListener {

    private AQuery aq;
    private AQuery listAq;
    private List<Article> entries;
    private HListView luke54_hot_listView, daily_bible_listView, rhema_words_listView, gospel_articles_listView;

    ArticleAdapterNative luke54_hot_mAdapter, daily_bible_mAdapter, rhema_words_mAdapter, gospel_articles_mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        aq = new AQuery(this);
        luke54_hot_listView = (HListView)findViewById(R.id.luke54_hot_hlistView);
        daily_bible_listView = (HListView)findViewById(R.id.daily_bible_hlistView);
        rhema_words_listView = (HListView)findViewById(R.id.rhema_words_hlistView);
        gospel_articles_listView = (HListView)findViewById(R.id.gospel_articles_hlistView);

        String[] rhema_words_array_json = getResources().getStringArray(R.array.rhema_words_array_json_rul);

        int rhema_random_index = (int)(Math.random()*4);

        String[] url = {
                getResources().getString(R.string.luke54_hot_json_url),
                getResources().getString(R.string.daily_bible_json_url),
                rhema_words_array_json[rhema_random_index],
                getResources().getString(R.string.gospel_articles_json_url)
        };
        for(int i = 0; i < 4; i++) {
            work(url[i]);
        }
    }

    public void work(String url){


        AQUtility.cleanCacheAsync(this, 0, 0);
        BitmapAjaxCallback.clearCache();

        aq.progress(R.id.progress).ajax(url, JSONArray.class, this, "renderArticles");
//        View list = aq.id(R.id.list).getView();
    }


    public void scrolledBottom(AbsListView view, int scrollState){

        Toast toast = Toast.makeText(this, "ScrolledBottom", Toast.LENGTH_SHORT);
        toast.show();
    }

    public  Article convert(JSONObject jo) throws JSONException {

        try {
            String id = jo.getString("id");
            String title = jo.getString("title");
            String link = jo.getString("link");
            String thumbImgSrc = jo.getString("thumb");
            String description = jo.getString("description");


            Article article = new Article(id, title, link, thumbImgSrc, description);
            return article;

        }finally {
        }
    }

    public  List<Article> convertAll(JSONArray jsonArray) throws Exception{


        List<Article> articls = new ArrayList<Article>();

        for(int i = 0; i < 6; i++){
            JSONObject article = jsonArray.getJSONObject(i);
            articls.add(convert(article));
        }

        return articls;
    }


    public void renderArticles(String url, JSONArray jsonArray, AjaxStatus status) throws Exception{

        if(jsonArray == null) return;

        entries = convertAll(jsonArray);
        listAq = new AQuery(this);



        if (url == getResources().getString(R.string.luke54_hot_json_url)) {
            luke54_hot_mAdapter = new ArticleAdapterNative(this, R.layout.main_page_hlist_view_item, entries);
            luke54_hot_listView.setAdapter(luke54_hot_mAdapter);
            luke54_hot_listView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int position, long id) {
                    Article article = (Article)(parent.getAdapter().getItem(position));
                    onListItemClicked(article);
//                    onGalleryItemClicked(position);
                }
            });
        }

        else if (url == getResources().getString(R.string.daily_bible_json_url)) {
            daily_bible_mAdapter = new ArticleAdapterNative(this, R.layout.main_page_hlist_view_item, entries);
            daily_bible_listView.setAdapter(daily_bible_mAdapter);
            daily_bible_listView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int position, long id) {
                    Article article = (Article)(parent.getAdapter().getItem(position));
                   onDailyBibleListItemClicked(article);
//                    onGalleryItemClicked(position);
                }
            });
        }
//sdfsdfsdf
        else if (url == getResources().getString(R.string.gospel_articles_json_url)) {
            gospel_articles_mAdapter = new ArticleAdapterNative(this, R.layout.main_page_hlist_view_item, entries);
            gospel_articles_listView.setAdapter(gospel_articles_mAdapter);
            gospel_articles_listView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int position, long id) {
                    Article article = (Article)(parent.getAdapter().getItem(position));
                    onListItemClicked(article);
                }
            });
        }

        else {
            rhema_words_mAdapter = new ArticleAdapterNative(this, R.layout.main_page_hlist_view_item, entries);
            rhema_words_listView.setAdapter(rhema_words_mAdapter);
            rhema_words_listView.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int position, long id) {
                    Article article = (Article)(parent.getAdapter().getItem(position));
                    onListItemClicked(article);
                }
            });
        }
    }

    @Override
    public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> parent, View view, int position, long id) {

    }

    public void onListItemClicked (Article article) {
        Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
        intent.putExtra("id", article.getId());
        intent.putExtra("title", article.getTitle());
        intent.putExtra("link", article.getLink());
        intent.putExtra("description", article.getDescription());
        intent.putExtra("className", "MainActivity");
        startActivity(intent);
    }

    public void onDailyBibleListItemClicked (Article article) {
        Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
        intent.putExtra("id", article.getId());
        intent.putExtra("title", article.getTitle());
        intent.putExtra("link", article.getLink());
        intent.putExtra("description", article.getDescription());
        intent.putExtra("className", "DailyBibleActivity");
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent_luke54_hot = new Intent(MainActivity.this, Luke54HotActivity.class);
        Intent intent_daily_bible = new Intent(MainActivity.this, DailyBibleActivity.class);
        Intent intent_rhema_words = new Intent(MainActivity.this, RhemaWordsActivity.class);
        Intent intent_gospel_articles = new Intent(MainActivity.this, GospelArticlesActivity.class);
//        Intent intent_favorite_list = new Intent(MainActivity.this, FavoriteListActivity.class);
        switch (item.getItemId()) {
            case R.id.luke54_hot:
                startActivity(intent_luke54_hot);
                break;
            case R.id.daily_bible:
                startActivity(intent_daily_bible);
                break;
            case R.id.rhema_words:
                startActivity(intent_rhema_words);
                break;
            case R.id.gospel_article:
                startActivity(intent_gospel_articles);
                break;
            case R.id.about_luke54:
                Intent about_luke54_intent = new Intent(MainActivity.this, AboutLuke54Activity.class);
                startActivity(about_luke54_intent);
                break;
            case R.id.luke54_facebook_fans:
                Intent luke54_facebook_fans_intent = new Intent(MainActivity.this, Luke54FansActivity.class);
                startActivity(luke54_facebook_fans_intent);
                break;
            case R.id.luke54_moh:
                Intent luke54_moh_intent = new Intent(MainActivity.this, MinistryOfLifeActivity.class);
                startActivity(luke54_moh_intent);
                break;
            case R.id.fttt_moh:
                Intent fttt_moh_intent = new Intent(MainActivity.this, FTTTMOHActivity.class);
                startActivity(fttt_moh_intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onActionItemClicked( ActionMode mode, android.view.MenuItem item ) {

        return false;
    }

    public boolean onCreateActionMode( ActionMode mode, android.view.Menu menu ) {
        menu.add( 0, 0, 0, "Delete" );
        return true;
    }

    public void onDestroyActionMode( ActionMode mode ) {

    }

    public boolean onPrepareActionMode( ActionMode mode, android.view.Menu menu ) {
        return true;
    }

    @SuppressLint("NewApi")
    public void onItemCheckedStateChanged( ActionMode mode, int position, long id, boolean checked ) {

    }

    class ArticleAdapterNative extends ArrayAdapter<Article> {
        List<Article> articles;

        public ArticleAdapterNative(Context context, int resourceId,  List<Article> entries) {
            super(context, resourceId, entries);
            articles = entries;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
//            return articles.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position % 3;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.main_page_hlist_view_item, parent, false);
            }

            Article article = getItem(position);

            AQuery aq = listAq.recycle(convertView);

            aq.id(R.id.main_page_hlistView_item_title).text(article.getTitle());
//                aq.id(R.id.meta).text(article.getDescription());

            String tbUrl = article.getThumbImgSrc();

            Bitmap placeholder = aq.getCachedImage(R.drawable.image_ph);

//            if(aq.shouldDelay(position, convertView, parent, tbUrl)){
//
//                aq.id(R.id.main_page_gallery_item_image).image(placeholder);
//            }else{

            aq.id(R.id.main_page_hlistView_item_image).image(tbUrl, true, true, 0, R.drawable.image_missing, placeholder, AQuery.FADE_IN_NETWORK, 0);
//            }

            return convertView;
        }
    }
}

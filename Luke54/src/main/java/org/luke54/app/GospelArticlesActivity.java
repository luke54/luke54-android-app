package org.luke54.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.util.AQUtility;

import org.json.JSONArray;

import java.util.List;

public class GospelArticlesActivity extends ImageLoadingListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        aq.id(R.id.list).scrolled(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                AQUtility.debug("forward", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                AQUtility.debug("forward", "onScroll");
            }
        });

    }



    public void work(){


        AQUtility.cleanCacheAsync(this, 0, 0);
        BitmapAjaxCallback.clearCache();

        String url = "http://www.luke54.org/components/com_mobileapp/ask.json";
        aq.progress(R.id.progress).ajax(url, JSONArray.class, this, "renderArticles");

        View list = aq.id(R.id.list).getView();
    }

    public void scrolledBottom(AbsListView view, int scrollState){

        Toast toast = Toast.makeText(this, "ScrolledBottom", Toast.LENGTH_SHORT);
        toast.show();
    }



    public void renderArticles(String url, JSONArray jsonArray, AjaxStatus status) throws Exception{

        if(jsonArray == null) return;

        final List<Article> entries = ArticleAdapter.convertAll(jsonArray);

        listAq = new AQuery(this);

        ArrayAdapter<Article> aa = new ArrayAdapter<Article>(this, R.layout.photo_item, entries){

            public View getView(int position, View convertView, ViewGroup parent) {

                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.photo_item, parent, false);
                }

                Article article = getItem(position);

                AQuery aq = listAq.recycle(convertView);

                aq.id(R.id.name).text(article.getTitle());
                aq.id(R.id.meta).text(article.getDescription());

                String tbUrl = article.getThumbImgSrc();

                Bitmap placeholder = aq.getCachedImage(R.drawable.image_ph);

                if(aq.shouldDelay(position, convertView, parent, tbUrl)){

                    aq.id(R.id.tb).image(placeholder);
                }else{

                    aq.id(R.id.tb).image(tbUrl, true, true, 0, R.drawable.image_missing, placeholder, AQuery.FADE_IN_NETWORK, 0);
                }

                return convertView;

            }


        };

        aq.id(R.id.list).adapter(aa);
        aq.id(R.id.list).itemClicked(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GospelArticlesActivity.this, ArticleActivity.class);
                intent.putExtra("id", entries.get(i).getId());
                intent.putExtra("title", entries.get(i).getTitle());
                intent.putExtra("link", entries.get(i).getLink());
                intent.putExtra("description", entries.get(i).getDescription());
                intent.putExtra("className", "GospelArticleActivity");
                startActivity(intent);
            }
        });


//        aq.scrolledBottom(this, "scrolledBottom");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gospel_articles, menu);
        return true;
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

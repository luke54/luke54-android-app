package org.luke54.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hitobias on 13/9/5.
 */
public class ArticleAdapter {

    public static Article convert(JSONObject jo) throws JSONException {

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

    public static List<Article> convertAll(JSONArray jsonArray) throws Exception{


        List<Article> articls = new ArrayList<Article>();

        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject article = jsonArray.getJSONObject(i);
            articls.add(convert(article));
        }

        return articls;
    }

}

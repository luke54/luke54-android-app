package org.luke54.app;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;


public class MinistryOfLifeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.ministry_of_life);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ministry_of_life, menu);
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
            case R.id.luke54_moh_add_contact:
                Intent intent_add_contact = new Intent(Intent.ACTION_INSERT);
                intent_add_contact.setType("vnd.android.cursor.dir/person");
                intent_add_contact.setType("vnd.android.cursor.dir/contact");
                intent_add_contact.setType("vnd.android.cursor.dir/raw_contact");
                startActivity(intent_add_contact);;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

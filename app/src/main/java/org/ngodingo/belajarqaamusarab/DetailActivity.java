package org.ngodingo.belajarqaamusarab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long id = getIntent().getLongExtra("id",0);
        Realm realm = Realm.getDefaultInstance();
        Entry entry = realm.where(Entry.class).equalTo("id",id).findFirst();

        TextView arabTextView = (TextView) findViewById(R.id.tv_arab);
        TextView terjemahanTextView = (TextView) findViewById(R.id.tv_terjemahan);

        arabTextView.setText(entry.getText());
        terjemahanTextView.setText(entry.getTarjim());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

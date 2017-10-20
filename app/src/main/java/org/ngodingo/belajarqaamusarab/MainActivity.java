package org.ngodingo.belajarqaamusarab;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.speech.RecognizerIntent;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private SimpleCursorAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchView = (SearchView) findViewById(R.id.search_view);

        final String[] from = new String[] {"text"};
        final int[] to = new int[] {android.R.id.text1};

        mAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_dropdown_item,
                null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mSearchView.setSuggestionsAdapter(mAdapter);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final MatrixCursor matrixCursor = new MatrixCursor(new String[] {
                        BaseColumns._ID, "text"
                });

                Realm realm = Realm.getDefaultInstance();
                RealmResults<Entry> realmResults = realm.where(Entry.class)
                        .contains("tarjim", newText, Case.INSENSITIVE).findAll();

                for (int i = 0; i < realmResults.size(); i++) {
                    Entry entry = realmResults.get(i);
                    Object[] row = new Object[] {
                            entry.getId(), entry.getTarjim()
                    };
                    matrixCursor.addRow(row);
                }

                realm.close();

                mAdapter.changeCursor(matrixCursor);

                return true;

            }
        });

        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter ca = mSearchView.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                long id = cursor.getLong(0);

                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                return true;
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    mSearchView.setQuery(result.get(0), false);
                }

                break;
            }

        }
    }
}

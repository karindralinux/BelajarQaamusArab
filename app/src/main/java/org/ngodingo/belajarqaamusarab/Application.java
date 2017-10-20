package org.ngodingo.belajarqaamusarab;

import android.os.DropBoxManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Ariaseta on 12/08/2017.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        init();

    }

    private void init() {
        final Realm realm = Realm.getDefaultInstance();
        long numberOfEntries = realm.where(Entry.class).count();

        if (numberOfEntries == 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(Entry.class);
                    importFromCSV(realm);
                }
            });
        }
        realm.close();
    }

    private void importFromCSV(Realm realm) {
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open("Qaamus.csv"));
            BufferedReader reader = new BufferedReader(is);
            String line;

            int id = 0;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                id++;

                Entry entry = new Entry();
                entry.setId(id);
                entry.setText(row[0]);
                entry.setTarjim(row[1]);

                realm.insert(entry);
            }

        } catch (IOException e) {

        }
    }
}

package com.blitzfud.controllers.localDB;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blitzfud.R;
import com.blitzfud.controllers.restapi.API;

import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class DBConnection extends MultiDexApplication {

    public static AtomicLong shoppingCartDBId = new AtomicLong();
    public static AtomicLong itemShoppingCartDBId = new AtomicLong();
    public static AtomicLong itemOrderId = new AtomicLong();

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        setUpRealmConfig();

        API.DOMINIO = getResources().getString(R.string.backend_url_dev);
        API.BASE_URL = API.DOMINIO + "/";

    }

    private void setUpRealmConfig(){
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }


    public static <T extends RealmObject> AtomicLong getIdByTable(Realm realm, Class<T> anyClass){
        RealmResults<T> results = realm.where(anyClass).findAll();

        return results.isEmpty()? new AtomicLong() : new AtomicLong(results.max("_id").longValue());
    }

    public static void clearDatabase(final Realm realm){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        resetIds();
    }

    private static void resetIds(){
        shoppingCartDBId = new AtomicLong();
        itemShoppingCartDBId = new AtomicLong();
        itemOrderId = new AtomicLong();
    }

}

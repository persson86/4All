package dev.lfspersson.a4all.database.dao;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

import dev.lfspersson.a4all.database.DatabaseHelper;
import dev.lfspersson.a4all.database.models.ItemModel;
import io.realm.Realm;

/**
 * Created by LFSPersson on 28/11/16.
 */

@EBean(scope = EBean.Scope.Singleton)
public class ItemDAO {
    @RootContext
    Context context;

    @Bean
    DatabaseHelper dbHelper;

    public void SaveList(List<ItemModel> model) {
        Realm realm = dbHelper.getRealm();
        realm.beginTransaction();
        realm.where(ItemModel.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
    }

    public ItemModel getItemById(String id) {
        return dbHelper.getRealm().where(ItemModel.class).equalTo("id", id).findFirst();
    }

    public List<ItemModel> getList() {
        return dbHelper.getRealm().where(ItemModel.class).findAll();
    }
}
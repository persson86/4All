package dev.lfspersson.a4all.database.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by LFSPersson on 18/11/16.
 */

public class ItemListModel {
    @SerializedName("lista")
    private List<String> lista;

    public List<String> getLista() {
        return lista;
    }
}

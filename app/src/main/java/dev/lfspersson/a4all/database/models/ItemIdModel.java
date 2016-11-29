package dev.lfspersson.a4all.database.models;

import io.realm.RealmObject;

/**
 * Created by LFSPersson on 28/11/16.
 */

public class ItemIdModel extends RealmObject {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

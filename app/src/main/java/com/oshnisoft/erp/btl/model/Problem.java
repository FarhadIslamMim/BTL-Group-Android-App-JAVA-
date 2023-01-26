package com.oshnisoft.erp.btl.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Problem extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Problem){
            Problem c = (Problem) obj;
            if(c.getName().equals(name) ) return true;
        }

        return false;
    }

}

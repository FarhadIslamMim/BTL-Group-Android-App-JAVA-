package com.oshnisoft.erp.btl.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DealerQuestion extends RealmObject {
    @PrimaryKey
    private long id;
    private String question;
    private String question_type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    @Override
    public String toString() {
        return question;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DealerQuestion){
            DealerQuestion c = (DealerQuestion) obj;
            if(c.getQuestion().equals(question) ) return true;
        }

        return false;
    }

}

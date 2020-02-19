package com.yyxnb.widget.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "test")
public class TestData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;

    public TestData() {
    }

    public TestData(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestData)) return false;
        TestData data = (TestData) o;
        return getId() == data.getId() &&
                getContent().equals(data.getContent());
    }

    @Override
    public int hashCode() {
        return Math.abs(Objects.hash(getId(), getContent()));
    }
}

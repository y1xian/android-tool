package com.yyxnb.widget.bean;


import java.io.Serializable;
import java.util.Objects;

public class MainBean implements Serializable {

    public int id;
    public String content;
    public String url;

    public MainBean(int id, String content, String url) {
        this.id = id;
        this.content = content;
        this.url = url;
    }

    public MainBean(String content, String url) {
        this.content = content;
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MainBean)) return false;
        MainBean mainBean = (MainBean) o;
        return id == mainBean.id &&
                content.equals(mainBean.content) &&
                url.equals(mainBean.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, url);
    }
}

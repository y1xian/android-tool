package com.yyxnb.widget.bean;

import java.io.Serializable;

public class BaseDatas<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

}

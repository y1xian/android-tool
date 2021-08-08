package com.yyxnb.oh.localservice.manager;


import com.yyxnb.oh.localservice.bean.LocalFolder;

import java.util.ArrayList;


/**
 * 数据回调
 */
public interface DataCallback {


    void onData(ArrayList<LocalFolder> list);

}

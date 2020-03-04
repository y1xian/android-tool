package com.yyxnb.widget.vm;

import com.yyxnb.arch.base.mvvm.BaseViewModel;
import com.yyxnb.arch.common.Message;

public class MsgViewModel extends BaseViewModel {

    public void reqToast(String str){

        getDefUI().getToastEvent().setValue(str);
    }
    public void reqMsg(String str){
        getDefUI().getMsgEvent().setValue(new Message(1,str));
    }
}
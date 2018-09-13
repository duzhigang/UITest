package com.ggec.uitest.data;

/**
 * Created by sanmu on 2016/10/13 0013.
 */
public interface Callback {
    /**
     * @param position 0:表示取消事件，1表示确定事件
     * */
    public void callback(int position);
}

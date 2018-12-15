package com.bawei.week3.model;


import com.bawei.week3.callback.MyCallBack;

import java.util.Map;

/**
 * Model接口
 */
public interface IModel {
    void requestData(String url, Map<String, String> params, Class clazz, MyCallBack callBack);
}

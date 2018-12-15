package com.bawei.week3.model;

import com.bawei.week3.callback.MyCallBack;
import com.bawei.week3.okHttp.ICallBack;
import com.bawei.week3.okHttp.OkHttpUtils;

import java.util.Map;

public class IModelImpl implements IModel {
    @Override
    public void requestData(final String url, Map<String, String> params, final Class clazz, final MyCallBack callBack) {
        OkHttpUtils.getInstance().postEnqueue(url, params, clazz, new ICallBack() {
            @Override
            public void success(Object obj) {
                callBack.success(obj);
            }

            @Override
            public void failed(Exception e) {
                callBack.failed(e);
            }
        });
    }
}

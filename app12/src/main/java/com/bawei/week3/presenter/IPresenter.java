package com.bawei.week3.presenter;

import java.util.Map;

public interface IPresenter {
    void startRequest(String url, Map<String, String> params, Class clazz);
}

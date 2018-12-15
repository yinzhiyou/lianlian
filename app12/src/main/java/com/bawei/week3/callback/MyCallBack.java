package com.bawei.week3.callback;

public interface MyCallBack<T> {
    void success(T data);
    void failed(Exception e);
}

package com.bawei.week3.view;

public interface IView<T> {
    void showResponseData(T data);
    void showResponseFail(T data);
}

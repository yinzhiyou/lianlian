package com.bawei.week3.presenter;


import com.bawei.week3.callback.MyCallBack;
import com.bawei.week3.model.IModelImpl;
import com.bawei.week3.view.IView;

import java.util.Map;

public class IPresenterImpl implements IPresenter {
    private IModelImpl model;
    private IView iView;

    public IPresenterImpl(IView iView) {
        this.iView = iView;
        model = new IModelImpl();
    }

    @Override
    public void startRequest(String url, Map<String, String> params, Class clazz) {
        model.requestData(url, params, clazz, new MyCallBack() {
            @Override
            public void success(Object data) {
                iView.showResponseData(data);
            }

            @Override
            public void failed(Exception e) {
                iView.showResponseFail(e);
            }
        });
    }

    public void onDetach() {
        if (model != null) {
            model = null;
        }
        if (iView != null) {
            iView = null;
        }
    }
}

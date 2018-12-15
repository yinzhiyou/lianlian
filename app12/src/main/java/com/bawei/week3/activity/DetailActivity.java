package com.bawei.week3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.week3.Apis;
import com.bawei.week3.Bean.ProductDetailBean;
import com.bawei.week3.Constants;
import com.bawei.week3.R;
import com.bawei.week3.presenter.IPresenterImpl;
import com.bawei.week3.view.IView;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements IView{
    private IPresenterImpl mIPresenterImpl;
    private String mPid;
    private TextView mTvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getPid();
        initPresenter();
        initView();
        getData();
    }

    private void getPid() {
        mPid = getIntent().getStringExtra(Constants.INTENT_KEY_PRODUCES_PID);
    }

    private void initPresenter() {
        mIPresenterImpl = new IPresenterImpl(this);
    }

    private void initView() {
        mTvDetail = findViewById(R.id.tv_detail);
    }

    private void getData() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.MAP_KEY_PRODUCTS_DETAIL_PID, mPid);

        mIPresenterImpl.startRequest(Apis.URL_PRODUCTS_DETAIL, map, ProductDetailBean.class);
    }

    @Override
    public void showResponseData(Object data) {
        if(data instanceof ProductDetailBean){
            ProductDetailBean productDetailBean = (ProductDetailBean)data;
            Toast.makeText(this, productDetailBean.getCode(), Toast.LENGTH_SHORT).show();
            //TODO:完成详情页剩余布局，完成javaBean剩余参数，完成数据展示，我不写了
        }else{
            Toast.makeText(this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showResponseFail(Object data) {
        if (data instanceof Exception){
            Exception e = (Exception)data;
            e.printStackTrace();
        }
        Toast.makeText(this, "网络请求失败", Toast.LENGTH_SHORT).show();
    }
}

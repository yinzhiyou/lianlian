package com.bawei.week3.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.week3.Apis;
import com.bawei.week3.Bean.ProductsListBean;
import com.bawei.week3.Constants;
import com.bawei.week3.R;
import com.bawei.week3.adapter.RecyclerAdapter;
import com.bawei.week3.presenter.IPresenterImpl;
import com.bawei.week3.view.IView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IView, View.OnClickListener {
    private static final int SPAN_COUNT = 2;

    private IPresenterImpl mIPresenterImpl;
    private EditText mEdit;
    private XRecyclerView mXRecyclerView;
    private RecyclerAdapter mAdapter;
    /**
     * 用成员变量记录页数
     */
    private int mPage = 1;
    /**
     * 用成员变量记录所选条目
     */
    private int mSort = 0;
    /**
     * 用来控制管理器是线性还是网格
     */
    private boolean mIsLinear = true;
    private TextView mTvComplex, mTvSalesVolume, mTvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPresenter();
        initView();
        initRecyclerView();
    }

    /**
     * 实现P层
     */
    private void initPresenter() {
        mIPresenterImpl = new IPresenterImpl(this);
    }

    /**
     * 初始化所有控件
     */
    private void initView() {
        mTvComplex = findViewById(R.id.tv_complex);
        mTvSalesVolume = findViewById(R.id.tv_sales_volume);
        mTvPrice = findViewById(R.id.tv_price);
        mTvComplex.setSelected(true);

        findViewById(R.id.iv_search);
        findViewById(R.id.iv_change);
        mEdit = findViewById(R.id.edit_text);
        mXRecyclerView = findViewById(R.id.XRecyclerView);

        findViewById(R.id.iv_search).setOnClickListener(this);
        findViewById(R.id.iv_change).setOnClickListener(this);
        mTvComplex.setOnClickListener(this);
        mTvSalesVolume.setOnClickListener(this);
        mTvPrice.setOnClickListener(this);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(true);
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getProductList();
            }

            @Override
            public void onLoadMore() {
                getProductList();
            }
        });

        changeRecyclerView();
    }

    /**
     * 切换管理器
     */
    private void changeRecyclerView() {
        //根据状态判断展示线性还是网格
        if (mIsLinear) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mXRecyclerView.setLayoutManager(layoutManager);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            mXRecyclerView.setLayoutManager(layoutManager);
        }

        //重新创建adapter，因为new 之后数据和点击事件都没有了，所以需要重新添加点击事件
        mAdapter = new RecyclerAdapter(this, mIsLinear);
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<ProductsListBean.Data> data = mAdapter.getData();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constants.INTENT_KEY_PRODUCES_PID, data.get(position).getPid());
                startActivity(intent);
            }
        });
        mXRecyclerView.setAdapter(mAdapter);
        //将状态反选
        mIsLinear = !mIsLinear;
    }

    /**
     * 拉取数据
     */
    private void getProductList() {
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.MAP_KEY_SEARCH_PRODUCTS_KEYWORDS, mEdit.getText().toString());
        map.put(Constants.MAP_KEY_SEARCH_PRODUCES_PAGE, String.valueOf(mPage));
        map.put(Constants.MAP_KEY_SEARCH_PRODUCES_SORT, String.valueOf(mSort));

        mIPresenterImpl.startRequest(Apis.URL_SEARCH_PRODUCTS, map, ProductsListBean.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                //开始获取网络数据
                getProductList();
                break;
            case R.id.iv_change:
                //切换布局
                //因为切换布局时需要new 一个adapter，为了保证数据的一致性，所以先取出老adapter中的老数据
                List<ProductsListBean.Data> data = mAdapter.getData();
                changeRecyclerView();
                //将老数据赋值给新的adapter
                mAdapter.setData(data);
                break;
            case R.id.tv_complex:
                //如果当前栏目已经被选中，则再次点击无效，不然就会再次请求
                if(!mTvComplex.isSelected()) {
                    //切换时一定要从第一页获取数据，不然会造成新老数据在一起，导致排序出现问题
                    mPage = 1;
                    mSort = 0;
                    getProductList();

                    mTvComplex.setSelected(true);
                    mTvSalesVolume.setSelected(false);
                    mTvPrice.setSelected(false);
                }
                break;
            case R.id.tv_sales_volume:
                if(!mTvSalesVolume.isSelected()) {
                    mPage = 1;
                    mSort = 1;
                    getProductList();

                    mTvComplex.setSelected(false);
                    mTvSalesVolume.setSelected(true);
                    mTvPrice.setSelected(false);
                }
                break;
            case R.id.tv_price:
                if(!mTvPrice.isSelected()) {
                    mPage = 1;
                    mSort = 2;
                    getProductList();

                    mTvComplex.setSelected(false);
                    mTvSalesVolume.setSelected(false);
                    mTvPrice.setSelected(true);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void showResponseData(Object data) {
        if (data instanceof ProductsListBean) {
            ProductsListBean productsListBean = (ProductsListBean) data;
            List<ProductsListBean.Data> list = productsListBean.getData();
            if (mPage == 1) {
                mAdapter.setData(list);
            } else {
                mAdapter.addData(list);
            }

            mPage++;
            mXRecyclerView.refreshComplete();
            mXRecyclerView.loadMoreComplete();
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

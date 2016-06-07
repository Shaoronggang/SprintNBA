package com.yuyh.cavaliers.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.yuyh.cavaliers.R;
import com.yuyh.cavaliers.base.BaseAppCompatActivity;
import com.yuyh.cavaliers.base.BaseLazyFragment;
import com.yuyh.cavaliers.bean.NavigationEntity;
import com.yuyh.cavaliers.ui.adapter.VPFragmentAdapter;
import com.yuyh.cavaliers.ui.fragment.NBANewsFragment;
import com.yuyh.library.utils.toast.ToastUtils;
import com.yuyh.library.view.viewpager.XViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class MainActivity extends BaseAppCompatActivity {

    @InjectView(R.id.home_container)
    XViewPager mViewPager;
    @InjectView(R.id.home_navigation_list)
    ListView mNavListView;
    @InjectView(R.id.home_drawer)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mActionBarDrawerToggle = null;
    private List<NavigationEntity> list;
    private QuickAdapter<NavigationEntity> mNavListAdapter = null;
    private List<BaseLazyFragment> fragments;

    private static long DOUBLE_CLICK_TIME = 0L;
    private int mCurrentMenuCheckedPos = 0;

    private int mCheckedListItemColorResIds[] = {
            R.color.navigation_checked_picture_text_color,
            R.color.navigation_checked_video_text_color,
            R.color.navigation_checked_music_text_color,
    };

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {
        initData();
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(getString(R.string.app_name));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (null != mNavListAdapter) {
                    setTitle(mNavListAdapter.getItem(mCurrentMenuCheckedPos).getName());
                }
            }
        };
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        mNavListAdapter = new QuickAdapter<NavigationEntity>(getApplicationContext(), R.layout.list_item_navigation) {
            @Override
            protected void convert(BaseAdapterHelper helper, NavigationEntity item) {
                helper.setImageResource(R.id.list_item_navigation_icon, item.getIconResId())
                        .setText(R.id.list_item_navigation_name, item.getName());
            }
        };
        mNavListView.setAdapter(mNavListAdapter);
        mNavListAdapter.addAll(list);
        mNavListAdapter.notifyDataSetChanged();

        mNavListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentMenuCheckedPos = position;
                mNavListAdapter.notifyDataSetChanged();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                mViewPager.setCurrentItem(mCurrentMenuCheckedPos, false);
            }
        });

    }

    private void initData() {
        list = new ArrayList<NavigationEntity>() {{
            add(new NavigationEntity(R.drawable.ic_visibility_on, "NBA新闻"));
            add(new NavigationEntity(R.drawable.ic_visibility_on, "赛事直播"));
            add(new NavigationEntity(R.drawable.ic_visibility_on, "比赛视频"));
            add(new NavigationEntity(R.drawable.ic_visibility_on, "数据酷"));
            add(new NavigationEntity(R.drawable.ic_visibility_on, "骑士专区"));
            add(new NavigationEntity(R.drawable.ic_visibility_on, "其他"));
        }};

        fragments = new ArrayList<BaseLazyFragment>() {{
            add(new NBANewsFragment());
            add(new NBANewsFragment());
            add(new NBANewsFragment());
            add(new NBANewsFragment());
            add(new NBANewsFragment());
            add(new NBANewsFragment());
        }};

        if (null != fragments && !fragments.isEmpty()) {
            mViewPager.setEnableScroll(false);
            mViewPager.setOffscreenPageLimit(fragments.size());
            mViewPager.setAdapter(new VPFragmentAdapter(getSupportFragmentManager(), fragments));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                if ((System.currentTimeMillis() - DOUBLE_CLICK_TIME) > 2000) {
                    ToastUtils.showSingleToast("再按一次退出");
                    DOUBLE_CLICK_TIME = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
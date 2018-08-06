package com.example.weipeixian.MYYDBG.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.TabFragmentPagerAdapter;
import com.example.weipeixian.MYYDBG.ui.Fragment.FragmentBanjie;
import com.example.weipeixian.MYYDBG.ui.Fragment.FragmentDaiban;
import com.example.weipeixian.MYYDBG.ui.Fragment.FragmentYiban;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class process extends AppCompatActivity {
    private Button add;
    @BindView(R.id.tab_layout1)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private List<String> mTitleList = Arrays.asList("待办事宜","已办事宜","办结事宜");
    private List<Fragment> mFragmentList = new ArrayList<>();
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow);
        ButterKnife.bind(this);
        initFragmentViewPager();
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(process.this,add_process.class));
            }
        });
    }
    private void initFragmentViewPager(){
        mFragmentList.add(new FragmentDaiban());
        mFragmentList.add(new FragmentYiban());
        mFragmentList.add(new FragmentBanjie());
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(
                getSupportFragmentManager(),
                mTitleList,
                mFragmentList);
        mViewPager.setAdapter(mTabFragmentPagerAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewPager);

    }
}

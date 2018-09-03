package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.TabFragmentPagerAdapter;
import com.example.weipeixian.MYYDBG.ui.Fragment.process.FragmentBanjie;
import com.example.weipeixian.MYYDBG.ui.Fragment.process.FragmentDaiban;
import com.example.weipeixian.MYYDBG.ui.Fragment.process.FragmentYiban;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class process extends AppCompatActivity {
    private TextView setup;
    @BindView(R.id.add)
    Button add;
    private TextView monitor;
    @BindView(R.id.tab_layout1)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.return5)
    ImageButton back;
    @BindView(R.id.tx2)
    TextView myflow;
    private List<String> mTitleList = Arrays.asList("待办事宜","已办事宜","办结事宜");
    private List<Fragment> mFragmentList = new ArrayList<>();
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initFragmentViewPager();
        setup = findViewById(R.id.tx1);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(process.this, process_manage.class));
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(process.this, add_flow.class));
            }
        });
        myflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(process.this, my_flow.class));
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

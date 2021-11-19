package com.be.solo.qr.code.scanner.SoloFragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloAdapters.SoloHistoryAdapter;
import com.be.solo.qr.code.scanner.SoloModels.SoloHistoryModel;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SoloHistoryFragment extends Fragment {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabItem generated, scanned;
    private PagerAdapter pagerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.histroy_fragment, container, false);
        innit(view);

        getActivity().setTitle("History");
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        pagerAdapter = new com.be.solo.qr.code.scanner.SoloAdapters.PagerAdapter(getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0|| tab.getPosition()==1)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return view;
    }
    private void innit(View view) {

        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.custom_tab_layout);
        generated = view.findViewById(R.id.explore_tabs);
        scanned = view.findViewById(R.id.my_topic);
    }




}

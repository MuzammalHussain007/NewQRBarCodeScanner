package com.be.solo.qr.code.scanner.SoloAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.be.solo.qr.code.scanner.SoloFragments.Generated;
import com.be.solo.qr.code.scanner.SoloFragments.ScannedFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private int tabCount ;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior ;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
            {
                return  new Generated();
            }
            case 1:
            {
                return new ScannedFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

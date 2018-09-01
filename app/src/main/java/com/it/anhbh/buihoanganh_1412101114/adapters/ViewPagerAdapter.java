package com.it.anhbh.buihoanganh_1412101114.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.it.anhbh.buihoanganh_1412101114.fragments.EducationFragment;
import com.it.anhbh.buihoanganh_1412101114.fragments.NewsDayFragment;
import com.it.anhbh.buihoanganh_1412101114.fragments.SportFragment;
import com.it.anhbh.buihoanganh_1412101114.fragments.WorldFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private ArrayList<String> fragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

        fragmentList.add(new NewsDayFragment());
        fragmentList.add(new WorldFragment());
        fragmentList.add(new SportFragment());
        fragmentList.add(new EducationFragment());

        fragmentTitleList.add("Tin trong ngày");
        fragmentTitleList.add("Thế giới");
        fragmentTitleList.add("Thể thao");
        fragmentTitleList.add("Giáo dục");
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}

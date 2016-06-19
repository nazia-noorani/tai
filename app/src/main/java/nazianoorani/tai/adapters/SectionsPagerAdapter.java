package nazianoorani.tai.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


import nazianoorani.tai.AnalyticsFragment;
import nazianoorani.tai.CoursesFragment;
import nazianoorani.tai.MainFragment;
import nazianoorani.tai.TestFragment;

/**
 * Created by nazianoorani on 24/02/16.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new CoursesFragment();
            case 2:
                return new TestFragment();
            case 3:
                return new AnalyticsFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int pos) {
        return registeredFragments.get(pos);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "Courses";
            case 2:
                return "Tests";
            case 3:
                return "Analytics";
        }
        return null;
    }
}
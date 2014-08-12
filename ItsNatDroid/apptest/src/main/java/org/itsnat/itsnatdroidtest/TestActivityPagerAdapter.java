package org.itsnat.itsnatdroidtest;

/**
 * Created by jmarranz on 12/08/14.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TestActivityPagerAdapter extends FragmentPagerAdapter
{
    protected ArrayList<TestActivityTabFragment> fragmentArray;
    protected Resources resources;

    public TestActivityPagerAdapter(FragmentManager fm,Resources resources) {
        super(fm);

        this.resources = resources;

        this.fragmentArray = new ArrayList<TestActivityTabFragment>(getCount());
        for(int i = 0; i < getCount(); i++)
            fragmentArray.add( TestActivityTabFragment.newInstance(i + 1) );
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragmentArray.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return resources.getString(R.string.title_tab1).toUpperCase(l);
            case 1:
                return resources.getString(R.string.title_tab2).toUpperCase(l);
            case 2:
                return resources.getString(R.string.title_tab3).toUpperCase(l);
        }
        return null;
    }

    public int getItemPosition(Object item) {
        // Solución inspirada en:
        // http://stackoverflow.com/questions/10849552/android-viewpager-cant-update-dynamically/10852046#10852046
        // https://code.google.com/p/android/issues/detail?id=19001
        // Si se quieren añadir y/o eliminar tabs es mejor derivar de FragmentStatePagerAdapter (creo)
        TestActivityTabFragment fragment = (TestActivityTabFragment)item;
        if (fragment.changed)
        {
            int res = POSITION_NONE; // Hace que se pida de nuevo en getItem(int) y se revisualice
            fragment.changed = false;
            return res;
        }
        else
        {
            return POSITION_UNCHANGED;
        }
    }
}

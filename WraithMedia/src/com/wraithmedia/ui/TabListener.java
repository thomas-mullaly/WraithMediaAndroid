package com.wraithmedia.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import com.wraithmedia.R;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;

    public TabListener(Activity activity, String tag, Class<T> klass) {
        mActivity = activity;
        mTag = tag;
        mClass = klass;
    }

    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
        if (mFragment == null) {
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            fragmentTransaction.add(R.id.fragment_content, mFragment, mTag);
        } else {
            fragmentTransaction.attach(mFragment);
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (mFragment != null) {
            fragmentTransaction.detach(mFragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // We don't care what happens in this case.
    }
}

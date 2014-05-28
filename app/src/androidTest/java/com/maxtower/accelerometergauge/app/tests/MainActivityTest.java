package com.maxtower.accelerometergauge.app.tests;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.maxtower.accelerometergauge.app.GaugeView;
import com.maxtower.accelerometergauge.app.MainActivity;
import com.maxtower.accelerometergauge.app.MotionGaugeFragment;
import com.maxtower.accelerometergauge.app.R;

/**
 * Created by mxt196 on 5/27/2014.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity mActivity;
    ActionBar mActionBar;
    ActionBar.Tab mTab0, mTab1;
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mActionBar = mActivity.getSupportActionBar();
        mTab0 = mActionBar.getTabAt(0);
        mTab1 = mActionBar.getTabAt(1);
    }

    @UiThreadTest
    public void testGaugeViewNotNull() {
        mActionBar.selectTab(mTab0);
        GaugeView gaugeView = (GaugeView) mActivity.findViewById(R.id.gaugeView);
        assertNotNull(gaugeView);
    }
    @UiThreadTest
    public void testGaugeView2NotNull() {
        assertNotNull(mTab1);
        mActionBar.selectTab(mTab1);
        GaugeView gaugeView = (GaugeView) mActivity.findViewById(R.id.gaugeView);
        assertNotNull(gaugeView);
    }

    @Override
    protected void tearDown() throws Exception {
        mActivity.finish();
        super.tearDown();
    }
}

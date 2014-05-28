package com.maxtower.accelerometergauge.app.tests;

import com.maxtower.accelerometergauge.app.GaugeView;

import junit.framework.TestCase;

/**
 * Created by mxt196 on 5/27/2014.
 */
public class GaugeTestCase extends TestCase {
    public void GaugeTestCase() {    }

    public void testGauge() {
        float test = GaugeView.computeNewIntermediate(50, 70, 3);
        assertEquals(67, test, 0.1);
        test = GaugeView.computeNewIntermediate(70, 50, 3);
        assertEquals(53, test, 0.1);
        test = GaugeView.computeNewIntermediate(5, -10, 3);
        assertEquals(0, test, 0.1);
        test = GaugeView.computeNewIntermediate(1, 2, 3);
        assertEquals(1, test, 0.1);

    }

}

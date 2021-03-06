package com.maxtower.accelerometergauge.app;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MotionGaugeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MotionGaugeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MotionGaugeFragment extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Long mLastRecordedTime = 0L;
    private Float mLastX = 0f, mLastY = 0f, mLastZ = 0f;

    private GaugeView gaugeView;
    private SeekBar mSeekBar;
    private TextView mSeekBarTextView;
    private OnFragmentInteractionListener mListener;

    public static MotionGaugeFragment newInstance() {
        MotionGaugeFragment fragment = new MotionGaugeFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    public MotionGaugeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        //mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        View rootView = inflater.inflate(R.layout.fragment_motion_gauge, container, false);
        gaugeView = (GaugeView) rootView.findViewById(R.id.gaugeView);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // Many sensors return 3 values, one for each axis.
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double speed = (double)(x - mLastX) * (x-mLastX) +(y - mLastY) * (y-mLastY) +(z - mLastZ) * (z-mLastZ);
        speed = Math.sqrt(speed) * 5;
        mLastRecordedTime = event.timestamp;
        mLastX = x;
        mLastY = y;
        mLastZ = z;

        gaugeView.setPosition((float)speed*mSeekBar.getProgress()/2);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //Only enable sensor notifications when app is displayed to save battery
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        //Only enable sensor notifications when app is displayed to save battery
        mSensorManager.unregisterListener(this);
    }


    public void registerSensor() {
        if(mSensorManager != null) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }
    public void unregisterSensor() {
        if(mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

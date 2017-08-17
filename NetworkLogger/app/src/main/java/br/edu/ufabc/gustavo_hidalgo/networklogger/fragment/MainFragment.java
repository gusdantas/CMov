package br.edu.ufabc.gustavo_hidalgo.networklogger.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.util.Calendar;
import java.util.logging.LogRecord;

import br.edu.ufabc.gustavo_hidalgo.networklogger.R;
import br.edu.ufabc.gustavo_hidalgo.networklogger.constants.Constants;
import br.edu.ufabc.gustavo_hidalgo.networklogger.interfaces.OnFragmentInteractionListener;
import br.edu.ufabc.gustavo_hidalgo.networklogger.util.NetLogger;

public class MainFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    TextView mTimeInfoTv, mCellInfoTv, mLocationInfoTv;
    ToggleButton mEnableBt;
    NetLogger mNetLogger;
    Handler mHandler;
    Runnable mRunnable;
    File NETLOG;
    private OnFragmentInteractionListener mListener;

    Calendar mCalendar = Calendar.getInstance();
    public static final String TAG = "[NetLog]MainFragment";

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mTimeInfoTv = (TextView) view.findViewById(R.id.timeInfoTextView);
        mCellInfoTv = (TextView) view.findViewById(R.id.cellInfoTextView);
        mLocationInfoTv = (TextView) view.findViewById(R.id.locationTextView);
        mEnableBt = (ToggleButton) view.findViewById(R.id.enableToggleButton);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                mNetLogger.writeLine();
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                mHandler.postDelayed(this, 2000);
            }
        };

        mEnableBt.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // The toggle is enabled
            String filename = "NL-"+mCalendar.getTime().toString()+".csv";
            filename = filename.replace(" ","-").replace(":","-");
            NETLOG = new File(getActivity().getExternalFilesDir(null), filename);
            mNetLogger = new NetLogger(this, NETLOG);
            mHandler.post(mRunnable);

        } else {
            // The toggle is disabled
            mHandler.removeCallbacks(mRunnable);
            mNetLogger.closeFile();
            mNetLogger = null;
            scanFile(NETLOG, getActivity());
            NETLOG = null;
        }
    }

    public static void scanFile(File file, Context context){
        MediaScannerConnection.scanFile(
                context, new String[]{file.getAbsolutePath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.v(TAG,
                                "file " + path + " was scanned seccessfully: " + uri);
                    }
                });
    }

    public void setText(String time, String cell, String location){
        mTimeInfoTv.setText(time);
        mCellInfoTv.setText(cell);
        mLocationInfoTv.setText(location);
    }
}

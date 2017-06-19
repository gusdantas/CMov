package br.edu.ufabc.gustavo_hidalgo.networklogger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.edu.ufabc.gustavo_hidalgo.networklogger.R;
import br.edu.ufabc.gustavo_hidalgo.networklogger.activity.MainActivity;
import br.edu.ufabc.gustavo_hidalgo.networklogger.interfaces.OnFragmentInteractionListener;

public class MainFragment extends Fragment implements View.OnClickListener {
    static Context context;
    TextView textView;
    Button button;
    static TelephonyManager tm;
    private OnFragmentInteractionListener mListener;
    public static final String TAG = "[NetLog]MainFragment";

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(Context mainActivity) {
        context = mainActivity;
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Log.d(TAG,"");

        textView = (TextView) view.findViewById(R.id.textView);
        button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                getInformation();
                break;
        }
    }

    private void getInformation() {
        List<CellInfo> cellInfos = (List<CellInfo>) tm.getAllCellInfo();

        for(CellInfo cellInfo : cellInfos)
        {
            String cellInfoClass = cellInfo.getClass().getSimpleName();
            switch (cellInfoClass){
                case "CellInfoGsm":
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                    CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                    CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();

                    Log.d(TAG, "registered: "+cellInfoGsm.isRegistered());
                    Log.d(TAG, cellIdentityGsm.toString());
                    Log.d(TAG, cellSignalStrengthGsm.toString());
                    textView.setText(cellIdentityGsm.toString());
                    break;
                case "CellInfoWcdma":
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                    CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                    CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();

                    Log.d(TAG, "registered: "+cellInfoWcdma.isRegistered());
                    Log.d(TAG, cellIdentityWcdma.toString());
                    Log.d(TAG, cellSignalStrengthWcdma.toString());
                    textView.setText(cellIdentityWcdma.toString());
                    break;
                case "CellInfoLte":
                    CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                    CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                    CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();

                    Log.d(TAG, "registered: "+cellInfoLte.isRegistered());
                    Log.d(TAG, cellIdentityLte.toString());
                    Log.d(TAG, cellSignalStrengthLte.toString());
                    textView.setText(cellIdentityLte.toString());
                    break;
            }
        }
    }
}

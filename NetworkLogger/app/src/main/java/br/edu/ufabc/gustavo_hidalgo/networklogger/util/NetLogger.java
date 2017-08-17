package br.edu.ufabc.gustavo_hidalgo.networklogger.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import br.edu.ufabc.gustavo_hidalgo.networklogger.fragment.MainFragment;

/**
 * Created by note on 29/07/17.
 */

public class NetLogger {
    MainFragment mMainFragment;
    static TelephonyManager sTelephonyManager;
    File NETLOG;
    FileWriter mFileWriter;
    String mLocation = "first;first;first;first;first";

    public static final String TAG = "[NetLog]NetLogger";

    public NetLogger(MainFragment mainFragment, File file){
        mMainFragment = mainFragment;
        NETLOG = file;
        sTelephonyManager = (TelephonyManager) mMainFragment.getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        getLocationInfo();
        try {
            mFileWriter = new FileWriter(NETLOG);
            String header = "time;lat;long;acc;alt;spd;celltech;str;mcc;mnc;cid;psc;lac\n";
            mFileWriter.append(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine(){
        StringBuilder stringBuilder = new StringBuilder();
        Long timeStamp = (System.currentTimeMillis()/1000);
        stringBuilder.append(timeStamp.toString()).append(";");
        stringBuilder.append(mLocation).append(";");
        stringBuilder.append(getCellInfo()).append("\n");
        mMainFragment.setText(timeStamp.toString(), mLocation, getCellInfo());
        try {
            mFileWriter.append(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeFile(){
        try {
            mFileWriter.flush();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCellInfo() {
        StringBuilder cellInfoString = new StringBuilder();
        List<CellInfo> cellInfos = (List<CellInfo>) sTelephonyManager.getAllCellInfo();

        for(CellInfo cellInfo : cellInfos)
        {
            if (cellInfo.isRegistered()) {
                String cellInfoClass = cellInfo.getClass().getSimpleName();
                switch (cellInfoClass) {
                    case "CellInfoGsm":
                        CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                        CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                        CellSignalStrengthGsm cellSignalStrengthGsm =
                                cellInfoGsm.getCellSignalStrength();

                        Log.d(TAG, "registered: " + cellInfoGsm.isRegistered());
                        Log.d(TAG, cellIdentityGsm.toString());
                        Log.d(TAG, cellSignalStrengthGsm.toString());
                        cellInfoString.append("gsm;")
                                .append(cellSignalStrengthGsm.getDbm()).append(";")
                                .append(cellIdentityGsm.getMcc()).append(";")
                                .append(cellIdentityGsm.getMnc()).append(";")
                                .append(cellIdentityGsm.getCid()).append(";")
                                .append(cellIdentityGsm.getPsc()).append(";")
                                .append(cellIdentityGsm.getLac()).append("\n");
                        break;
                    case "CellInfoWcdma":
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                        CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                        CellSignalStrengthWcdma cellSignalStrengthWcdma =
                                cellInfoWcdma.getCellSignalStrength();

                        Log.d(TAG, "registered: " + cellInfoWcdma.isRegistered());
                        Log.d(TAG, cellIdentityWcdma.toString());
                        Log.d(TAG, cellSignalStrengthWcdma.toString());
                        cellInfoString.append("wcdma;")
                                .append(cellSignalStrengthWcdma.getDbm()).append(";")
                                .append(cellIdentityWcdma.getMcc()).append(";")
                                .append(cellIdentityWcdma.getMnc()).append(";")
                                .append(cellIdentityWcdma.getCid()).append(";")
                                .append(cellIdentityWcdma.getPsc()).append(";")
                                .append(cellIdentityWcdma.getLac()).append("\n");
                        break;
                    case "CellInfoLte":
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                        CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                        CellSignalStrengthLte cellSignalStrengthLte =
                                cellInfoLte.getCellSignalStrength();

                        Log.d(TAG, "registered: " + cellInfoLte.isRegistered());
                        Log.d(TAG, cellIdentityLte.toString());
                        Log.d(TAG, cellSignalStrengthLte.toString());
                        cellInfoString.append("lte;")
                                .append(cellSignalStrengthLte.getDbm()).append(";")
                                .append(cellIdentityLte.getMcc()).append(";")
                                .append(cellIdentityLte.getMnc()).append(";")
                                .append(cellIdentityLte.getCi()).append(";")
                                .append(cellIdentityLte.getPci()).append(";")
                                .append(cellIdentityLte.getTac()).append("\n");
                        break;
                }
            }
        }
        return cellInfoString.toString();
    }

    private void getLocationInfo() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) mMainFragment.getActivity()
                .getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to getLocationInfo updates
        android.location.LocationListener locationListener = new android.location.LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new getLocationInfo is found by the network getLocationInfo provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive getLocationInfo updates
        if (ActivityCompat.checkSelfPermission(mMainFragment.getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mMainFragment.getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 50, locationListener);
    }

    private void makeUseOfNewLocation(Location location){
        mLocation = String.valueOf(location.getLatitude()) + ";" +
                location.getLongitude() + ";" +
                location.getAccuracy() + ";" +
                location.getAltitude() + ";" +
                location.getSpeed();
    }
}

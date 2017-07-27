package br.edu.ufabc.gustavo_hidalgo.networklogger.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import br.edu.ufabc.gustavo_hidalgo.networklogger.R;
import br.edu.ufabc.gustavo_hidalgo.networklogger.constants.Constants;
import br.edu.ufabc.gustavo_hidalgo.networklogger.fragment.MainFragment;
import br.edu.ufabc.gustavo_hidalgo.networklogger.interfaces.OnFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{
    public static final int PERMISSIONS_REQUEST = 1;
    public Fragment mFragment;
    public FragmentManager mFragmentManager;
    public FragmentTransaction mFragmentTransaction;
    String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String TAG = "[NetLog]MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getFragmentManager();

        // Here, thisActivity is the current activity
        for (String permission:PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
        onFragmentInteraction(Constants.MAIN_FRAGMENT);
    }

    @Override
    public void onFragmentInteraction(int fragment) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        Log.d(TAG,String.valueOf(fragment)+" - changing frag");
        switch (fragment){
            case Constants.MAIN_FRAGMENT:
                mFragment = MainFragment.newInstance();
                break;
        }
        mFragmentTransaction.replace(R.id.frame, mFragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

package com.example.waldo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class FragmentScan extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getContext());
        return mScannerView;
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }



    @Override
    public void handleResult(com.google.zxing.Result result) {
        // Do something with the result here
        Log.e("tag","vfvfv"+result.getText()); // Prints scan results
        Log.v("tag", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)



        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);
    }
}

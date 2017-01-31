package com.mengroba.barcodecamerascan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.client.android.Intents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.sudar.zxingorient.Barcode;
import me.sudar.zxingorient.ZxingOrient;

/**
 * Created by mengroba on 30/01/2017.
 */

public class BarcodeScanner {

    public static final int REQUEST_CODE = 0x0000c0de; // Only use bottom 16 bits
    private static final String TAG = ZxingOrient.class.getSimpleName();

    private static final String BS_PACKAGE = "me.sudar.zxing";

    private final Activity activity;

//    private boolean autoFocus = true;
//    private boolean flash = false;

    private boolean vibrate;
    private boolean playBeep;

    private Integer iconID;

    private final Map<String,Object> moreExtras = new HashMap<String,Object>(3);


    public BarcodeScanner(Activity activity) {
        this.activity = activity;
        initialize();
    }

    private void initialize(){
        vibrate = false;
        playBeep = true;
        iconID = null;
    }

    public BarcodeScanner setBeep(boolean setting){
        this.playBeep= setting;
        return this;
    }

    public BarcodeScanner setIcon(int iconID){
        this.iconID = iconID;
        return this;
    }

    public final void initiateScan(Collection<String> desiredBarcodeFormats, int cameraId) {
        Intent intentScan = new Intent(BS_PACKAGE + ".SCAN");
        intentScan.addCategory(Intent.CATEGORY_DEFAULT);

        intentScan.putExtra(Intents.Scan.VIBRATE, vibrate);
        intentScan.putExtra((Intents.Scan.BEEP), playBeep);

        // check which types of codes to scan for
        if (desiredBarcodeFormats != null) {
            // set the desired barcode types
            StringBuilder joinedByComma = new StringBuilder();
            for (String format : desiredBarcodeFormats) {
                if (joinedByComma.length() > 0) {
                    joinedByComma.append(',');
                }
                joinedByComma.append(format);
            }
            intentScan.putExtra("SCAN_FORMATS", joinedByComma.toString());
        }

        // check requested camera ID
        if (cameraId >= 0) {
            intentScan.putExtra("SCAN_CAMERA_ID", cameraId);
        }

        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        attachMoreExtras(intentScan);
        startActivityForResult(intentScan, REQUEST_CODE);
    }

    public void startActivityForResult(Intent intent, int code) {
            activity.startActivityForResult(intent, code);
    }


    public static PortraitOrientation parseActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");
                byte[] rawBytes = intent.getByteArrayExtra("SCAN_RESULT_BYTES");
                int intentOrientation = intent.getIntExtra("SCAN_RESULT_ORIENTATION", Integer.MIN_VALUE);
                Integer orientation = intentOrientation == Integer.MIN_VALUE ? null : intentOrientation;
                String errorCorrectionLevel = intent.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
                return new PortraitOrientation(contents,
                        formatName,
                        rawBytes,
                        orientation,
                        errorCorrectionLevel);
            }
            return new PortraitOrientation();
        }
        return null;
    }



    public final void shareText(CharSequence text) {
        shareText(text, "TEXT_TYPE");
    }


    public final void shareText(CharSequence text, CharSequence type) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(BS_PACKAGE + ".ENCODE");
        intent.putExtra("ENCODE_TYPE", type);
        intent.putExtra("ENCODE_DATA", text);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        attachMoreExtras(intent);
            activity.startActivity(intent);
    }

    private void attachMoreExtras(Intent intent) {
        for (Map.Entry<String,Object> entry : moreExtras.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            // Kind of hacky
            if (value instanceof Integer) {
                intent.putExtra(key, (Integer) value);
            } else if (value instanceof Long) {
                intent.putExtra(key, (Long) value);
            } else if (value instanceof Boolean) {
                intent.putExtra(key, (Boolean) value);
            } else if (value instanceof Double) {
                intent.putExtra(key, (Double) value);
            } else if (value instanceof Float) {
                intent.putExtra(key, (Float) value);
            } else if (value instanceof Bundle) {
                intent.putExtra(key, (Bundle) value);
            } else {
                intent.putExtra(key, value.toString());
            }
        }
    }

}

package technicalbird.myqrscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.scan_qr_code_button)
    Button scanButton;
    @Bind(R.id.result_viewer)
    TextView resultViewer;
    @Bind(R.id.webview_for_diplaying_data)
    WebView scannedQRResultShower;
    private static final String TAG = "TAG";
    private String resultWebsiteName = "";
    private static final int RC_BARCODE_CAPTURE = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.bird_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        scannedQRResultShower.setVisibility(View.INVISIBLE);
        logUser();
    }

    //method for crash analytics provided by fabric library
    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier("12345");
        Crashlytics.setUserEmail("user@fabric.io");
        Crashlytics.setUserName("Test User");
    }

    @OnClick(R.id.scan_qr_code_button)
    public void onClick(View view) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

/*
    //generated method from fabric library
    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }
*/

    //get result back to parent Activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    //barcode scanned successfully
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    scanButton.setText("Scan Another QR Code");
                    resultViewer.setText(barcode.displayValue);
                    scannedQRResultShower.setVisibility(View.VISIBLE);
                    resultWebsiteName = "https://www.google.com/search?q=" + barcode.displayValue;
                    scannedQRResultShower.getSettings().setJavaScriptEnabled(true);
                    scannedQRResultShower.loadUrl(resultWebsiteName);
                    scannedQRResultShower.setWebViewClient(new WebViewController());
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    //no barcode captured
                    resultViewer.setText("No barcode captured");
                    scannedQRResultShower.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                //error in reading barcode
                scannedQRResultShower.setVisibility(View.INVISIBLE);
                resultViewer.setText("Error in Reading barcode");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

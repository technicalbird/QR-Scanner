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
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCaptureActivity;
import com.google.android.gms.vision.barcode.Barcode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.title_qr_scanner)
    TextView titleQRScanner;
    @Bind(R.id.scan_qr_code_button)
    Button scanButton;
    @Bind(R.id.result_viewer)
    TextView resultViewer;
    @Bind(R.id.webview_for_diplaying_data)
    WebView webView;
    private static final int RC_BARCODE_CAPTURE = 900;
    private static final String TAG = "TAG";
    private String resultURL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bird_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        webView.setVisibility(View.INVISIBLE);
    }
    @OnClick(R.id.scan_qr_code_button)
    public void onClick(View view) {
        Intent intent=new Intent(this,BarcodeCaptureActivity.class);
        startActivityForResult(intent,RC_BARCODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    scanButton.setText("Scan Another QR Code");
                    resultViewer.setText(barcode.displayValue);
                    webView.setVisibility(View.VISIBLE);
                    resultURL = "https://www.google.com/search?q=" + barcode.displayValue;
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.loadUrl(resultURL);
                    webView.setWebViewClient(new WebViewController());
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    resultViewer.setText(com.google.android.gms.samples.vision.barcodereader.R.string.barcode_failure);
                    webView.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                webView.setVisibility(View.INVISIBLE);
                resultViewer.setText(String.format(getString(com.google.android.gms.samples.vision.barcodereader.R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

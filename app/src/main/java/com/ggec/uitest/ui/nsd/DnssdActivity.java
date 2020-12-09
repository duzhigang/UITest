package com.ggec.uitest.ui.nsd;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.ggec.uitest.R;
import com.github.druk.dnssd.BrowseListener;
import com.github.druk.dnssd.DNSSD;
import com.github.druk.dnssd.DNSSDBindable;
import com.github.druk.dnssd.DNSSDException;
import com.github.druk.dnssd.DNSSDService;
import com.github.druk.dnssd.ResolveListener;

import java.util.Map;

public class DnssdActivity extends FragmentActivity {
    private static final String TAG = "DnssdActivity";
    private DNSSD dnssd;

    private DNSSDService browseService;
    private Button btnStart;

//    private DnssdClient dnssdClient;
//    private RxDnssdClient rxDnssdClient;
//    private Rx2DnssdClient rx2DnssdClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnnsd);

//        dnssdClient = new DnssdClient(this);
//        rxDnssdClient = new RxDnssdClient(this);
//        rx2DnssdClient = new Rx2DnssdClient(this);

        dnssd = new DNSSDBindable(this);

        btnStart = findViewById(R.id.btn_dnssd_start_browse);
        btnStart.setOnClickListener(view -> {
            if (browseService == null) {
                btnStart.setText("Stop");
                startBrowse();
            } else {
                btnStart.setText("Start");
                stopBrowse();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        dnssdClient.startBrowse();
//        rxDnssdClient.startBrowse();
//        rx2DnssdClient.startBrowse();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        dnssdClient.stopBrowse();
//        rxDnssdClient.stopBrowse();
//        rx2DnssdClient.stopBrowse();
    }

    @Override
    protected void onResume() {
        Log.v(TAG,"onResume");
        super.onResume();
        if (browseService == null) {
            btnStart.setText("Stop");
            startBrowse();
        }
    }

    @Override
    protected void onPause() {
        Log.v(TAG,"onPause");
        super.onPause();
        if (browseService != null) {
            btnStart.setText("Start");
            stopBrowse();
        }
    }

    private void startBrowse() {
        Log.i(TAG, "start browse");
        try {
            browseService = dnssd.browse("_ggec-iar._tcp.", new BrowseListener() {
                @Override
                public void serviceFound(DNSSDService browser, int flags, int ifIndex, final String serviceName, String regType, String domain) {
                    Log.d(TAG, "Found " + serviceName);
                    startResolve(flags, ifIndex, serviceName, regType, domain);
                }

                @Override
                public void serviceLost(DNSSDService browser, int flags, int ifIndex, String serviceName, String regType, String domain) {
                    Log.d(TAG, "serviceLost " + serviceName);
                }

                @Override
                public void operationFailed(DNSSDService service, int errorCode) {
                    Log.e(TAG, "error: " + errorCode);
                }
            });
        } catch (DNSSDException e) {
            e.printStackTrace();
            Log.e(TAG, "error", e);
        }
    }

    private void startResolve(int flags, int ifIndex, final String serviceName, final String regType, final String domain) {
        try {
            dnssd.resolve(flags, ifIndex, serviceName, regType, domain, new ResolveListener() {
                @Override
                public void serviceResolved(DNSSDService resolver, int flags, int ifIndex, String fullName, String hostName, int port, Map<String, String> txtRecord) {
                    Log.d(TAG, "serviceResolved fullName = " + fullName + ",hostName = " + hostName + ",port = " + port + ",txtRecord = " + txtRecord.toString());
                }

                @Override
                public void operationFailed(DNSSDService service, int errorCode) {
                    Log.d(TAG, "operationFailed ,errorCode = " + errorCode);
                }
            });
        } catch (DNSSDException e) {
            e.printStackTrace();
        }
    }

    private void stopBrowse() {
        Log.d(TAG, "Stop browsing");
        browseService.stop();
        browseService = null;
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG,"onDestroy");
        super.onDestroy();
        if (browseService != null) {
            browseService.stop();
        }
    }
}
package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.util.Log;

import com.github.druk.dnssd.BrowseListener;
import com.github.druk.dnssd.DNSSD;
import com.github.druk.dnssd.DNSSDBindable;
import com.github.druk.dnssd.DNSSDException;
import com.github.druk.dnssd.DNSSDService;

public class DnssdClient {
    private static final String TAG = "DnssdClient";
    private final String SERVER_TYPE = "_ggec-iar._tcp.";
    private DNSSD dnssd;
    private DNSSDService browseService;

    public DnssdClient(Context context) {
        dnssd = new DNSSDBindable(context);
    }

    public void startBrowse() {
        Log.i(TAG, "start browse");
        try {
            browseService = dnssd.browse(SERVER_TYPE, new BrowseListener() {
                @Override
                public void serviceFound(DNSSDService browser, int flags, int ifIndex, final String serviceName, String regType, String domain) {
                    Log.d(TAG, "Found " + serviceName);
//                    startResolve(flags, ifIndex, serviceName, regType, domain);
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

    public void stopBrowse() {
        Log.i(TAG, "stop browse");
        if (browseService != null) {
            browseService.stop();
            browseService = null;
        }
    }
}

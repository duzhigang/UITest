package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.util.Log;

import com.github.druk.rx2dnssd.Rx2Dnssd;
import com.github.druk.rx2dnssd.Rx2DnssdBindable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Rx2DnssdClient {
    private static final String TAG = "Rx2DnssdClient";
    private final String SERVER_TYPE = "_ggec-iar._tcp.";
    private Rx2Dnssd rx2Dnssd;
    private Disposable browseDisposable;

    public Rx2DnssdClient(Context context) {
        rx2Dnssd = new Rx2DnssdBindable(context);
    }

    public void startBrowse() {
        browseDisposable = rx2Dnssd.browse(SERVER_TYPE, "local.")
                .compose(rx2Dnssd.resolve())
                .compose(rx2Dnssd.queryRecords())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bonjourService -> {
                    Log.d(TAG, bonjourService.toString());
                    if (bonjourService.isLost()) {
                        Log.d(TAG, "bonjourService is Lost");
                    } else {
                        Log.d(TAG, "bonjourService is not Lost");
                    }
                }, throwable -> Log.e(TAG, "error", throwable));
    }

    public void stopBrowse() {
        if (browseDisposable != null) {
            browseDisposable.dispose();
            browseDisposable = null;
        }
    }
}

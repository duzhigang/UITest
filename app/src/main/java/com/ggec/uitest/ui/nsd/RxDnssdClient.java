package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.util.Log;

import com.github.druk.rxdnssd.BonjourService;
import com.github.druk.rxdnssd.RxDnssd;
import com.github.druk.rxdnssd.RxDnssdBindable;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 发现不了设备的概率要比用DNSSD库的概率要大
 * */
public class RxDnssdClient {
    private static final String TAG = "RxDnssdClient";
    private final String SERVER_TYPE = "_ggec-iar._tcp.";
    private RxDnssd rxdnssd;
    private Subscription subscription;

    public RxDnssdClient(Context context) {
        rxdnssd = new RxDnssdBindable(context);
    }

    public void startBrowse() {
        Log.i(TAG, "start browse");
        subscription = rxdnssd.browse(SERVER_TYPE, "local.")
                .compose(rxdnssd.resolve())
                .compose(rxdnssd.queryRecords())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BonjourService>() {
                    @Override
                    public void call(BonjourService bonjourService) {
                        Log.d(TAG, bonjourService.toString());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("TAG", "error", throwable);
                    }
                });
    }

    public void stopBrowse() {
        Log.i(TAG, "stop browse");
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
}

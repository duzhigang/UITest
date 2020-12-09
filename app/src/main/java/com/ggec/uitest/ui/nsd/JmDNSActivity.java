package com.ggec.uitest.ui.nsd;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.ggec.uitest.R;

import org.json.JSONArray;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

/**
 * 使用很复杂，目前还没有成功测试发现设备
 * JmDNS库在Android平台上效率欠佳
 * */
public class JmDNSActivity extends FragmentActivity {
    private static final String TAG = "JmDNSActivity";
    private Button btnStart;
    private WifiManager.MulticastLock lock;
    private Handler handler = new android.os.Handler();

    private String type = "_workstation._tcp.local.";
    //    private String type = "_ggec-iar._tcp.";
    private JmDNS jmdns = null;
    private ServiceListener listener = null;
    private ServiceInfo serviceInfo;
    private JmdnsDiscover jmdnsDiscover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnnsd);

        btnStart = findViewById(R.id.btn_dnssd_start_browse);
        jmdnsDiscover = new JmdnsDiscover(this);
        btnStart.setOnClickListener(v -> {
            jmdnsDiscover.startSearch("test", new JmdnsDiscover.MdnsCallback() {
                @Override
                public void onDeviceFind(JSONArray jsonArray) {
                    Log.v(TAG,"onDeviceFind:" + jsonArray.toString());
                }
            });
//            start();
        });
    }

    private void notifyUser(final String msg) {
        handler.postDelayed(new Runnable() {
            public void run() {
                btnStart.setText(msg+"\n=== start");
            }
        }, 1);

    }

    private InetAddress getLocalIpAddress(WifiManager wifiManager) throws UnknownHostException {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int intAddr = wifiInfo.getIpAddress();
        byte[] byteaddr = new byte[]{
                (byte) (intAddr & 255),
                (byte) (intAddr >> 8 & 255),
                (byte) (intAddr >> 16 & 255),
                (byte) (intAddr >> 24 & 255)};
        return InetAddress.getByAddress(byteaddr);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        jmdnsDiscover.stopSearch();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class SampleListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            // Required to force serviceResolved to be called again (after the first search)
            Log.v(TAG,"Service added: " + event.getInfo());
            jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            Log.v(TAG,"Service removed: " + event.getInfo());
            notifyUser("Service removed: " + event.getName());
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            Log.v(TAG,"Service resolved: " + event.getInfo());
            String additions = "";
            if (event.getInfo().getInetAddresses() != null && event.getInfo().getInetAddresses().length > 0) {
                additions = event.getInfo().getInetAddresses()[0].getHostAddress();
            }
            notifyUser("Service resolved: " + event.getInfo().getQualifiedName() + " port:" + event.getInfo().getPort() + additions);
        }
    }
}

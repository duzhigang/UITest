package com.ggec.uitest.ui.nsd;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class JmdnsDiscover {
    private static final String TAG = "MdnsDiscover";
        private Context mContext;
        private String mServiceName;
        private JmDNS mJmdns;
        private MdnsSearchThread mSearchThread;
        private MdnsCallback mCallback;
        private Map<String, JSONObject> jsonMap = new HashMap<>();

        public JmdnsDiscover(Context context) {
            mContext = context;
        }

        public void startSearch(String serviceName, MdnsCallback callback) {
            mCallback = callback;
            if (mSearchThread != null && mSearchThread.isRunning()) {
                if (Objects.equals(mServiceName, serviceName)) {
                    return;
                }
                mSearchThread.interrupt();
            }
            mServiceName = serviceName;
            mSearchThread = new MdnsSearchThread();
            mSearchThread.start();
        }

        public void stopSearch() {
            if (mSearchThread != null && mSearchThread.isRunning()) {
                mSearchThread.interrupt();
                mSearchThread = null;
            }
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

        private class MdnsSearchThread extends Thread {

            private volatile boolean running = false;

            @Override
            public void run() {
                WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                assert wifiManager != null;
                WifiManager.MulticastLock multicastLock = wifiManager.createMulticastLock(getClass().getName());
                multicastLock.setReferenceCounted(false);
                multicastLock.acquire();//to receive multicast packets
                try {
                    ServiceListener listener = new JmdnsListener();
                    boolean crash = false;
                    while (running) {
                        try {
                            if (crash) {
                                Thread.sleep(3000L);
                            }
                            jsonMap.clear();
                            //
                            InetAddress addr = getLocalIpAddress(wifiManager);
                            //
                            mJmdns = JmDNS.create(addr);
                            mJmdns.addServiceListener(mServiceName, listener);
                            Thread.sleep(3000L);
                            mJmdns.removeServiceListener(mServiceName, listener);
                            mJmdns.close();
                            //
                            if (running) {
                                sendCallback();
                            }
                            crash = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                            crash = true;
                        }
                    }
                } finally {
                    multicastLock.release();
                }
            }

            @Override
            public synchronized void start() {
                running = true;
                super.start();
            }

            @Override
            public void interrupt() {
                running = false;
                super.interrupt();
            }

            public boolean isRunning() {
                return running;
            }
        }

        private class JmdnsListener implements ServiceListener {

            public void serviceAdded(ServiceEvent ev) {
                Log.i(TAG,"serviceAdded, info = " + ev.getInfo().toString());
                mJmdns.requestServiceInfo(ev.getType(), ev.getName(), 1);
            }

            public void serviceRemoved(ServiceEvent ev) {
                Log.i(TAG,"serviceRemoved, info = " + ev.getInfo().toString());
                jsonMap.remove(ev.getName());
            }

            public void serviceResolved(ServiceEvent ev) {
                Log.i(TAG,"serviceResolved, info = " + ev.getInfo().toString());
                if (!jsonMap.containsKey(ev.getName())) {
                    JSONObject jsonObj = toJsonObject(ev.getInfo());
                    jsonMap.put(ev.getName(), jsonObj);
                }
            }
        }

        private void sendCallback() {
            if (mCallback != null) {
                JSONArray jsonArray = new JSONArray();
                for (JSONObject jsonObj : jsonMap.values()) {
                    jsonArray.put(jsonObj);
                }
                mCallback.onDeviceFind(jsonArray);
            }
        }

        /**
         * mDNS数据格式解析
         */
        private JSONObject toJsonObject(ServiceInfo sInfo) {
            JSONObject jsonObj;
            try {
                jsonObj = new JSONObject();
                String ipv4 = "";
                if (sInfo.getInet4Addresses().length > 0) {
                    ipv4 = sInfo.getInet4Addresses()[0].getHostAddress();
                }

                jsonObj.put("Name", sInfo.getName());
                jsonObj.put("IP", ipv4);
                jsonObj.put("Port", sInfo.getPort());

                byte[] allInfo = sInfo.getTextBytes();
                int allLen = allInfo.length;
                byte fLen;
                for (int index = 0; index < allLen; index += fLen) {
                    fLen = allInfo[index++];
                    byte[] fData = new byte[fLen];
                    System.arraycopy(allInfo, index, fData, 0, fLen);

                    String fInfo = new String(fData, StandardCharsets.UTF_8);
                    if (fInfo.contains("=")) {
                        String[] temp = fInfo.split("=");
                        jsonObj.put(temp[0], temp[1]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                jsonObj = null;
            }
            return jsonObj;
        }


    interface MdnsCallback {
        void onDeviceFind(JSONArray jsonArray);
    }
}

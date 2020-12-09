package com.ggec.uitest.ui.nsd;

import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ggec.uitest.R;

public class NsdChatActivity extends FragmentActivity {
    NsdHelper mNsdHelper;
    private TextView mStatusView;
    private Handler mUpdateHandler;
    public static final String TAG = "NsdChat";
    ChatConnection mConnection;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Creating chat activity");
        setContentView(R.layout.activity_nsd_chat);
        mStatusView = findViewById(R.id.tv_act_nsd_chat_status);
        Button btnAdvertise = findViewById(R.id.btn_act_nsd_chat_register_service);
        btnAdvertise.setOnClickListener(v -> {
            // Register service
            if(mConnection.getLocalPort() > -1) {
                mNsdHelper.registerService(mConnection.getLocalPort());
            } else {
                Log.d(TAG, "ServerSocket isn't bound.");
            }
        });

        Button btnDiscover = findViewById(R.id.btn_act_nsd_chat_client_discover);
        btnDiscover.setOnClickListener(v -> {
            mNsdHelper.discoverServices();
        });

        Button btnConnect = findViewById(R.id.btn_act_nsd_connect);
        btnConnect.setOnClickListener(v -> {
            NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
            if (service != null) {
                Log.d(TAG, "Connecting.");
                mConnection.connectToServer(service.getHost(),
                        service.getPort());
            } else {
                Log.d(TAG, "No service to connect to!");
            }
        });

        Button btnSend = findViewById(R.id.btn_act_nsd_chat_send);
        btnSend.setOnClickListener(v -> {
            EditText messageView = this.findViewById(R.id.et_act_nsd_chat_send);
            if (messageView != null) {
                String messageString = messageView.getText().toString();
                if (!messageString.isEmpty()) {
                    mConnection.sendMessage(messageString);
                }
                messageView.setText("");
            }
        });

        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");
                addChatLine(chatLine);
            }
        };
    }

    public void addChatLine(String line) {
        mStatusView.append("\n" + line);
    }
    @Override
    protected void onStart() {
        Log.d(TAG, "Starting.");
        mConnection = new ChatConnection(mUpdateHandler);
        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();
        super.onStart();
    }
    @Override
    protected void onPause() {
        Log.d(TAG, "Pausing.");
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
        super.onPause();
    }
    @Override
    protected void onResume() {
        Log.d(TAG, "Resuming.");
        super.onResume();
        if (mNsdHelper != null) {
            mNsdHelper.discoverServices();
        }
    }
    // For KitKat and earlier releases, it is necessary to remove the
    // service registration when the application is stopped.  There's
    // no guarantee that the onDestroy() method will be called (we're
    // killable after onStop() returns) and the NSD service won't remove
    // the registration for us if we're killed.
    // In L and later, NsdService will automatically unregister us when
    // our connection goes away when we're killed, so this step is
    // optional (but recommended).
    @Override
    protected void onStop() {
        Log.d(TAG, "Being stopped.");
        mNsdHelper.tearDown();
        mConnection.tearDown();
        mNsdHelper = null;
        mConnection = null;
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG, "Being destroyed.");
        super.onDestroy();
    }
}

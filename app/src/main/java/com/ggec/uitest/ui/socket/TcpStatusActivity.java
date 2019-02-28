package com.ggec.uitest.ui.socket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ggec.uitest.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 这个类用于测试NIO中Socket通信时，在Client端主动断开连接/断开WiFi/断开WiFi又重连上该WiFi、
 * Server端主动断开连接/退出软件/关闭WiFi这几种场景时socket的状态及事件。
 * */
public class TcpStatusActivity extends FragmentActivity {
    private static final String TAG = "TcpStatusActivity";

    private String dstIp = "192.168.34.12";
    private int dstPort = 60000;
    private Selector mSelector;
    private SocketChannel mChannel;
    public static final int TIME_OUT = 10000; // 默认链接超时时间10s
    private SelectorHelper selectorHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_status);
        Button btnTcpStart = findViewById(R.id.btn_tcp_status_activity_start);
        btnTcpStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"启动Tcp连接");
                initialize();
            }
        });

        Button btnTcpClose = findViewById(R.id.btn_tcp_status_activity_close);
        btnTcpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"断开Tcp连接");
                try {
                    mChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            mSelector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectorHelper = new SelectorHelper(mSelector);
        startSelector();
    }

    private void initialize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }).start();
    }

    private void connect(){
        try {
//            mChannel = SocketChannel.open(new InetSocketAddress(dstIp, dstPort));

            mChannel = SocketChannel.open();
/*            boolean isAlive = mChannel.getOption(StandardSocketOptions.SO_KEEPALIVE);   // 客户端是否定期利用空闲的连接向服务器发送一个数据包，为了检测服务器是否仍处于活动状态，默认false。
            boolean isReuseAddr = mChannel.getOption(StandardSocketOptions.SO_REUSEADDR);   //端口是否重用，默认false
            boolean isTcpNoDelay = mChannel.getOption(StandardSocketOptions.TCP_NODELAY);   //数据包是否立即发送，默认false
            int recSize = mChannel.getOption(StandardSocketOptions.SO_RCVBUF);  // 输入流的接收缓冲区Java默认建议8KB
            int sendSize = mChannel.getOption(StandardSocketOptions.SO_SNDBUF); // 输出流的接收缓冲区Java默认建议8KB
            int lingerSize = mChannel.getOption(StandardSocketOptions.SO_LINGER);   // 默认-1表示SO_LINGER是关闭的
            int timeOut = mChannel.socket().getSoTimeout(); // 读取数据超时时长，默认0
            Log.i(TAG,"isAlive = " + isAlive + ",isReuseAddr = " + isReuseAddr + ",isTcpNoDelay = " + isTcpNoDelay + ",recSize = " + recSize
                    + ",sendSize = " + sendSize + ",lingerSize = " + lingerSize + ",timeOut = " + timeOut);*/
            mChannel.configureBlocking(false);
            mChannel.connect(new InetSocketAddress(dstIp, dstPort));
            Log.v(TAG,"SocketChannel打开成功");
        } catch (IOException e) {
            Log.i(TAG,"SocketChannel打开失败,开始重新连接");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            connect();
            return;
        }
        try {
            selectorHelper.reg(mChannel,SelectionKey.OP_CONNECT);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }

    private void startSelector() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int num = 0;
                    try {
                        num = selectorHelper.select();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (num > 0) {
                        // 所有事件列表
                        Iterator it = selectorHelper.getSelector().selectedKeys().iterator();
                        // 处理每一个事件
                        while (it.hasNext()) {
                            // 得到关键字
                            SelectionKey selKey = (SelectionKey) it.next();
                            // 删除已经处理的关键字,以防重复出处理
                            it.remove();
                            try {
                                // 处理事件
                                processSelectionKey(selKey);
                            } catch (IOException e) {
                                // Handle error with channel and unregister
                                selKey.cancel();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void processSelectionKey(SelectionKey selKey) throws IOException {
        // Since the ready operations are cumulative,
        // need to check readiness for each operation
        // 调用isConnected()方法会丢CancelledKeyException
        Log.i(TAG,"selKey = " + selKey.interestOps());
        // 确认连接正常
        if (selKey.isValid() && selKey.isConnectable()) {
            // Get channel with connection request
            Log.v(TAG,"OP_CONNECT");
            SocketChannel sChannel = (SocketChannel) selKey.channel();
            // 是否连接完毕
            boolean success = sChannel.finishConnect();
            if (!success) {
                Log.i(TAG,"Socket连接失败");
                // An error occurred; handle it
                // Unregister the channel with this selector
                selKey.cancel();
            } else {
                SocketChannel channel = (SocketChannel)selKey.channel();
                selectorHelper.reg(channel, SelectionKey.OP_READ);
                Log.i(TAG,"Socket连接成功");
            }
        }
        // 读取数据
        if (selKey.isValid() && selKey.isReadable()) {
            // Get channel with bytes to read
            Log.v(TAG,"OP_READ");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();
            Log.v(TAG,"准备读");
            int size = mChannel.read(buffer);
            Log.v(TAG,"读完成");
            if (size <= 0) {
                Log.i(TAG,"socket断开连接,size = " + size);
            } else {
                buffer.flip();
                byte[] bytes = new byte[size];
                buffer.get(bytes);
                String result = new String(bytes);
                Log.v(TAG,"result = " + result);
            }
        }
    }

    private class SelectorHelper {
        private volatile boolean mark = false;
        private final Selector selector;

        SelectorHelper(Selector selector) {
            this.selector = selector;
        }

        Selector getSelector() {
            return selector;
        }

        /**
         * 必须是同步的， 保证多个线程调用reg的时候不会出现问题
         * @param channel 选择的通道
         * @param op Options
         * @return SelectionKey
         * @throws ClosedChannelException
         */
        synchronized SelectionKey reg(SelectableChannel channel, int op)
                throws ClosedChannelException {
            mark = true;
            selector.wakeup();
            SelectionKey register = channel.register(selector, op);
            mark = false;
            return register;
        }

        int select() throws IOException {
            for (;;) {
                // 为了防止一直占用CPU,20ms执行一次
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mark)
                    continue;
                int select = selector.select();
                if (select >= 1)
                    return select;
            }
        }
    }
}

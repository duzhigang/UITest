package com.ggec.uitest.ui.netty.multiclient;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.JdkLoggerFactory;

/**
 * 测试手机APP同时连接多个服务器
 * 一个EventLoopGroup可以管理不止一个TCP连接（EventLoop）,Netty作为客户端实际上是把Netty作为TCP连接池使用.
 * ClientHandler里面的各种回调是处于NioEventGroup开的线程里面
 * */
public class MultiNettyClient {
    private static final String TAG = "MultiNettyClient";
    private EventLoopGroup group;
    private Bootstrap bootstrap;
    private Map<String, Channel> channels = new HashMap<>();

    public void init() {
        // Netty-all-4.1.23之后的版本需要加上这一句，否则会报错：
        // java.lang.NoClassDefFoundError: Failed resolution of: Lorg/apache/logging/log4j/spi/ExtendedLoggerWrapper;
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE);
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        Log.d(TAG,"开始初始化Bootstrap");
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)    //屏蔽Nagle算法试图
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS));//30s未发送数据，回调userEventTriggered
                        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));//编码request
                        ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));//解码response
                        ch.pipeline().addLast(new MultiClientHandler());//客户端处理类
//                        下面这行代码需要在Server端也做同样的粘包处理，否则client可能无法收到Server发送过来的消息
//                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024)); //黏包处理
                    }
                });
    }

/*    // 也可以不用起线程去连接Server，因为只是连接时会需要一点时间，后续通讯Netty会自己起线程管理
    public void connectServer(String host, int port) {
        connect(host, port);
    }*/

    // 每个线程在连接server成功后会自动退出线程
    public void connectServer(final String host, final int port) {
        String name = "netty" + host;
        Thread clientThread = new Thread(name) {
            @Override
            public void run() {
                super.run();
                connect(host, port);
            }
        };
        clientThread.start();

/*        new Thread(new Runnable() {
            @Override
            public void run() {
                connect(host, port);
            }
        }).start();*/
    }

    public void disconnectServer(String host) {
        Channel channel = channels.get(host);
        if (channel != null) {
            channel.close();
            channels.remove(host);
            Log.d(TAG, "disconnectServer()");
        }
    }

    public void exit() {
        // 遍历map中的值
        for (Channel channel : channels.values()) {
            channel.close();
        }
        channels.clear();
        group.shutdownGracefully();
        Log.d(TAG, "exit()");
    }

    public void sendMsg(String host, String msg) {
        final Channel channel = channels.get(host);
        if (channel != null && channel.isActive()) {
            // 获得要发送信息的字节数组
            byte[] content = msg.getBytes();
            ByteBuf buf = Unpooled.copiedBuffer(content);
//            channel.writeAndFlush(msg + System.getProperty("line.separator")).addListener(new ChannelFutureListener() {
            channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        Log.d(TAG,"write msg successful");
                    } else {
                        Log.d(TAG,"write msg error");
                    }
                }
            });
        }
    }

    public Map<String, Channel> getChannels() {
        return channels;
    }

    private void connect(final String host, final int port) {
        ChannelFuture channelFuture = null;
        try {
            // 发起异步连接请求，绑定连接端口和host信息，同步等待链接成功
            Log.d(TAG,"开始连接服务器");
            channelFuture = bootstrap.connect(host, port).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) {
                    if (channelFuture.isSuccess()) {
                        Log.d(TAG,"连接服务器成功");
                        channels.put(host, channelFuture.channel());
                    } else {
                        Log.d(TAG,"连接服务器失败");
                    }
                }
            }).sync();
            long threadId = Thread.currentThread().getId();
            String threadName = Thread.currentThread().getName();
            Log.d(TAG, " connect执行完毕" + ",thread id = " + threadId + ",thread name = " + threadName);
        } catch (Exception e) {
            e.printStackTrace();
            if (null != channelFuture) {
                if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
                    channelFuture.channel().close();
                }
            }
        }
    }
}

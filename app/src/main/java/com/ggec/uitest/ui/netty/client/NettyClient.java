package com.ggec.uitest.ui.netty.client;

import android.os.SystemClock;
import android.util.Log;

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
 * LineBasedFrameDecoder+StringDecoder组合就是按行切换的文本解码器，它被设计用来支持TCP的粘包和拆包。
 * 当然，如果发送的消息不是以换行符结束的，该怎么办呢？或者没有回车换行符，靠消息头中的长度字段来分包怎么办？是不是需要自己写半包解码器？
 * 答案是否定的，Netty提供了多种支持TCP粘包/拆包的解码器用来满足用户的不同诉求。
 * */
public class NettyClient {
    private static final String TAG = "NettyClient";

    private EventLoopGroup group;
    private NettyClientListener nettyClientListener;
    private Channel channel;
    private boolean isConnect = false;  // 是否已经连接
    private int reconnectNum = 5;    // 重连次数
    private boolean isNeedReconnect = true; // 是否需要重连
    private boolean isConnecting = false;   // 是否正在连接
    private long reconnectIntervalTime = 5000;  // 重连间隔
    private static final Integer CONNECT_TIMEOUT_MILLIS = 5000; // 连接超时时间
    public String host;
    public int tcp_port;

    public NettyClient(String host, int tcp_port) {
        this.host = host;
        this.tcp_port = tcp_port;
        // Netty-all-4.1.23之后的版本需要加上这一句，否则会报错：
        // java.lang.NoClassDefFoundError: Failed resolution of: Lorg/apache/logging/log4j/spi/ExtendedLoggerWrapper;
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE);
    }

    public void connect() {
        if (isConnecting) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isNeedReconnect = true;
                connectServer();
            }
        }).start();
    }


    private void connectServer() {
        synchronized (NettyClient.this) {
            ChannelFuture channelFuture = null;
            if (!isConnect) {
                isConnecting = true;
                group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap().group(group)
//                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .option(ChannelOption.TCP_NODELAY, true)//屏蔽Nagle算法试图
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() { // 5
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast("ping", new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));//5s未发送数据，回调userEventTriggered
                                ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));    //编码request
                                // 下面这行代码需要在Server端也做同样的粘包处理，否则client可能无法收到Server发送过来的消息
//                                ch.pipeline().addLast(new LineBasedFrameDecoder(1024)); //黏包处理
                                ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));    //解码response
                                ch.pipeline().addLast(new NettyClientHandler(nettyClientListener));    //客户端处理类
                            }
                        });

                // 发起异步连接请求，绑定连接端口和host信息，同步等待链接成功
                try {
                    channelFuture = bootstrap.connect(host, tcp_port).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()) {
                                Log.d(TAG,"连接服务器连接成功");
                                isConnect = true;
                                channel = channelFuture.channel();
                            } else {
                                Log.d(TAG,"连接连接服务器失败");
                                isConnect = false;
                            }
                            isConnecting = false;
                        }
                    }).sync();
                    Log.d(TAG, " connect执行完毕");
                    // Wait until the connection is closed.
                    channelFuture.channel().closeFuture().sync();
                    Log.d(TAG, " 断开连接");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isConnect = false;
                    nettyClientListener.onServiceStatusConnectChanged(NettyClientListener.STATUS_CONNECT_CLOSED);
                    if (null != channelFuture) {
                        if (channelFuture.channel() != null && channelFuture.channel().isOpen()) {
                            channelFuture.channel().close();
                        }
                    }
                    // 关闭线程池和释放所有资源
                    group.shutdownGracefully();
                    Log.d(TAG,"优雅的退出");
                    reconnect();
                }
            }
        }
    }


    public void disconnect() {
        Log.d(TAG, "disconnect");
        isNeedReconnect = false;
        group.shutdownGracefully();
    }

    public void reconnect() {
        if (isNeedReconnect && reconnectNum > 0 && !isConnect) {
            reconnectNum--;
            SystemClock.sleep(reconnectIntervalTime);
            if (isNeedReconnect && reconnectNum > 0 && !isConnect) {
                Log.d(TAG, "重新连接");
                connectServer();
            }
        }
    }

    public boolean sendMsgToServer(String data, ChannelFutureListener listener) {
        boolean flag = channel != null && isConnect;
        if (flag) {
//			ByteBuf buf = Unpooled.copiedBuffer(data);
//            ByteBuf byteBuf = Unpooled.copiedBuffer(data + System.getProperty("line.separator"), //2
//                    CharsetUtil.UTF_8);
            channel.writeAndFlush(data + System.getProperty("line.separator")).addListener(listener);
        }
        return flag;
    }

    public boolean sendMsgToServer(byte[] data, ChannelFutureListener listener) {
        boolean flag = channel != null && isConnect;
        if (flag) {
            ByteBuf buf = Unpooled.copiedBuffer(data);
            channel.writeAndFlush(buf).addListener(listener);
        }
        return flag;
    }

    public void setReconnectNum(int reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    public void setReconnectIntervalTime(long reconnectIntervalTime) {
        this.reconnectIntervalTime = reconnectIntervalTime;
    }

    public boolean getConnectStatus() {
        return isConnect;
    }

    public boolean isConnecting() {
        return isConnecting;
    }

    public void setConnectStatus(boolean status) {
        this.isConnect = status;
    }

    public void setNettyClientListener(NettyClientListener nettyClientListener) {
        this.nettyClientListener = nettyClientListener;
    }

}

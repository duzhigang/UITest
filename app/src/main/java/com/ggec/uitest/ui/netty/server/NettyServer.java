package com.ggec.uitest.ui.netty.server;

import android.util.Log;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.JdkLoggerFactory;


public class NettyServer {
    private static final String TAG = "NettyServer";
    private static final int PORT = 1088;
    private Channel channel;

    private static NettyServer instance = null;
    private NettyServerListener listener;
    private boolean connectStatus;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private boolean isServerStart;

    public static NettyServer getInstance() {
        if (instance == null) {
            synchronized (NettyServer.class) {
                if (instance == null) {
                    instance = new NettyServer();
                }
            }
        }
        return instance;
    }

    private NettyServer() {
        // Netty-all-4.1.23之后的版本需要加上这一句，否则会报错：
        // java.lang.NoClassDefFoundError: Failed resolution of: Lorg/apache/logging/log4j/spi/ExtendedLoggerWrapper;
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE);
    }

    public void start() {
        // Netty内部都是通过线程在处理各种数据，EventLoopGroup就是用来管理调度他们的，注册Channel，管理他们的生命周期。
        // NioEventLoopGroup是一个处理I/O操作的多线程事件循环
        // bossGroup作为boss,接收传入连接，bossGroup仅接收客户端连接，不做复杂的逻辑处理，为了尽可能减少资源的占用，取值越小越好
        bossGroup = new NioEventLoopGroup(1);
        // workerGroup作为worker，处理boss接收的连接的流量和将接收的连接注册进入这个worker
        workerGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap负责建立服务端
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    // 指定使用NioServerSocketChannel产生一个Channel用来接收连接
                    .channel(NioServerSocketChannel.class) // 5
                    .localAddress(new InetSocketAddress(PORT)) // 6
                    // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于
                    // 临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    // ChannelInitializer用于配置一个新的Channel，向你的Channel当中添加ChannelInboundHandler的实现
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 7
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // ChannelPipeline用于存放管理ChannelHandel
                            // ChannelHandler用于处理请求响应的业务逻辑相关代码
                            // 每个Client连接上Server时都会执行一次
                            Log.d(TAG,"initChannel ch:" + ch);
                            ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
//                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                            ch.pipeline().addLast(new NettyServerHandler(listener));
                        }
                    });

            // Bind and start to accept incoming connections,sync()方法会阻塞直到服务绑定
            ChannelFuture f = b.bind().sync(); // 8

            isServerStart = true;
            listener.onStartServer();
            // Wait until the server socket is closed.
            // closeFuture()当Channel关闭时返回一个ChannelFuture,用于链路检测
            f.channel().closeFuture().sync(); // 9
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            isServerStart = false;
            listener.onStopServer();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void disconnect() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        Log.d(TAG,"Server断开所有连接");
    }

    public void setListener(NettyServerListener listener) {
        this.listener = listener;
    }

    public void setConnectStatus(boolean connectStatus) {
        this.connectStatus = connectStatus;
    }

    public boolean getConnectStatus() {
        return connectStatus;
    }

    public boolean isServerStart(){
        return isServerStart;
    }


    public boolean sendMsgToServer(String data, ChannelFutureListener listener) {
        boolean flag = channel != null && connectStatus && channel.isActive();
        if (flag) {
//			ByteBuf buf = Unpooled.copiedBuffer(data);
//            ByteBuf byteBuf = Unpooled.copiedBuffer(data + System.getProperty("line.separator"), //2
//                    CharsetUtil.UTF_8);
            channel.writeAndFlush(data + System.getProperty("line.separator")).addListener(listener);
        }
        return flag;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}

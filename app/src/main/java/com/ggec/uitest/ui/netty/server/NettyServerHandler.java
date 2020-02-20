package com.ggec.uitest.ui.netty.server;

import android.util.Log;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

// 在多个ChannelPipeline中共享同一个ChannelHandler，对应的ChannelHandler必须要使用@Sharable注解标注；否则,试图将它添加到多个ChannelPipeline时将会触发异常。
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    private static final String TAG = "NettyServerHandler";

    private NettyServerListener mListener;

    public NettyServerHandler(NettyServerListener listener) {
        this.mListener = listener;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"channelReadComplete client: " + host);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"exceptionCaught client: " + host);
        cause.printStackTrace();                //5
        ctx.close();                            //6
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"channelRead0 client: " + host + ",msg: " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"channelActive client: " + host);
        mListener.onChannel(ctx.channel());
        NettyServer.getInstance().setConnectStatus(true);
        mListener.onServiceStatusConnectChanged(NettyServerListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"channelInactive client: " + host);
        NettyServer.getInstance().setConnectStatus(false);
        mListener.onServiceStatusConnectChanged(NettyServerListener.STATUS_CONNECT_CLOSED);
    }
}

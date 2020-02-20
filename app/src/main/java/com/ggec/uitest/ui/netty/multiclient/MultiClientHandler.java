package com.ggec.uitest.ui.netty.multiclient;

import android.util.Log;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MultiClientHandler extends SimpleChannelInboundHandler<String> {
    private static final String TAG = "MultiClientHandler";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"channelActive,connecting server: " + host);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"channelInactive,disconnecting server: " + host);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"channelReadComplete " + host);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"userEventTriggered " + host);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.e(TAG, "exceptionCaught " + host);
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Log.d(TAG, "channelRead, msg = " + msg);
    }
}
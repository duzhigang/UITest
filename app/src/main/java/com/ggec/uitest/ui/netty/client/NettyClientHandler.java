package com.ggec.uitest.ui.netty.client;

import android.util.Log;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * readerIdleTime读空闲超时时间设定，如果channelRead()方法超过readerIdleTime时间未被调用则会触发超时事件调用userEventTrigger()方法；
 * writerIdleTime写空闲超时时间设定，如果write()方法超过writerIdleTime时间未被调用则会触发超时事件调用userEventTrigger()方法；
 * allIdleTime所有类型的空闲超时时间设定，包括读空闲和写空闲；
 *
 * 总结
 * (1).IdleStateHandler心跳检测主要是通过向线程任务队列中添加定时任务，判断channelRead()方法或write()方法是否调用空闲超时，如果超时则触发超时事件执行自定义userEventTrigger()方法；
 *
 * (2).Netty通过IdleStateHandler实现最常见的心跳机制不是一种双向心跳的PING-PONG模式，而是客户端发送心跳数据包，服务端接收心跳但不回复，因为如果服务端同时有上千个连接，
 *     心跳的回复需要消耗大量网络资源；如果服务端一段时间内内有收到客户端的心跳数据包则认为客户端已经下线，将通道关闭避免资源的浪费；在这种心跳模式下服务端可以感知客户端的存活情况，
 *     无论是宕机的正常下线还是网络问题的非正常下线，服务端都能感知到，而客户端不能感知到服务端的非正常下线.
 *
 * (3).要想实现客户端感知服务端的存活情况，需要进行双向的心跳；Netty中的channelInactive()方法是通过Socket连接关闭时挥手数据包触发的，因此可以通过channelInactive()方法感知正常的下线情况，但是因为网络异常等非正常下线则无法感知.
 *     手机主动关闭socket连接或者主动断网、Server主动断开连接，则会出发channelInactive方法
 *
 * (4).SimpleChannelInboundHandler是有泛型参数的，可以指定一个具体的类型参数，通过decoder配合使用，非常方便；好处是可以处理不同的类型对象，搭配channelRead0()回调完成后Netty会帮我们完成释放。
 *     ChannelInboundHandlerAdapter则是直接操作byte数组的；好处是更自由，在异步的场景下耿适合，搭配channelRead()使用，但是在channelRead()中要使用ReferenceCountUtil.release()来丢弃收到的信息。
 *     如果说channelRead都是同步操作的话，SimpleChannelInboundHandler是不错的选择；如果操作是异步的话，那他的逻辑就有点麻烦了，例如你把数据交给另外的线程处理了，还没处理就会释放了.
 *
 * (5).Netty handler的exceptionCaught 只会catch inbound handler的exception, outbound exceptions 需要在writeAndFlush方法里加上listener来监听消息是否发送成功，
 *     最好在每个outbound handler的处理类里加上try catch，只是处理由于程序异常导致的发包失败，由于网络原因没有发送成功，
 *     最终会被nettychannel异常检测机制检测到，反馈到channel inactivate事件上，这样能够处理大部分case，但是如果需要保证消息的最终送达，不允许丢失，则需要业务层自己保证.
 * */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    private static final String TAG = "NettyClientHandler";
    private NettyClientListener listener;

    public NettyClientHandler(NettyClientListener listener) {
        this.listener = listener;
    }


    // 如果5s内write()方法未被调用则触发一次userEventTrigger()方法，实现客户端每四秒向服务端发送一次消息；
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String host = ipSocket.getHostString();
        Log.d(TAG,"userEventTriggered " + host);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            //检测是否这段时间没有和服务器联系
            if (event.state() == IdleState.WRITER_IDLE) {
                ctx.channel().writeAndFlush("Heartbeat"+ System.getProperty("line.separator"));
            }
        }
    }

    /**
     * 连接成功
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Log.d(TAG, "channelActive");
        listener.onServiceStatusConnectChanged(NettyClientListener.STATUS_CONNECT_SUCCESS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.d(TAG, "channelInactive");
    }

    /**
     * 客户端收到消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String byteBuf) throws Exception {
        Log.d(TAG, "channelRead0");
        listener.onMessageResponse(byteBuf);
        String response = "client response";
        ByteBuf buf = ctx.alloc().buffer(4*response.length());
        buf.writeBytes(response.getBytes());
        ctx.writeAndFlush(buf);
        Log.i(TAG,"发送response消息");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        Log.e(TAG, "exceptionCaught");
        listener.onServiceStatusConnectChanged(NettyClientListener.STATUS_CONNECT_ERROR);
        cause.printStackTrace();
        ctx.close();
    }
}

package ykf.inbound;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import ykf.filter.ProxyBizFilter;
import ykf.homework03.filter.HttpRequestFilter;
import ykf.homework03.httpclient4.HttpOutboundHandler;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private final String proxyServer;
    private HttpOutboundHandler handler;
    HttpRequestFilter filter;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        handler = new HttpOutboundHandler(this.proxyServer);
        filter = ProxyBizFilter.newInstance();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        System.out.println("==== channelReadComplete(ChannelHandlerContext ctx)");
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("==== channelRead(ChannelHandlerContext ctx, Object msg) ");

        if (false == (msg instanceof FullHttpRequest)) {
            return;
        }
        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        try {
            filter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);

        } catch (Exception e) {
            //e.printStackTrace();
            System.err.println(e.getMessage());
            if (fullRequest != null) {
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);

                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


}
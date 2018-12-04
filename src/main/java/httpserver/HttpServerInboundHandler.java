package httpserver;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import DhApi.*;
import Obj.RealStreamObj;
import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


public class HttpServerInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }
        if (request.getMethod() != GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }
        final String uri = request.getUri().substring(1);
        //System.out.println(uri);

        //这里可以根据具体的url来拆分，获取请求带过来的参数，实现自己具体的业务操作
        sendListing(ctx, uri);

        //sendError(ctx, UNAUTHORIZED);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)  {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }


    private String sendCommand(String text) {
        String jsongStr = null;
        Gson gson = new Gson();
        String responseString = null;

        String [] dataAry = text.split("&|=");
        for (int i=0; i < dataAry.length; i++) {
            System.out.println("正则表达式分割 -- " + i + " " + dataAry[i]);
        }
        if (dataAry.length < 2) {
            return "param error";
        }


        try {
            jsongStr = URLDecoder.decode(dataAry[3], "utf-8");
            //System.out.println(jsongStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if (dataAry[1].equals("RealStream")) {
            RealStreamObj realStreamObj = gson.fromJson(jsongStr, RealStreamObj.class);
            responseString = new RealStream().getRealStream(realStreamObj.getDevice());
        }


        return responseString;
    }

    private void sendListing(ChannelHandlerContext ctx, String text) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        //buf.append("http请求成功！" + text);
        buf.append(sendCommand(text));
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer("Failure: " + status.toString()
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

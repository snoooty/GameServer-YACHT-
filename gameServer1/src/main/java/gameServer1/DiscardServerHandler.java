package gameServer1;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter{

	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
		
//		ByteBuf in = (ByteBuf) msg;
//		try {
//			while(in.isReadable()) {
//				System.out.print((char) in.readByte());
//                System.out.flush();
//			}
//		}finally {
//			ReferenceCountUtil.release(msg);
//		}
		
		String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset()); // (3)
        StringBuilder builder = new StringBuilder();
        builder.append("수신한 문자열 [");
        builder.append(readMessage);  // (4)
        builder.append("]");
        System.out.println(builder.toString());
		
		// 응답으로 돌려주기
        ByteBuf msgBuffer = Unpooled.buffer();
        msgBuffer.writeBytes("Server Response => received data : ".getBytes());

        ctx.write(msgBuffer);
		ctx.write(msg);
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
		cause.printStackTrace();
		ctx.close();
	}
}
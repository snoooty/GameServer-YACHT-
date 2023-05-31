package gameServer1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.*;

public class DiscardServer {
	
	private int port;
	
	public DiscardServer(int port) {
		this.port = port;
	}
	
	public void run() throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception{
							ChannelPipeline p = ch.pipeline();
							p.addLast(new DiscardServerHandler());
						}
					});
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
			
		}finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		int port = 8080;
		if(args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		
		new DiscardServer(port).run();

	}

}

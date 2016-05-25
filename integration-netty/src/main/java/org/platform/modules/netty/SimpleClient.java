package org.platform.modules.netty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class SimpleClient {
	
	public void run() {
		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new StringEncoder(), new StringDecoder(),
						new SimpleClientHandler());
			}
		});
		ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 8000));
		future.getChannel().getCloseFuture().awaitUninterruptibly();
		bootstrap.releaseExternalResources();
	}

	public static void main(String[] args) {
		SimpleClient client = new SimpleClient();
		client.run();
	}
}

class SimpleClientHandler extends SimpleChannelHandler {
	
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		System.err.println("已经与Server建立连接，请输入要发送的信息：");
		e.getChannel().write(br.readLine());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.messageReceived(ctx, e);
		if (e.getMessage() instanceof String) {
			String message = (String) e.getMessage();
			System.out.println(message);
			e.getChannel().write(br.readLine());
		}
	}
	
	
}
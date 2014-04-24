package org.platform.utils.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class NIOServer {
	
	private Logger logger = Logger.getLogger(getClass());
	
	private Selector selector = null;
	
	private ServerSocketChannel serverSocketChannel = null;
	
	public NIOServer(String host, int port) {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(null == host ? 
					new InetSocketAddress(port) : new InetSocketAddress(host ,port));
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("server " + host + " running in port " + port);
	}
	
	public void start() {
		while(true) {
			try {
				selector.select(); //当注册的事件到达时方法返回,否则该方法会一直阻塞
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();  
	            while (iterator.hasNext()) {  
	                SelectionKey selectionKey = (SelectionKey) iterator.next();  
	                // 删除已选的key,以防重复处理  
	                iterator.remove();  
					if (selectionKey.isAcceptable()) {
						accept(selectionKey);
					} else if (selectionKey.isReadable()) { 
						read(selectionKey);
					} else if (selectionKey.isWritable()) {
						write(selectionKey);
					}
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		if (null != serverSocketChannel) {
			try {
				serverSocketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void accept(SelectionKey selectionKey) throws IOException {
		ServerSocketChannel serverSocketChannel = 
				(ServerSocketChannel) selectionKey.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		NIOUtils.sendData(socketChannel, "server accept client request");
		socketChannel.register(selector, SelectionKey.OP_READ);
	}
	
	private void read(SelectionKey selectionKey) throws IOException {
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		String remoteHost = ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostName();
		String rData = NIOUtils.receiveData(socketChannel);
		String sData = "[" + remoteHost + "]: " + rData;
		logger.debug("client: " + sData);
		NIOUtils.sendData(socketChannel, sData);
		selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	private void write(SelectionKey selectionKey) throws IOException {
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
		String command = bufferReader.readLine();
		if ("exit".equals(command)) {
			stop();
		} else {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			NIOUtils.sendData(socketChannel, command);
		}
		selectionKey.interestOps(SelectionKey.OP_READ);
	}
	
	public static void main(String[] args) {
		NIOServer server = new NIOServer("127.0.0.1", 1000);
		server.start();
	}
}

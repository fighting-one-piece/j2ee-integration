package org.platform.utils.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

public class NIOClient {
	
	private Logger logger = Logger.getLogger(getClass());

	private Selector selector = null;

	public NIOClient(String host, int port) {
		try {
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(host, port));
			selector = Selector.open();
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("client start connect " + host + " : " + port);
	}

	public void start() {
		try {
			while (true) {
				selector.select();
				// 获得selector中选中的项的迭代器
				Iterator<SelectionKey> iterator = selector.selectedKeys()
						.iterator();
				while (iterator.hasNext()) {
					SelectionKey selectionKey = (SelectionKey) iterator.next();
					// 删除已选的key,以防重复处理
					iterator.remove();
					if (selectionKey.isConnectable()) {
						connect(selectionKey);
					} else if (selectionKey.isReadable()) {
						read(selectionKey);
					} else if (selectionKey.isWritable()) {
						write(selectionKey);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void connect(SelectionKey selectionKey) throws IOException {
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		//如果正在连接，则完成连接
		if (socketChannel.isConnectionPending()) {
			socketChannel.finishConnect();
		}
		socketChannel.configureBlocking(false);
		
		NIOUtils.sendData(socketChannel, "client request server");
		socketChannel.register(selector, SelectionKey.OP_READ);
	}
	
	private void read(SelectionKey selectionKey) throws IOException {
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		logger.debug(NIOUtils.receiveData(socketChannel));
		selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	private void write(SelectionKey selectionKey) throws IOException {
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
		NIOUtils.sendData(socketChannel, bufferReader.readLine());
		selectionKey.interestOps(SelectionKey.OP_READ);
	}
	
	public static void main(String[] args) {
		NIOClient client = new NIOClient("127.0.0.1", 1000);
		client.start();
	}
}

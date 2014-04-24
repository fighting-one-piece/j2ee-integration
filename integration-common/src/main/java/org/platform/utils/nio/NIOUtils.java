package org.platform.utils.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class NIOUtils {
	
	private static Charset charset = Charset.forName("UTF-8");
	private static CharsetDecoder decoder = charset.newDecoder();
	private static CharsetEncoder encoder = charset.newEncoder();
	
	public static void sendData(SocketChannel socketChannel, String data) throws IOException {
//		ByteBuffer buffer = ByteBuffer.allocate(4096);
//		buffer.put(encoder.encode(CharBuffer.wrap(data.toCharArray())));
//		buffer.flip();
		socketChannel.write(encoder.encode(CharBuffer.wrap(data.toCharArray())));
	}
	
	public static String receiveData(SocketChannel socketChannel) throws IOException {
		StringBuilder sb = new StringBuilder();
		ByteBuffer buffer = ByteBuffer.allocate(4096);
		int index = socketChannel.read(buffer);
		while (index > 0) {
			buffer.flip(); 
			while(buffer.hasRemaining()){
				sb.append(decoder.decode(buffer).toString());
			}
		    buffer.clear();
		    index = socketChannel.read(buffer);
		}
		return sb.toString();
	}
	
}

package org.platform.utils.bigdata.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class SimpleServer {
	
	public static void main(String[] args) throws Exception {
		TServerTransport serverTransport = new TServerSocket(0);
		serverTransport.accept();
//		TServer server= new TSimpleServer(processor, serverTransport, 
//				new TFramedTransport.Factory(), new TCompactProtocol.Factory());
		TServer server = new TSimpleServer(null); 
		server.serve();
	}
}

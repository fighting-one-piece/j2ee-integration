package org.platform.utils.bigdata.thrift;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class Client {

	public static void main(String[] args) throws Exception {
		TTransport transport = new TFramedTransport(new TSocket("host", 10000));
		TProtocol protocol = new TCompactProtocol(transport);
		protocol.getScheme();
		transport.open();
		//SimpleService.Client client = new SimpleService().Client(protocol);
	}
}

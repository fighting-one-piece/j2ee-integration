package org.platform.utils.bigdata.avro;

import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.Responder;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.specific.SpecificResponder;

public class SimpleServer {

	public static void main(String[] args) throws Exception {
		Responder responder = new SpecificResponder(SimpleService.class, new SimpleServiceImpl());
		Server server = new HttpServer(responder, 0);
		server.join();
	}
}

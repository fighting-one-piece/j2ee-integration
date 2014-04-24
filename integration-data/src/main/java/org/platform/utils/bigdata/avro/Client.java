package org.platform.utils.bigdata.avro;

import java.net.URL;

import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.specific.SpecificRequestor;


public class Client {

	public static void main(String[] args) throws Exception {
		URL url = new URL("http", "host", "file");
		Transceiver transceiver = new HttpTransceiver(url);
		SimpleService service = (SimpleService) SpecificRequestor.getClient(
				SimpleService.class, transceiver);
		service.getClass();
	}
}

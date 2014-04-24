package org.platform.utils.file;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PomContentHandler extends DefaultHandler {
	
	private StringBuilder sb = null;
	
	private String file = null;
	
	public PomContentHandler(String file) {
		this.file = file;
	}

	@Override
	public void characters(char[] chars, int start, int length) throws SAXException {
		if (null != sb) {
			String value = new String(chars, start, length);
			if ("4.2.3".equals(value) || "commons-httpclient".equals(value)) {
				System.out.println("value : " + value + " , file: " + file);
			}
			//sb.append(chars, start, length);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		//System.out.println("*******解析文档结束*******");
	}

	@Override
	public void endElement(String namespaceURI, String localName, String fullName)
			throws SAXException {
		if ("artifactId".equals(fullName) || "version".equals(fullName)) {
			//System.out.println(sb.toString());
			//System.out.println("元素: "+"["+fullName+"]"+" 解析结束!");  
			sb = null;
		}
		
	}

	@Override
	public void startDocument() throws SAXException {
		//System.out.println("*******开始解析文档*******");
	}

	@Override
	public void startElement(String arg0, String localName, String fullName,
			Attributes arg3) throws SAXException {
		if ("artifactId".equals(fullName) || "version".equals(fullName)) {
			//System.out.println("\n 元素: " + "["+fullName+"]" +" 开始解析!");
			sb = new StringBuilder();
		}
	}

}

package org.platform.utils.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.sax.SAXSource;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/** JAXB工具类*/
public class JAXBUtils {

	private static Logger logger = Logger.getLogger(JAXBUtils.class);

	private static Set<Class<?>> classes = null;

	private static JAXBUtils jaxbUtils = null;

	private Marshaller marshaller = null;

	private Unmarshaller unmarshaller = null;

	static {
		classes = new HashSet<Class<?>>();
	}

	private void initContext() {
		try {
			JAXBContext context = JAXBContext.newInstance(classes.toArray(new Class<?>[0]));
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");//编码格式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);//是否格式化生成的xml串
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);//是否省略xml头信息（<?xml version="1.0" encoding="gb2312" standalone="yes"?>）
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			logger.debug(e.getMessage(), e);
		}
	}

	private JAXBUtils() {
		initContext();
	}

	public static synchronized JAXBUtils getInstance() {
		if (null == jaxbUtils) {
			jaxbUtils = new JAXBUtils();
		}
		return jaxbUtils;
	}

	/**
	  *
	  *<p>包名类名：platform.utils.jaxb.JAXBUtils</p>
	  *<p>方法名：read</p>
	  *<p>描述：读取文件对象</p>
	  *<p>参数：@param file 文件
	  *<p>参数：@return 对象</p>
	  *<p>返回类型：Object</p>
	  *<p>创建时间：2013-4-29 上午10:14:15</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public Object read(File file) {
		try {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = parserFactory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			InputSource inSrc = new InputSource(new FileInputStream(file));
			EntityResolver entityReolver = new EntityResolver() {
				public InputSource resolveEntity(String publicId,
						String systemId) {
					return new InputSource(new StringReader(""));
				}
			};
			xmlReader.setEntityResolver(entityReolver);
			SAXSource saxSource = new SAXSource(xmlReader, inSrc);
			return unmarshaller.unmarshal(saxSource);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return null;
	}

	/**
	 *
	  *<p>包名类名：platform.utils.jaxb.JAXBUtils</p>
	  *<p>方法名：write</p>
	  *<p>描述：对象写入文件</p>
	  *<p>参数：@param object 对象
	  *<p>参数：@param file 文件</p>
	  *<p>返回类型：void</p>
	  *<p>创建时间：2013-4-29 上午10:14:49</p>
	  *<p>作者: wulin </p>
	 *
	 */
	public synchronized void write(Object object, File file) {
		try {
			marshaller.marshal(object, file);
		} catch (JAXBException e) {
			logger.warn(e.getMessage(), e);
		}
	}

	public Object readObject(Class<?> clazz, File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = parserFactory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			InputSource inSrc = new InputSource(new FileInputStream(file));
			EntityResolver entityReolver = new EntityResolver() {
				public InputSource resolveEntity(String publicId,
						String systemId) {
					return new InputSource(new StringReader(""));
				}
			};
			xmlReader.setEntityResolver(entityReolver);
			SAXSource saxSource = new SAXSource(xmlReader, inSrc);
			return unmarshaller.unmarshal(saxSource);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return null;
	}

	public synchronized void writeObject(Object object, File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
			marshaller.marshal(object, System.out);
			marshaller.marshal(object, file);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
	}

	public String convertObject2Xml(Object object) {
		StringWriter stringWriter = new StringWriter();
		try {
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
			JAXBContext context = JAXBContext.newInstance(object.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
			marshaller.marshal(object, writer);
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
		}
		return stringWriter.getBuffer().toString();
	}
}

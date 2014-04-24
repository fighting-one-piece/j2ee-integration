package org.platform.utils.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class PomFileVisitor extends SimpleFileVisitor<Path> {

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		if (file.toString().endsWith(".pom")) {
			//System.out.println("parse file: " + file.toString());
			try {
		        XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser") ;
		        ContentHandler contentHandler = new PomContentHandler(file.toString());
		        reader.setContentHandler(contentHandler);
		        reader.parse(new InputSource(file.toString()));
		    } catch ( SAXException e ) {
		        System.out.println("解析文档时错: " + e.getMessage());
		    }
		}
		return super.visitFile(file, attrs);
	}
}

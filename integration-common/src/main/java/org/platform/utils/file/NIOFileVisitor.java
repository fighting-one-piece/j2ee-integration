package org.platform.utils.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.log4j.Logger;

public class NIOFileVisitor extends SimpleFileVisitor<Path> {
	
	private Logger logger = Logger.getLogger(NIOFileVisitor.class);

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		System.out.println("preVisitDirectory: " + dir);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		logger.debug("visitFile: " + file);
		System.out.println("visitFile: " + file);
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		return FileVisitResult.TERMINATE;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		System.out.println("postVisitDirectory: " + dir);
		return FileVisitResult.CONTINUE;
	}
	
}

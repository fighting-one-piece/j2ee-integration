package org.platform.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.EnumSet;
import java.util.List;

import org.junit.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.platform.utils.file.NIOFileVisitor;

public class NIOFileTest {

	@Test
	public void testWalkFileTree() {
		try {
			Files.walkFileTree(Paths.get("D:\\develop\\data", "hadoop"), EnumSet.noneOf(FileVisitOption.class), 2,new NIOFileVisitor());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindPom() {
		try {
			Files.walkFileTree(Paths.get("D:\\develop\\java\\maven\\repository"), new PomFileVisitor());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testReadPom() {
		String path = "D:\\ant-1.8.4.pom";
		try {
	        // 初始化reader
	        XMLReader reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser") ;
	        // 创建ContentHandler的实例
	        ContentHandler contentHandler = new PomContentHandler(path);
	        // 在reader中注册实例化的ContentHandler
	        reader.setContentHandler( contentHandler );
	        // 开始解析文档
	        reader.parse(new InputSource(path));
	    } catch ( IOException e ) {
	        System.out.println("读入文档时错: " + e.getMessage());
	    } catch ( SAXException e ) {
	        System.out.println("解析文档时错: " + e.getMessage());
	    }
	}
	
	@Test
	public void testFileWatchService() {
		//获取当前文件系统的WatchService监控对象
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Kind<?>[] events = new Kind[]{StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.OVERFLOW};
			Paths.get("D:\\develop\\data", "hadoop").register(watchService, events);
			while(true){   
		          WatchKey key = watchService.take();  //获取下一个文件变化事件 
		          for(WatchEvent<?> event : key.pollEvents()){  
		              System.out.println(event.context()+"文件发生了"+event.kind()+"事件"+"此事件发生的次数: "+event.count());  
		          }  
		          //重设WatchKey  
		          boolean valid=key.reset();  
		          //监听失败，退出监听  
		          if(!valid){  
		              break;  
		          }  
		     }  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {
		try {
			FileOutputStream f = new FileOutputStream(new File("D:\\develop\\data\\mylock.lock"));
	        FileChannel channel=f.getChannel();  
	        //非阻塞加锁  
	         FileLock lock=channel.tryLock();  
	        //阻塞加锁  
	        // FileLock lock=channel.lock();  
	        if(lock==null){  
	            System.out.println("改程序已经被占用.....");  
	            System.out.println("阻塞中.....");  
	              
	        }else{  
	            System.out.println("开始访问.......");  
	            Thread.sleep(5000);//5秒后进行访问  
	            if(lock != null){  
	                List<String> s=Files.readAllLines(Paths.get("D:\\develop\\data\\my.txt"), StandardCharsets.UTF_8);  
	                //读取文件，打印内容  
	                for(String ss : s){  
	                    System.out.println(ss);  
	                }  
	                lock.release();//释放锁  
	                lock.close();//关闭资源  
	                f.close();//关闭流资源  
	                Files.delete(Paths.get("D:\\develop\\data\\mylock.lock"));  
	                System.out.println("访问完毕删除锁文件");  
	            }  
	        }  
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
}

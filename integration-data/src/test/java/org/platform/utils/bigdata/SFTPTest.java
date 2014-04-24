package org.platform.utils.bigdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.apache.hadoop.io.IOUtils;
import org.junit.Test;
import org.platform.utils.common.sftp.SFTPUtils;

import com.jcraft.jsch.ChannelSftp;

public class SFTPTest {
	
	public void Connect() {
		try {
			ChannelSftp chSftp = SFTPUtils.obtainChannel();
			String src = "/home/hadoop/temp/word.txt"; 
			Random random = new Random();
	        String dst = "D:/test" + random.nextInt(100) + ".txt"; 
			InputStream in = chSftp.get(src);
			OutputStream out = new FileOutputStream(new File(dst));
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = in.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
			SFTPUtils.closeChannel(chSftp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testMultiConnect() {
		for (int i = 0; i < 2; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " start");
					Connect();
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " end");
				}
			}).start();
		}
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}
}

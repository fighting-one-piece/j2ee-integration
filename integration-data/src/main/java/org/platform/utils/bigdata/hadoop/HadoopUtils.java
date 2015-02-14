package org.platform.utils.bigdata.hadoop;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.fs.FileSystem;
import org.platform.utils.bigdata.AbstrUtils;

public class HadoopUtils extends AbstrUtils {
	
	public static final String HDFS_DATA_WAREHOUSE = "hdfs://centos.master:9000/home/hadoop/filesystem/data";

	public static final String HDFS_USER_WAREHOUSE = "hdfs://centos.master:9000/user/Administrator";
	
	public static FileSystem getFileSystem() {
		FileSystem fileSystem = null;
		try {
			fileSystem = FileSystem.get(URI.create(HDFS_DATA_WAREHOUSE), configuration);
		} catch (IOException e) {
			LOG.info(e.getMessage(), e);
		}
		return fileSystem;
	}
	
	public static FileSystem getFileSystem(String hdfsPath) {
		FileSystem fileSystem = null;
		try {
			fileSystem = FileSystem.get(URI.create(hdfsPath), configuration);
		} catch (IOException e) {
			LOG.info(e.getMessage(), e);
		}
		return fileSystem;
	}

}

package org.platform.utils.bigdata;

import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.platform.utils.bigdata.hadoop.HadoopUtils;
import org.platform.utils.common.resource.ResourceManager;

public class HadoopUtilsTest {

	@Test
	public void testFSMkdirs() throws Exception {
		HadoopUtils.getFileSystem().mkdirs(new Path(HadoopUtils.HDFS_DATA_WAREHOUSE + "/first1"));
	}
	
	@Test
	public void testFSDelete() throws Exception {
		HadoopUtils.getFileSystem().delete(new Path("/first"), true);
	}
	
	@Test
	public void testFSCopy() throws Exception {
		String parent = HadoopUtils.HDFS_DATA_WAREHOUSE + "/movie";
		HadoopUtils.getFileSystem().mkdirs(new Path(parent));
		Path src = new Path(ResourceManager.getAbsolutePath("mahout/movies.dat"));
		HadoopUtils.getFileSystem().copyFromLocalFile(src, new Path(parent + "/movies.dat"));
		src = new Path(ResourceManager.getAbsolutePath("mahout/users.dat"));
		HadoopUtils.getFileSystem().copyFromLocalFile(src, new Path(parent + "/users.dat"));
		src = new Path(ResourceManager.getAbsolutePath("mahout/ratings.dat"));
		HadoopUtils.getFileSystem().copyFromLocalFile(src, new Path(parent + "/ratings.dat"));
		src = new Path(ResourceManager.getAbsolutePath("mahout/tags.dat"));
		HadoopUtils.getFileSystem().copyFromLocalFile(src, new Path(parent + "/tags.dat"));
	}
	
}

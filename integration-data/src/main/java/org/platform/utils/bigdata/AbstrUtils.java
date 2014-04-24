package org.platform.utils.bigdata;

import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Logger;

public abstract class AbstrUtils {
	
	protected static Logger logger = Logger.getLogger(AbstrUtils.class);
	
	protected static Configuration configuration = null;
	
	/** 初始化配置 **/
	static {
		System.setProperty("hadoop.home.dir", "D:/develop/data/hadoop/hadoop-2.2.0");
		System.setProperty("HADOOP_MAPRED_HOME", "D:/develop/data/hadoop/hadoop-2.2.0");
		System.setProperty("SQOOP_CONF_DIR", "D:/develop/data/sqoop/sqoop-1.4.4-hadoop-2.0.4");
		configuration = new Configuration();
		/** 与hbase/conf/hbase-site.xml中hbase.master配置的值相同 */
		configuration.set("hbase.master", "192.168.10.10:60000");
		/** 与hbase/conf/hbase-site.xml中hbase.zookeeper.quorum配置的值相同 */
		configuration.set("hbase.zookeeper.quorum", "192.168.10.10");
		/** 与hbase/conf/hbase-site.xml中hbase.zookeeper.property.clientPort配置的值相同 */
		configuration.set("hbase.zookeeper.property.clientPort", "2181");
		//configuration = HBaseConfiguration.create(configuration);
	}

}

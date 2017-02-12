package org.platform.utils.datasource;

public class DynamicDataSourceHolder {

	/** 动态数据源存储 */
    private static final ThreadLocal<DynamicDataSourceConstants> DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL = new ThreadLocal<>();

    private DynamicDataSourceHolder() {
        //
    }

    /**
     * 存放数据源
     * @param dataSource 数据源
     */
    public static void putDataSource(DynamicDataSourceConstants dataSource) {
        DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL.set(dataSource);
    }

    /**
     * 获取数据源
     * @return the data source
     */
    public static DynamicDataSourceConstants getDataSource() {
        return DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL.get();
    }

    /**
     * 清除数据源
     */
    public static void clearDataSource() {
        DYNAMIC_DATA_SOURCE_GLOBAL_THREAD_LOCAL.remove();
    }
	
}

package org.platform.utils.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/** 动态数据源实现读写分离 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /** 写数据源 */
    private Object writeDataSource;
    
    /** 读数据源 */
    private Object readDataSource;

    @Override
    public void afterPropertiesSet() {
        if (this.writeDataSource == null) {
            throw new IllegalArgumentException("Property 'writeDataSource' is required");
        }
        setDefaultTargetDataSource(writeDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DynamicDataSourceConstants.WRITE.name(), writeDataSource);
        if (readDataSource != null) {
            targetDataSources.put(DynamicDataSourceConstants.READ.name(), readDataSource);
        }
        setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DynamicDataSourceConstants dynamicDataSourceGlobal = DynamicDataSourceHolder.getDataSource();
        if (dynamicDataSourceGlobal == null
            || dynamicDataSourceGlobal == DynamicDataSourceConstants.WRITE) {
            return DynamicDataSourceConstants.WRITE.name();
        }
        return DynamicDataSourceConstants.READ.name();
    }

    public void setWriteDataSource(Object writeDataSource) {
        this.writeDataSource = writeDataSource;
    }

    public Object getWriteDataSource() {
        return writeDataSource;
    }

    public Object getReadDataSource() {
        return readDataSource;
    }

    public void setReadDataSource(Object readDataSource) {
        this.readDataSource = readDataSource;
    }

}

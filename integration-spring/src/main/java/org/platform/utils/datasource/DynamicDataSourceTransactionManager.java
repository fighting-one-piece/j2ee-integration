package org.platform.utils.datasource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

/** 动态数据源事务管理器 */
@SuppressWarnings("serial")
public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

    public DynamicDataSourceTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        //设置数据源
        boolean readOnly = definition.isReadOnly();
        //只读事务到读库，读写事务到写库
        if (readOnly) {
            DynamicDataSourceHolder.putDataSource(DynamicDataSourceConstants.READ);
        } else {
            DynamicDataSourceHolder.putDataSource(DynamicDataSourceConstants.WRITE);
        }
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        //清理本地线程的数据源
        DynamicDataSourceHolder.clearDataSource();
    }

}

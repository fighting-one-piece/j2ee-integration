package org.platform.utils.pagination;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;

import org.platform.entity.QueryCondition;
import org.platform.utils.reflect.ReflectUtils;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {

	private final static Log logger = LogFactory.getLog(PaginationInterceptor.class);

	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		//Mybatis在进行Sql语句处理的时候都是建立的RoutingStatementHandler，而在RoutingStatementHandler里面拥有一个StatementHandler类型的delegate属性
		//RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，即SimpleStatementHandler、PreparedStatementHandler或CallableStatementHandler
		//在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。
	    //我们在PageInterceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的prepare方法，又因为Mybatis只有在建立RoutingStatementHandler的时候
	    //是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
        StatementHandler delegate = (StatementHandler) ReflectUtils.obtainValueByFieldName(handler, "delegate");
	    BoundSql boundSql = delegate.getBoundSql();
	    //获取当前要执行的Sql语句，也就是我们直接在Mapper映射语句中写的Sql语句
        String sql = boundSql.getSql();
	    if(sql.toUpperCase().indexOf("INSERT") != -1 ||
	    		sql.toUpperCase().indexOf("UPDATE") != -1 ||
	    				sql.toUpperCase().indexOf("DELETE") != -1) {
	    	return invocation.proceed();
	    }
	    Object parameterObject = boundSql.getParameterObject();
	    logger.debug("parameterObject: " + parameterObject);
	    if (parameterObject instanceof Map) {
	    	Map<String, Object> condition = (Map<String, Object>) parameterObject;
	    	MappedStatement mappedStatement = (MappedStatement)ReflectUtils.obtainValueByFieldName(delegate, "mappedStatement");
            //拦截到的prepare方法参数是一个Connection对象
            Connection connection = (Connection) invocation.getArgs()[0];
            setTotalRowNum(condition, mappedStatement, connection);
	    	boolean isPagination = (Boolean) condition.get(QueryCondition.IS_PAGINATION);
	    	if (!isPagination) {
	    		return invocation.proceed();
	    	}
	    	int offset = (Integer) condition.get(QueryCondition.OFFSET);
	    	int limit = (Integer) condition.get(QueryCondition.LIMIT);
	    	logger.debug("offset：　" + offset + "limit: " + limit);
            //获取MyBatis配置信息
            Configuration configuration = (Configuration) ReflectUtils.obtainValueByFieldName(delegate, "configuration");
            String dialectValue = configuration.getVariables().getProperty("dialect");
            if (null == dialectValue) {
            	throw new RuntimeException("the value of the dialect is not find");
            }
            Dialect.Type dialectType = Dialect.Type.valueOf(dialectValue.toUpperCase());;
    		logger.debug("dialectType: " + dialectType);
    		Dialect dialect = null;
    		switch (dialectType) {
    			case MYSQL:
    				dialect = new MySQL5Dialect();
    				break;
    			case MSSQL:
    				dialect = new MySQL5Dialect();
    				break;
    			case ORACLE:
    				dialect = new OracleDialect();
    				break;
    			default:
    				dialect = new MySQL5Dialect();
    		}
            String pageSql = dialect.obtainPageSql(sql, offset, limit);
            //利用反射设置当前BoundSql对应的sql属性为我们建立好的分页Sql语句
            ReflectUtils.setValueByFieldName(boundSql, "sql", pageSql);
            logger.debug("生成分页SQL : " + boundSql.getSql());
	    }
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}

	/**
     * 设置总记录数
     * @param condition Mapper映射语句对应的参数对象
     * @param mappedStatement Mapper映射语句
     * @param connection 当前的数据库连接
     */
    private void setTotalRowNum(Map<String, Object> condition, MappedStatement mappedStatement, Connection connection) {
       BoundSql boundSql = mappedStatement.getBoundSql(condition);
       String sql = boundSql.getSql();
       //通过查询Sql语句获取到对应的计算总记录数的sql语句
       String countSql = obtainCountSql(sql);
       if (null == countSql) {
    	   return;
       }
       //通过BoundSql获取对应的参数映射
       List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
       //利用Configuration、查询记录数的Sql语句countSql、参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。
       BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, condition);
       //通过mappedStatement、参数对象page和BoundSql对象countBoundSql建立一个用于设定参数的ParameterHandler对象
       ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, condition, countBoundSql);
       //通过connection建立一个countSql对应的PreparedStatement对象。
       PreparedStatement pstmt = null;
       ResultSet rs = null;
       try {
           pstmt = connection.prepareStatement(countSql);
           //通过parameterHandler给PreparedStatement对象设置参数
           parameterHandler.setParameters(pstmt);
           //之后就是执行获取总记录数的Sql语句和获取结果了。
           rs = pstmt.executeQuery();
           if (rs.next()) {
              int totalRecord = rs.getInt(1);
              condition.put(QueryCondition.TOTAL_ROW_NUM, totalRecord);
           }
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           try {
              if (rs != null)
                  rs.close();
               if (pstmt != null)
                  pstmt.close();
           } catch (SQLException e) {
              e.printStackTrace();
           }
       }
    }

    /**
     * 根据原Sql语句获取对应的查询总记录数的Sql语句
     * @param sql
     * @return
     */
    private String obtainCountSql(String sql) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT COUNT(1) FROM (").append(sql).append(") as countSQL");
    	return sb.toString();
    }

}
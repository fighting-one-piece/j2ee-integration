package org.platform.utils.database.ds;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.platform.utils.PlatformConstants;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiLocatorSupport;
import org.springframework.util.StringUtils;

@SuppressWarnings("rawtypes")
public class DataSourceFactoryBean implements FactoryBean, InitializingBean {

	private DataSource dataSource;
	private String dataSourceType;
	private String jndiName;
    private String driverUrl;
    private String user;
    private String password;
    private String driver;
    private String alias;

    private int maximumActiveTime;
    private int maximumConnectionCount;
    private int minimumConnectionCount;
    private int simultaneousBuildThrottle;
    private String delegateProperties;


	public void afterPropertiesSet() throws Exception {
		if(!StringUtils.hasText(this.dataSourceType)) {
			throw new IllegalArgumentException("dataSourceType is required");
		}
		if (PlatformConstants.DATASOURCE_TYPE_JNDI.equals(dataSourceType)) {
			if (!StringUtils.hasText(this.jndiName)) {
				throw new IllegalArgumentException("jndiName is required");
			}
		} else {
			if (!StringUtils.hasText(this.driver)) {
				throw new IllegalArgumentException("driverClassName is required");
			}
			if (!StringUtils.hasText(this.driverUrl)) {
				throw new IllegalArgumentException("url is required");
			}
			if (!StringUtils.hasText(this.user)) {
				throw new IllegalArgumentException("username is required");
			}
			if (!StringUtils.hasText(this.password)) {
				throw new IllegalArgumentException("password is required");
			}
		}
		createDataSource();
	}

	public Object getObject() {
		DataSource ds = createDataSource();
		return ds;
	}

	protected DataSource createDataSource() {
		DataSource ds = this.dataSource;
		if (ds == null) {
			try {
				if (PlatformConstants.DATASOURCE_TYPE_JNDI.equals(dataSourceType)) {
					ds = new JndiDataSourceSupport().lookupDataSource(this.jndiName);
				} else if(PlatformConstants.DATASOURCE_TYPE_JDBC.equals(dataSourceType)){
					ds = new DriverManagerDataSource(driverUrl, user, password);
				}else if(PlatformConstants.DATASOURCE_TYPE_PROXOOL.equals(dataSourceType)){
					ds = createProxoolDataSource();
				}
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		this.dataSource = ds;
		return ds;
	}

	public Class getObjectType() {
		return javax.sql.DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	private ProxoolDataSource createProxoolDataSource(){
		ProxoolDataSource ps = new ProxoolDataSource();
		ps.setAlias(alias);
		ps.setDriver(driver);
		ps.setDriverUrl(driverUrl);
		ps.setPassword(password);
		ps.setUser(user);
		ps.setDelegateProperties(delegateProperties);
		ps.setMaximumConnectionCount(maximumConnectionCount);
		ps.setMinimumConnectionCount(minimumConnectionCount);
		ps.setMaximumActiveTime(maximumActiveTime);
		ps.setSimultaneousBuildThrottle(simultaneousBuildThrottle);
		return ps;
	}

	private class JndiDataSourceSupport extends JndiLocatorSupport {
		public DataSource lookupDataSource(String jndiName)
				throws NamingException {
			return super.lookup(jndiName,
					javax.sql.DataSource.class);
		}
	}

	public String getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setMaximumActiveTime(int maximumActiveTime) {
		this.maximumActiveTime = maximumActiveTime;
	}

	public void setMaximumConnectionCount(int maximumConnectionCount) {
		this.maximumConnectionCount = maximumConnectionCount;
	}

	public void setMinimumConnectionCount(int minimumConnectionCount) {
		this.minimumConnectionCount = minimumConnectionCount;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSimultaneousBuildThrottle(int simultaneousBuildThrottle) {
		this.simultaneousBuildThrottle = simultaneousBuildThrottle;
	}

	public void setDriverUrl(String driverUrl) {
		this.driverUrl = driverUrl;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setDelegateProperties(String delegateProperties) {
		this.delegateProperties = delegateProperties;
	}

}

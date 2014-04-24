package org.platform.utils.bigdata.mahout;

import java.util.Collection;

import javax.sql.DataSource;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.jdbc.AbstractJDBCDataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

@SuppressWarnings("unused")
public class NewJDBCDataModel extends AbstractJDBCDataModel {
	
	private static final long serialVersionUID = 1L;
	
	public NewJDBCDataModel() {
		super(null, null, null, null,
				null, null, null, null,
				null, null, null, null,
				null, null, null, null);
	}

	protected NewJDBCDataModel(DataSource dataSource, String getPreferenceSQL,
			String getPreferenceTimeSQL, String getUserSQL,
			String getAllUsersSQL, String getNumItemsSQL,
			String getNumUsersSQL, String setPreferenceSQL,
			String removePreferenceSQL, String getUsersSQL, String getItemsSQL,
			String getPrefsForItemSQL, String getNumPreferenceForItemSQL,
			String getNumPreferenceForItemsSQL, String getMaxPreferenceSQL,
			String getMinPreferenceSQL) {
		super(dataSource, getPreferenceSQL, getPreferenceTimeSQL, getUserSQL,
				getAllUsersSQL, getNumItemsSQL, getNumUsersSQL, setPreferenceSQL,
				removePreferenceSQL, getUsersSQL, getItemsSQL, getPrefsForItemSQL,
				getNumPreferenceForItemSQL, getNumPreferenceForItemsSQL,
				getMaxPreferenceSQL, getMinPreferenceSQL);
		// TODO Auto-generated constructor stub
	}

	

}

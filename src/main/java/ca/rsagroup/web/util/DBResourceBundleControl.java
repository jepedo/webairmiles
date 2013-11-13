package ca.rsagroup.web.util;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import com.mysql.jdbc.StringUtils;

public class DBResourceBundleControl extends ResourceBundle.Control {
	DataSource dataSource;
	String tableName;
	public DBResourceBundleControl(DataSource dataSource, String tableName) {
		this.dataSource= dataSource;
		this.tableName = tableName;
	}
	public static final String FORMAT_DB = "FORMAT_DB";

	@Override
	public ResourceBundle newBundle(String      baseName,
			Locale      locale,
			String      format,
			ClassLoader loader,
			boolean     reload)
					throws IllegalAccessException, InstantiationException, IOException {
		if ((baseName == null) || (locale == null) || (format == null) ) {
			throw new NullPointerException();
		}
		if (!format.equals(FORMAT_DB)) {
			return null;
		}
		Properties p   = new Properties();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs   = null;
		try
		{
			con = dataSource.getConnection();			
			stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
					ResultSet.CONCUR_READ_ONLY);
			StringBuilder query = new StringBuilder();
			query.append("select string_id, value, variant from "+tableName+" where bundle='"
					+ baseName + "' ");
			if (locale != null)
			{
				// TODO -  enable this when needed (HM)
//				if (!StringUtils.isNullOrEmpty(locale.getCountry()))
//				{
//					query.append("and country_code='" + locale.getCountry() + "' ");
//				}
				if (!StringUtils.isNullOrEmpty(locale.getLanguage()))
				{
					query.append("and language_code='" + locale.getLanguage() + "' ");
				}
				if (!StringUtils.isNullOrEmpty(locale.getVariant()))
				{
					query.append("and variant in ('" + locale.getVariant() + "' , '0') order by variant asc");
				}
				
				rs = stmt.executeQuery(query.toString());

				while (rs.next())								
					p.setProperty(rs.getString(1), rs.getString(2));

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("Can not build properties: " + e);
		}
		finally
		{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return new DBResourceBundle(p);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static Object getFormatDb() {
		return FORMAT_DB;
	}
}

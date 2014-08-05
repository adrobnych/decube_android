package com.droidbrew.decube.model.spec.db;

import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

public class TestDbHelper {
	private static final String DB_NAME = "unit_tests.db";
	private ConnectionSource connectionSource;
	private String databaseUrl = "jdbc:sqlite:" + DB_NAME;

	public ConnectionSource getConnectionSource() {
		try {
			connectionSource = new JdbcConnectionSource(databaseUrl);
			return connectionSource;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

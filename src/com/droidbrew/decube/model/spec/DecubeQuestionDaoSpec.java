package com.droidbrew.decube.model.spec;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.droidbrew.decube.model.Question;
import com.droidbrew.decube.model.spec.db.TestDbHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DecubeQuestionDaoSpec {
	static Dao<Question, Integer> questionDao = null;
	static ConnectionSource connectionSource = null;

	@BeforeClass
	public static void setUpDatabaseLayer() throws SQLException {
		connectionSource = new TestDbHelper().getConnectionSource();
		TableUtils.createTableIfNotExists(connectionSource, Question.class);

		try {
			questionDao = DaoManager
					.createDao(connectionSource, Question.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Before
	public void clearData() {
		try {
			TableUtils.clearTable(connectionSource, Question.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void canBeStoredInDB() {
		Question data1 = new Question("Which animal buy?");
		Question data2 = new Question("What to drink?");
		Question read_data1 = null, read_data2 = null;
		try {
			questionDao.create(data1);
			questionDao.create(data2);

			read_data1 = questionDao.queryForId(1);
			read_data2 = questionDao.queryForId(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertEquals("Which animal buy?", read_data1.getQuestion());
		assertEquals("What to drink?", read_data2.getQuestion());
	}

}

package com.droidbrew.decube.model.spec;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.spec.db.TestDbHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DecubeAnswerDaoSpec {
	static Dao<Answer, Integer> answerDao = null;
	static ConnectionSource connectionSource = null;

	@BeforeClass
	public static void setUpDatabaseLayer() throws SQLException {
		connectionSource = new TestDbHelper().getConnectionSource();
		TableUtils.createTableIfNotExists(connectionSource, Answer.class);

		try {
			answerDao = DaoManager
					.createDao(connectionSource, Answer.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Before
	public void clearData() {
		try {
			TableUtils.clearTable(connectionSource, Answer.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void canBeStoredInDB() {
		Answer data1 = new Answer(1, "Cat");
		Answer data2 = new Answer(2, "Beer");
		Answer read_data1 = null, read_data2 = null;
		try {
			answerDao.create(data1);
			answerDao.create(data2);

			read_data1 = answerDao.queryForId(1);
			read_data2 = answerDao.queryForId(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		assertEquals("Cat", read_data1.getAnswer());
		assertEquals("Beer", read_data2.getAnswer());
	}
}

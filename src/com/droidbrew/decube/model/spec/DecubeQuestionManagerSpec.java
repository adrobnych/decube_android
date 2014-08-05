package com.droidbrew.decube.model.spec;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.droidbrew.decube.model.Question;
import com.droidbrew.decube.model.QuestionManager;
import com.droidbrew.decube.model.spec.db.TestDbHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DecubeQuestionManagerSpec {
	static QuestionManager qm = null;
	static ConnectionSource connectionSource = null;
	static Dao<Question, Integer> questionDao = null;

	@BeforeClass
	public static void setUpDatabaseLayer() throws SQLException {
		connectionSource = new TestDbHelper().getConnectionSource();
		TableUtils.createTableIfNotExists(connectionSource, Question.class);

		qm = new QuestionManager();

		try {
			questionDao = DaoManager.createDao(connectionSource, Question.class);
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
	public void getCategoryDataManager() {
		Question data1 = new Question("Which animal buy?");
		Question data2 = new Question("What to drink?");
		
		try {
			questionDao.create(data1);
			questionDao.create(data2);
			qm.setDataQuestionDao(questionDao);
			
			assertEquals("Which animal buy?", qm.getDataQuestion().get(0).getQuestion());
			assertEquals("What to drink?", qm.getDataQuestion().get(1).getQuestion());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

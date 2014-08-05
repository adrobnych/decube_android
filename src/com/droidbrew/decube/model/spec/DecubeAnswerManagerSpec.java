package com.droidbrew.decube.model.spec;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.AnswerManager;
import com.droidbrew.decube.model.spec.db.TestDbHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DecubeAnswerManagerSpec {
	static AnswerManager am = null;
	static ConnectionSource connectionSource = null;
	static Dao<Answer, Integer> answerDao = null;

	@BeforeClass
	public static void setUpDatabaseLayer() throws SQLException {
		connectionSource = new TestDbHelper().getConnectionSource();
		TableUtils.createTableIfNotExists(connectionSource, Answer.class);

		am = new AnswerManager();

		try {
			answerDao = DaoManager.createDao(connectionSource, Answer.class);
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
	public void getAnswerDataManager() {
		Answer data1 = new Answer(1, "Cat");
		Answer data2 = new Answer(2, "Beer");
		
		try {
			answerDao.create(data1);
			answerDao.create(data2);
			am.setDataAnswerDao(answerDao);
			
			assertEquals("Cat", am.getDataAnswer().get(0).getAnswer());
			assertEquals("Beer", am.getDataAnswer().get(1).getAnswer());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

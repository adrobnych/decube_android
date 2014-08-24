package com.droidbrew.decube.model.spec;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.AnswerManager;
import com.droidbrew.decube.model.Question;
import com.droidbrew.decube.model.QuestionManager;
import com.droidbrew.decube.model.spec.db.TestDbHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DecubeAnswerManagerSpec {
	static AnswerManager am = null;
	static QuestionManager qm = null;
	static ConnectionSource connectionSource = null;
	static Dao<Answer, Integer> answerDao = null;
	static Dao<Question, Integer> questionDao = null;

	@BeforeClass
	public static void setUpDatabaseLayer() throws SQLException {
		connectionSource = new TestDbHelper().getConnectionSource();
		TableUtils.createTableIfNotExists(connectionSource, Answer.class);
		TableUtils.createTableIfNotExists(connectionSource, Question.class);

		am = new AnswerManager();
		qm = new QuestionManager();

		try {
			answerDao = DaoManager.createDao(connectionSource, Answer.class);
			questionDao = DaoManager.createDao(connectionSource, Question.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void clearData() {
		try {
			TableUtils.clearTable(connectionSource, Answer.class);
			TableUtils.clearTable(connectionSource, Question.class);
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
	
	@Test
	public void deleteAnswerManager() {
		Answer data1 = new Answer(1, "Cat");
		Answer data2 = new Answer(2, "Beer");
		
		try {
			answerDao.create(data1);
			answerDao.create(data2);
			am.setDataAnswerDao(answerDao);
			am.removeAnswerAtId(1);
			am.removeAnswerAtId(2);
			
			assertEquals(null, am.findAnswerById(1));
			assertEquals(null, am.findAnswerById(2));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findAnswerManager() {
		Answer data1 = new Answer(1, "Cat");
		Answer data2 = new Answer(2, "Beer");
		
		try {
			answerDao.create(data1);
			answerDao.create(data2);
			am.setDataAnswerDao(answerDao);
			
			assertEquals("Cat", am.findAnswerById(1).getAnswer());
			assertEquals("Beer", am.findAnswerById(2).getAnswer());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findAnswerByQuestionId() {
		Question data = new Question("Animals?");
		
		Answer data1 = new Answer(1, "Cat");
		Answer data2 = new Answer(1, "Beer");
		
		try {
			answerDao.create(data1);
			answerDao.create(data2);
			am.setDataAnswerDao(answerDao);
			questionDao.create(data);
			qm.setDataQuestionDao(questionDao);
			
			assertEquals(2, am.findAnswerByQuestionId(1).size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRemoveByQuestionId() {
		Question data = new Question("Animals?");
		
		Answer data1 = new Answer(1, "Cat");
		Answer data2 = new Answer(1, "Beer");
		
		try {
			answerDao.create(data1);
			answerDao.create(data2);
			am.setDataAnswerDao(answerDao);
			questionDao.create(data);
			qm.setDataQuestionDao(questionDao);
			
			am.removeByQuestionId(1);
			
			assertEquals(0 , am.findAnswerByQuestionId(1).size());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateAnswer() {
		Answer data1 = new Answer(1, "Cat");
		String upName = "upCat";
		
		try {
			answerDao.create(data1);
			am.setDataAnswerDao(answerDao);
			
			am.updateAnswer(1, upName);
			
			assertEquals("upCat", am.findAnswerById(1).getAnswer());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

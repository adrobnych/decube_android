package com.droidbrew.decube.model;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

public class QuestionManager {
	private Dao<Question, Integer> questionDao = null;

	public QuestionManager() {
		super();
	}

	public Dao<Question, Integer> getQuestionDao() {
		return questionDao;
	}

	public void setDataQuestionDao(Dao<Question, Integer> dataDao) {
		this.questionDao = dataDao;
	}

	public List<Question> getDataQuestion() throws SQLException {
		return questionDao.queryForAll();
	}

	public void removeQuestionAtId(int id) throws SQLException {
		questionDao.deleteById(id);

	}

	public Question findQuestionById(int id) throws SQLException {
		return questionDao.queryForId(id);
	}
	
	public void updateQuestion(int id, String upQuestion){
		UpdateBuilder<Question, Integer> db = questionDao.updateBuilder();
		try {
			db.where().eq("id", id);
			db.updateColumnValue("question", upQuestion);
			db.update();
		} catch (SQLException e) {
			Log.e(QuestionManager.class.getName(), e.getMessage(), e);
		}
	}
}

package com.droidbrew.decube.model;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

public class AnswerManager {
	private Dao<Answer, Integer> answerDao = null;

	public Dao<Answer, Integer> getAnswerDao() {
		return answerDao;
	}

	public void setDataAnswerDao(Dao<Answer, Integer> answerDao) {
		this.answerDao = answerDao;
	}

	public List<Answer> getDataAnswer() throws SQLException {
		return answerDao.queryForAll();
	}

	public AnswerManager() {
		super();
	}

	public void removeAnswerAtId(int id) throws SQLException {
		answerDao.deleteById(id);
	}

	public Answer findAnswerById(int id) throws SQLException {
		return answerDao.queryForId(id);
	}

	public List<Answer> findAnswerByQuestionId(int idQuestion)
			throws SQLException {
		List<Answer> result = null;
		result = getAnswerDao().queryBuilder().where()
				.eq("idQuestion", idQuestion).query();
		return result;
	}

	public void removeByQuestionId(int idQuestoin) throws SQLException {
		DeleteBuilder<Answer, Integer> db = answerDao.deleteBuilder();
		db.where().eq("idQuestion", idQuestoin);
		db.delete();
	}
	
	public void updateAnswer(int id, String upAnswer){
		
		UpdateBuilder<Answer, Integer> db = answerDao.updateBuilder();
		try {
			db.where().eq("id", id);
			db.updateColumnValue("answer", upAnswer);
			db.update();
		} catch (SQLException e) {
			Log.e(AnswerManager.class.getName(), e.getMessage(), e);
		}
		
	}
}

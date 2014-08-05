package com.droidbrew.decube;

import java.sql.SQLException;

import android.app.Application;

import com.droidbrew.decube.db.DecubeAppDBHelper;
import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.AnswerManager;
import com.droidbrew.decube.model.Question;
import com.droidbrew.decube.model.QuestionManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public class DecubeApp extends Application {

	private QuestionManager questionManager = null;
	private AnswerManager answerManager = null;
	private DecubeAppDBHelper dbHelper = null;

	public DecubeApp() {
		super();
		try {
			dbHelper = new DecubeAppDBHelper(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public QuestionManager getQuestionManager() {
		if (questionManager == null) {
			questionManager = new QuestionManager();
			try {
				Dao<Question, Integer> questionDao = DaoManager.createDao(
						dbHelper.getConnectionSource(), Question.class);
				questionManager.setDataQuestionDao(questionDao);

				if (questionDao.countOf() == 0) {
					questionDao.create(new Question("Which animal buy?"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return questionManager;
	}

	public AnswerManager getAnswerManager() {
		if (answerManager == null) {
			answerManager = new AnswerManager();
			try {
				Dao<Answer, Integer> answerDao = DaoManager.createDao(
						dbHelper.getConnectionSource(), Answer.class);
				answerManager.setDataAnswerDao(answerDao);

				if (answerDao.countOf() == 0) {
					answerDao.create(new Answer(1, "Cat"));
					answerDao.create(new Answer(1, "dog"));
					answerDao.create(new Answer(1, "fish"));
					answerDao.create(new Answer(1, "lion"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return answerManager;
	}

}
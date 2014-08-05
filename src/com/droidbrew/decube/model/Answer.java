package com.droidbrew.decube.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "answer")
public class Answer {
	@DatabaseField(id = true, canBeNull = false)
	private Integer id;
	@DatabaseField(columnName = "idQuestion", canBeNull = false, index = true, indexName = "idQuestion_index")
	private Integer idQuestion;
	@DatabaseField(columnName = "answer", canBeNull = false, index = true, indexName = "answer_index")
	private String answer;

	public Answer() {
		super();
	}

	public Answer(Integer idQuestion, String answer) {
		super();
		this.idQuestion = idQuestion;
		this.answer = answer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdQuestion() {
		return idQuestion;
	}

	public void setIdQuestion(Integer idQuestion) {
		this.idQuestion = idQuestion;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}

package com.droidbrew.decube.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "question")
public class Question {
	@DatabaseField(id = true, canBeNull = false)
	private Integer id;
	@DatabaseField(columnName = "question", canBeNull = false, index = true, indexName = "question_index")
	private String question;

	public Question() {
		super();
	}

	public Question(String question) {
		super();
		this.question = question;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

}

package com.droidbrew.decube;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.droidbrew.decube.adapter.QuestionCheckBoxAdapter;
import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.AnswerManager;
import com.droidbrew.decube.model.QuestionManager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class QuestionActivity extends Activity {
	QuestionManager questionManager;
	AnswerManager answerManager;
	QuestionCheckBoxAdapter questionCheckAdapter;
	ListView list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		questionManager = ((DecubeApp) getApplication()).getQuestionManager();
		answerManager = ((DecubeApp) getApplication()).getAnswerManager();

		try {
			questionCheckAdapter = new QuestionCheckBoxAdapter(
					getApplicationContext(), questionManager.getDataQuestion());
		} catch (SQLException e) {
			Log.e(QuestionActivity.class.getName(), e.getMessage(), e);
		}

		list = (ListView) findViewById(R.id.question_list);
		list.setAdapter(questionCheckAdapter);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		Button getValue = (Button) findViewById(R.id.get_value);
		getValue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resHome();
			}
		});
	}

	public void resHome() {
		if (questionCheckAdapter.getState().size() == 0)
			return;
		int[] ids = new int[questionCheckAdapter.getState().size()];
		int a = 0;
		List<Answer> randomList = new ArrayList<Answer>();
		for (int i : questionCheckAdapter.getState().keySet()) {
			ids[a] = questionCheckAdapter.getItem(i).getId();

			try {
				if (answerManager.findAnswerByQuestionId(ids[a]).size() == 0) {
					Toast.makeText(
							getApplicationContext(),
							"You have chosen a question which does not have a response. Please enter the answer or choose another question.",
							Toast.LENGTH_LONG).show();
					return;
				}
				randomList.addAll(((DecubeApp) getApplication())
						.getAnswerManager().findAnswerByQuestionId(ids[a]));
			} catch (SQLException e) {
				Log.e(QuestionActivity.class.getName(), e.getMessage(), e);
			}
			a++;
		}
		if (randomList.size() == 0)
			return;

		Intent intent = new Intent(QuestionActivity.this, HomeActivity.class);
		intent.putExtra("ids", ids);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_question, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_question_edit:
			Intent intent = new Intent(QuestionActivity.this,
					QuestionEditActivity.class);
			startActivity(intent);
			finish();
			break;
		case android.R.id.home:
			resHome();
			break;
		}
		return true;
	}

}

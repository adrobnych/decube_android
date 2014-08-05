package com.droidbrew.decube;

import java.sql.SQLException;
import com.droidbrew.decube.adapter.AnswerAdapter;
import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.AnswerManager;
import com.j256.ormlite.dao.Dao;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AnswerActivity extends Activity {
	AnswerManager am;
	AnswerAdapter answerAdapter;
	ListView list = null;
	private Dao<Answer, Integer> answerDao = null;
	private int questionId;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
		questionId = getIntent().getIntExtra("questionId", 0);

		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		am = ((DecubeApp) getApplication()).getAnswerManager();
		answerDao = ((DecubeApp) getApplication()).getAnswerManager()
				.getAnswerDao();

		try {
			answerAdapter = new AnswerAdapter(getApplicationContext(),
					am.findAnswerByQuestionId(questionId));
		} catch (SQLException e) {
			Log.e(AnswerActivity.class.getName(), e.getMessage(), e);
		}

		list = (ListView) findViewById(R.id.answer_list);
		list.setAdapter(answerAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				Intent intent = new Intent(AnswerActivity.this,
						EditItemAnswerActivity.class);
				intent.putExtra("questionId", questionId);
				intent.putExtra("answerId", (int) id);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_answer_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_answer_add:
			AddAnswer();
			break;
		case android.R.id.home:
			Intent intent = new Intent(this, EditItemQuestionActivity.class);
			intent.putExtra("questionId", questionId);
			startActivity(intent);
			finish();
			break;
		}
		return true;
	}

	public void AddAnswer() {
		final EditText answerEditText = new EditText(this);
		answerEditText.setHint("Enter a answer");
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Add new answer");
		dialog.setMessage("Add answer");
		dialog.setView(answerEditText);
		dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (answerEditText.getText().toString().equals("")) {
					return;
				}
				try {
					answerDao.create(new Answer(questionId, answerEditText
							.getText().toString()));
				} catch (SQLException e) {
					Log.e(AnswerActivity.class.getName(), e.getMessage(), e);
				}

				Intent intent = new Intent(AnswerActivity.this,
						AnswerActivity.class);
				intent.putExtra("questionId", questionId);
				startActivity(intent);
				finish();
			}
		});
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}
}

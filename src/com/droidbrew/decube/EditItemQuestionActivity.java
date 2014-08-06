package com.droidbrew.decube;

import java.sql.SQLException;

import com.droidbrew.decube.model.AnswerManager;
import com.droidbrew.decube.model.QuestionManager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EditItemQuestionActivity extends Activity {
	EditText editItem;
	Button btnSave, btnDelete;
	QuestionManager qm;
	AnswerManager am;

	private int questionId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item_question);

		questionId = getIntent().getIntExtra("questionId", 0);

		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		qm = ((DecubeApp) getApplication()).getQuestionManager();
		am = ((DecubeApp) getApplication()).getAnswerManager();
		try {
			qm.findQuestionById(questionId);
			editItem = (EditText) findViewById(R.id.edit_question_text);
			editItem.setText(qm.findQuestionById(questionId).getQuestion()
					.toString());
			editItem.setSelection(editItem.getText().length());
		} catch (SQLException e) {
			Log.e(EditItemQuestionActivity.class.getName(), e.getMessage(), e);
		}
	}

	public void dialogSaveQuestion() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.editQuestion_activity_dialigSave_title));
		dialog.setPositiveButton(
				getString(R.string.editQuestion_activity_dialigSave_pBut),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (editItem.getText().length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.editQuestion_activity_dialigSave_verification_null),
									Toast.LENGTH_SHORT).show();
							return;
						}
						qm.updateQuestion(questionId, editItem.getText()
								.toString());
						Toast.makeText(
								getApplicationContext(),
								getString(R.string.editQuestion_activity_dialigSave_verification_save),
								Toast.LENGTH_SHORT).show();
						Intent inten = new Intent(
								EditItemQuestionActivity.this,
								QuestionEditActivity.class);
						startActivity(inten);
						finish();
					}
				});
		dialog.setNegativeButton(
				getString(R.string.editQuestion_activity_dialigSave_nBut),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	public void onSaveQuestionClick(View view) {
		dialogSaveQuestion();
	}

	public void onDeleteQuestionClick(View view) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.editQuestion_activity_dialigDelete_title));
		try {
			dialog.setMessage(qm.findQuestionById(questionId).getQuestion()
					.toString());
		} catch (SQLException e) {
			Log.e(EditItemQuestionActivity.class.getName(), e.getMessage(), e);
		}
		dialog.setPositiveButton(
				getString(R.string.editQuestion_activity_dialigDelete_pBut),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						try {
							if (qm.getDataQuestion().size() == 1) {
								Toast.makeText(
										getApplicationContext(),
										getString(R.string.editQuestion_activity_dialigDelete_verification),
										Toast.LENGTH_SHORT).show();
								return;
							}
							qm.removeQuestionAtId(questionId);
							am.removeByQuestionId(questionId);
							Intent inten = new Intent(
									EditItemQuestionActivity.this,
									QuestionEditActivity.class);
							startActivity(inten);
							finish();
						} catch (SQLException e) {
							Log.e(EditItemQuestionActivity.class.getName(),
									e.getMessage(), e);
						}
					}
				});
		dialog.setNegativeButton(
				getString(R.string.editQuestion_activity_dialigDelete_nBut),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		dialogSaveQuestion();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_answer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_answer:
			Intent intent = new Intent(EditItemQuestionActivity.this,
					AnswerActivity.class);
			intent.putExtra("questionId", questionId);
			startActivity(intent);
			finish();
			break;
		case android.R.id.home:
			dialogSaveQuestion();
			break;
		}
		return true;
	}
}

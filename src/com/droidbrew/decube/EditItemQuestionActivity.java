package com.droidbrew.decube;

import java.sql.SQLException;

import com.droidbrew.decube.model.AnswerManager;
import com.droidbrew.decube.model.QuestionManager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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

	public void onSaveQuestionClick(View view) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Save Question?");
		dialog.setPositiveButton("Save", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (editItem.getText().length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Field is empty, type a question!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					if (editItem
							.getText()
							.toString()
							.equals(qm.findQuestionById(questionId)
									.getQuestion().toString())) {
						Toast.makeText(
								getApplicationContext(),
								"Before saving, you need to change the question field!",
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						qm.updateQuestion(questionId, editItem.getText()
								.toString());
						Toast.makeText(getApplicationContext(),
								"Question changed successfully!",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (SQLException e) {
					Log.e(EditItemQuestionActivity.class.getName(),
							e.getMessage(), e);
				}

			}
		});
		dialog.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void onDeleteQuestionClick(View view) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Delete Question: ");
		try {
			dialog.setMessage(qm.findQuestionById(questionId).getQuestion()
					.toString());
		} catch (SQLException e) {
			Log.e(EditItemQuestionActivity.class.getName(), e.getMessage(), e);
		}
		dialog.setPositiveButton("Delete", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				try {
					if (qm.getDataQuestion().size() == 1) {
						Toast.makeText(getApplicationContext(),
								"There must be at least one question",
								Toast.LENGTH_SHORT).show();
						return;
					}
					qm.removeQuestionAtId(questionId);
					am.removeByQuestionId(questionId);
					finish();
				} catch (SQLException e) {
					Log.e(EditItemQuestionActivity.class.getName(),
							e.getMessage(), e);
				}
			}
		});
		dialog.setNegativeButton("Cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
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
			Intent inten = new Intent(this, QuestionEditActivity.class);
			startActivity(inten);
			finish();
			break;
		}
		return true;
	}
}

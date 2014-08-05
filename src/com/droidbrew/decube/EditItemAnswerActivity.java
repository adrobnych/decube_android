package com.droidbrew.decube;

import java.sql.SQLException;

import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.AnswerManager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
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
public class EditItemAnswerActivity extends Activity {
	EditText answerEditText;
	Button btnSave, btnDelete;
	AnswerManager am;
	private int questionId, answerId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item_answer);
		questionId = getIntent().getIntExtra("questionId", 0);
		answerId = getIntent().getIntExtra("answerId", 0);

		int currentAPIVersion = android.os.Build.VERSION.SDK_INT;
		if (currentAPIVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		am = ((DecubeApp) getApplication()).getAnswerManager();

		try {
			am.findAnswerById(answerId);
			answerEditText = (EditText) findViewById(R.id.edit_answer_text);
			answerEditText.setText(am.findAnswerById(answerId).getAnswer()
					.toString());
			answerEditText.setSelection(answerEditText.getText().length());
		} catch (SQLException e) {
			Log.e(EditItemAnswerActivity.class.getName(), e.getMessage(), e);
		}
	}

	public void onSaveAnswerClick(View view) {

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Save Answer? ");
		dialog.setPositiveButton("Save", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (answerEditText.getText().length() == 0) {
					Toast.makeText(getApplicationContext(),
							"Field is empty, type a answer!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				try {
					if (answerEditText
							.getText()
							.toString()
							.equals(am.findAnswerById(answerId).getAnswer()
									.toString())) {
						Toast.makeText(
								getApplicationContext(),
								"Before saving, you need to change the answer field!",
								Toast.LENGTH_SHORT).show();
						return;
					} else {
						am.updateAnswer(answerId, answerEditText.getText()
								.toString());
						Toast.makeText(getApplicationContext(),
								"Answer changed successfully!",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				} catch (SQLException e) {
					Log.e(EditItemAnswerActivity.class.getName(),
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

	public void onDeleteAnswerClick(View view) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Delete Answer: ");
		try {
			dialog.setMessage(am.findAnswerById(answerId).getAnswer()
					.toString());
		} catch (SQLException e) {
			Log.e(EditItemAnswerActivity.class.getName(), e.getMessage(), e);
		}
		dialog.setPositiveButton("Delete", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				try {
					am.removeAnswerAtId(answerId);
					finish();
				} catch (SQLException e) {
					Log.e(EditItemAnswerActivity.class.getName(),
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
		getMenuInflater().inflate(R.menu.menu_answer_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, AnswerActivity.class);
			intent.putExtra("questionId", questionId);
			intent.putExtra("answerId", answerId);
			startActivity(intent);
			finish();
			break;
		}
		return true;
	}
}

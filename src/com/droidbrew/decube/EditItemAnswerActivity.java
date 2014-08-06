package com.droidbrew.decube;

import java.sql.SQLException;

import com.droidbrew.decube.model.AnswerManager;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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

	public void dialogSaweAnswer() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.editAnswer_activity_dialigSaveAnswer_title));
		dialog.setPositiveButton(
				getString(R.string.editAnswer_activity_dialigSaveAnswer_pBut),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (answerEditText.getText().length() == 0) {
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.editAnswer_activity_dialigSaveAnswer_verification_null),
									Toast.LENGTH_SHORT).show();
							return;
						}
						am.updateAnswer(answerId, answerEditText.getText()
								.toString());
						Toast.makeText(
								getApplicationContext(),
								getString(R.string.editAnswer_activity_dialigSaveAnswer_verification_save),
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(EditItemAnswerActivity.this,
								AnswerActivity.class);
						intent.putExtra("questionId", questionId);
						intent.putExtra("answerId", answerId);
						startActivity(intent);
						finish();
					}
				});
		dialog.setNegativeButton(
				getString(R.string.editAnswer_activity_dialigSaveAnswer_nBut),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	public void onSaveAnswerClick(View view) {
		dialogSaweAnswer();
	}

	public void onDeleteAnswerClick(View view) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.editAnswer_activity_dialigDeleteAnswer_title));
		try {
			dialog.setMessage(am.findAnswerById(answerId).getAnswer()
					.toString());
		} catch (SQLException e) {
			Log.e(EditItemAnswerActivity.class.getName(), e.getMessage(), e);
		}
		dialog.setPositiveButton(
				getString(R.string.editAnswer_activity_dialigDeleteAnswer_pBut),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						try {
							am.removeAnswerAtId(answerId);
							Intent intent = new Intent(
									EditItemAnswerActivity.this,
									AnswerActivity.class);
							intent.putExtra("questionId", questionId);
							intent.putExtra("answerId", answerId);
							startActivity(intent);
							finish();
						} catch (SQLException e) {
							Log.e(EditItemAnswerActivity.class.getName(),
									e.getMessage(), e);
						}
					}
				});
		dialog.setNegativeButton(
				getString(R.string.editAnswer_activity_dialigDeleteAnswer_nBut),
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
		dialogSaweAnswer();
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
			dialogSaweAnswer();
			break;
		}
		return true;
	}
}

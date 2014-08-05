package com.droidbrew.decube.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.Question;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DecubeAppDBHelper extends OrmLiteSqliteOpenHelper {
	private static final String TAG = "com.droidbrew.decubeapp.db.DecubeAppDBHelper";
	private static final String DB_NAME = "decubeapp.db";
	private static final int DB_VERSION = 1;
	private Context context;

	public DecubeAppDBHelper(Context context) throws SQLException {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {

			TableUtils.createTableIfNotExists(connectionSource, Question.class);
			TableUtils.createTableIfNotExists(connectionSource, Answer.class);

		} catch (java.sql.SQLException e) {
			Log.e(TAG, "onCreate", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {

	}

	public Context getContext() {
		return context;
	}
}
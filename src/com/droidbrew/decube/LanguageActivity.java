package com.droidbrew.decube;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class LanguageActivity extends PreferenceActivity {
	PendingIntent intent;
	SharedPreferences preferences;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_lang);

		Preference enrestart = (Preference) findPreference("enrestart");
		Preference rurestart = (Preference) findPreference("rurestart");

		intent = PendingIntent.getActivity(getApplicationContext(), 0,
				new Intent(LanguageActivity.this, HomeActivity.class), 0);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences.Editor editor = preferences.edit();

		enrestart
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						editor.putString("lang", "en");
						editor.commit();
						restart();
						return true;
					}
				});

		rurestart
				.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						editor.putString("lang", "ru");
						editor.commit();
						restart();
						return true;
					}
				});
	}

	private void restart() {
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);
		System.exit(1);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

}
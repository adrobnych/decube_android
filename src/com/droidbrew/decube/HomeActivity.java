package com.droidbrew.decube;

import java.util.ArrayList;
import java.util.List;

import com.droidbrew.decube.model.Answer;
import com.droidbrew.decube.model.AnswerManager;
import com.droidbrew.decube.model.Question;
import com.droidbrew.decube.model.QuestionManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity implements SensorEventListener {
	AnswerManager am;
	QuestionManager qm;

	//WebView randomTV;
	List<Answer> randomList;
	Answer[] arrayRandom;
	WebView wb;

	ImageView imgCube;
	AnimationDrawable animCube;
	Animation anim = null;
	
	private SensorManager sensorManager;
	private long lastUpdate;
	private boolean rand = false;
	int random;
	int[] ids;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		am = ((DecubeApp) getApplication()).getAnswerManager();
		qm = ((DecubeApp) getApplication()).getQuestionManager();
		
		wb = ((WebView) findViewById(R.id.result));
		wb.setBackgroundColor(Color.TRANSPARENT);
		String summary = "<html><body style='background-color:transparent'> <br/><br/><br/> <h2 align='center'>Shake to get answers</h2> </body></html>";
		wb.loadDataWithBaseURL(null, summary, "text/html", "utf-8", null);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		lastUpdate = System.currentTimeMillis();
		
		//randomTV = (WebView)findViewById(R.id.random_tv);
		randomList = new ArrayList<Answer>();
		
		Intent intent = getIntent();
		ids = intent .getIntArrayExtra("ids");
		if(ids == null)
			ids = new int[] {1};

		imgCube = (ImageView) findViewById(R.id.img_cube);
		imgCube.setBackgroundResource(R.anim.anim_cube);
		imgCube.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				spinStart();
				randomResult();
				
			}
		});

		animCube = (AnimationDrawable) imgCube.getBackground();
		
		anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_combo);
	}

	public void spinStart() {
		SpinCube spin = new SpinCube();
		spin.execute();
	}
	
	public void randomResult(){
		String str = "<html><body align='center' style='background-color:transparent'> <br /> <h2>";
		try {
			for(int i : ids) {
				Question question = qm.findQuestionById(i);
				str += question.getQuestion() + "<br />";
				
				randomList = am.findAnswerByQuestionId(i);
				random = (int) (Math.random() * randomList.size());
				str += randomList.get(random).getAnswer() + "<br />"; 
				
				Log.i("TAG",str);
			}
			
			
		} catch(Exception ex) {
			Log.e(HomeActivity.class.getName(), ex.getMessage(), ex);
		}
		str += "</h2> <br /></body></html>";
		
		wb.setBackgroundColor(Color.TRANSPARENT);
		wb.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
		wb.startAnimation(anim);
	}

	class SpinCube extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(2500);
				Log.d("sleep", "2000");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			animCube.start();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			animCube.stop();
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			float x = values[0];
			float y = values[1];
			float z = values[2];
			float accelationSquareRoot = (x * x + y * y + z * z)
					/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
			long actualTime = System.currentTimeMillis();
			if (accelationSquareRoot >= 3) {
				if (actualTime - lastUpdate < 1300) {
					return;
				}
				lastUpdate = actualTime;
				//if (rand) {
					spinStart();
					randomResult();
				//} 
				//rand = !rand;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, QuestionActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return true;
	}
}

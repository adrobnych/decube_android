package com.droidbrew.decube.adapter;

import java.util.List;

import com.droidbrew.decube.R;
import com.droidbrew.decube.model.Question;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionAdapter extends BaseAdapter {
	private List<Question> question;
	private LayoutInflater inflater;

	private class ViewHolder {
		public TextView tv;
	}

	public QuestionAdapter(Context context, List<Question> question) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.question = question;
	}

	@Override
	public int getCount() {
		if (question != null)
			return question.size();
		return 0;
	}
	
	@Override
	public Object getItem(int position) {
		if (question != null && position >= 0 && position < getCount())
			return question.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (question != null && position >= 0 && position < getCount())
			return question.get(position).getId();
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder viewHolder;

		if (view == null) {
			view = inflater.inflate(R.layout.question_adapter, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.tv = (TextView) view
					.findViewById(R.id.question_adapter_tv);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		Question record = question.get(position);

		viewHolder.tv.setText("" + record.getQuestion());

		return view;
	}

}
package com.droidbrew.decube.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.droidbrew.decube.R;
import com.droidbrew.decube.model.Question;

public class QuestionCheckBoxAdapter extends BaseAdapter {
	private List<Question> question;
	private LayoutInflater inflater;
	private HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();

	public HashMap<Integer, Boolean> getState() {
		return state;
	}

	private class ViewHolder {
		public TextView tv;
		protected CheckBox checkbox;
	}

	public QuestionCheckBoxAdapter(Context context, List<Question> question) {
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
	public Question getItem(int position) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view;
		final ViewHolder viewHolder;

		view = inflater.inflate(R.layout.question_check_box_adapter, parent,
				false);

		viewHolder = new ViewHolder();
		viewHolder.tv = (TextView) view.findViewById(R.id.question_tv);
		viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);

		viewHolder.checkbox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							state.put(position, isChecked);
						} else {
							state.remove(position);
						}
					}
				});

		view.setTag(viewHolder);

		viewHolder.checkbox.setTag(question.get(position));

		Question record = question.get(position);

		viewHolder.tv.setText("" + record.getQuestion());
		viewHolder.checkbox.setChecked((state.get(position) == null ? false
				: true));
		return view;
	}

}
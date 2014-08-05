package com.droidbrew.decube.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droidbrew.decube.R;
import com.droidbrew.decube.model.Answer;

public class AnswerAdapter extends BaseAdapter {
	private List<Answer> answer;
	private LayoutInflater inflater;

	private class ViewHolder {
		public TextView tv;
	}

	public AnswerAdapter(Context context, List<Answer> answer) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.answer = answer;
	}

	@Override
	public int getCount() {
		if (answer != null)
			return answer.size();
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (answer != null && position >= 0 && position < getCount())
			return answer.get(position);
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (answer != null && position >= 0 && position < getCount())
			return answer.get(position).getId();
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder viewHolder;

		if (view == null) {
			view = inflater.inflate(R.layout.answer_adapter, parent, false);

			viewHolder = new ViewHolder();
			viewHolder.tv = (TextView) view
					.findViewById(R.id.answer_adapter_tv);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		Answer record = answer.get(position);

		viewHolder.tv.setText("" + record.getAnswer());
		return view;
	}

}
package com.nt.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//public class CustomSpinner extends Activity {
//
//	String[] numbers = { "One", "Two", "Three", "Four", "Five" };
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.main);
//
//		Spinner spinner = (Spinner) findViewById(R.id.spinner);
//
//		SpinnerAdapter adapter = new SpinnerAdapter(this,
//				android.R.layout.simple_spinner_item, numbers);
//
//		spinner.setAdapter(adapter);
//
//	}

	public class SpinnerAdapter extends ArrayAdapter<String> {

		Context context;

		String[] items = new String[] {};
		
		int textViewResourceId;

		public SpinnerAdapter(final Context context,

		final int textViewResourceId, final String[] objects) {

			super(context, textViewResourceId, objects);

			this.items = objects;

			this.context = context;

			this.textViewResourceId = textViewResourceId;
		}

		@Override
		public View getDropDownView(int position, View convertView,

		ViewGroup parent) {

			if (convertView == null) {

				LayoutInflater inflater = LayoutInflater.from(context);

				convertView = inflater.inflate(textViewResourceId, parent, false);

			}

			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);

			tv.setText(items[position]);

			tv.setTextColor(Color.BLUE);

			tv.setTextSize(20);

			return convertView;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				LayoutInflater inflater = LayoutInflater.from(context);

				convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);

			}

			// android.R.id.text1 is default text view in resource of the
			// android.

			// android.R.layout.simple_spinner_item is default layout in
			// resources of android.

			TextView tv = (TextView) convertView.findViewById(android.R.id.text1);

			tv.setText(items[position]);

			tv.setTextColor(Color.DKGRAY);

			tv.setTextSize(20);

			return convertView;

		}
	}
//}
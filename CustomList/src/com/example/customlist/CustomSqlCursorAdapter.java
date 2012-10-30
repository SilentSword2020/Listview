package com.example.customlist;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomSqlCursorAdapter extends SimpleCursorAdapter implements
OnClickListener{


	private Context context;

	private SqlHelper dbHelper;
	private Cursor currentCursor;

	public CustomSqlCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, SqlHelper dbHelper) {
		super(context, layout, c, from, to);
		this.currentCursor = c;
		this.context = context;
		this.dbHelper = dbHelper;

	}

	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.single_item, null);
		}

		this.currentCursor.moveToPosition(pos);

		CheckBox cBox = (CheckBox) v.findViewById(R.id.bcheck);

		cBox.setTag(Integer.parseInt(this.currentCursor
				.getString(this.currentCursor
						.getColumnIndex(SqlHelper.COLUMN_ID))));

		if (this.currentCursor.getString(this.currentCursor
				.getColumnIndex(SqlHelper.COLUMN_SELECTED)) != null
				&& Integer.parseInt(this.currentCursor
						.getString(this.currentCursor
								.getColumnIndex(SqlHelper.COLUMN_SELECTED))) != 0) {
			cBox.setChecked(true);
		} else {
			cBox.setChecked(false);
		}
		cBox.setOnClickListener(this);

		TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
		txtTitle.setText(this.currentCursor.getString(this.currentCursor
				.getColumnIndex(SqlHelper.COLUMN_TITLE)));

		return (v);
	}

	public void ClearSelections() {
		this.dbHelper.clearSelections();
		this.currentCursor.requery();

	}

	@Override
	public void onClick(View v) {

		CheckBox cBox = (CheckBox) v;
		Integer _id = (Integer) cBox.getTag();

		ContentValues values = new ContentValues();
		values.put(" selected", cBox.isChecked() ? 1 : 0);
		this.dbHelper.dbSqlite.update(SqlHelper.TABLE_NAME, values, "_id=?",
				new String[] { Integer.toString(_id) });
	}

}

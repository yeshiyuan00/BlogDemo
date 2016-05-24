package com.yesy.expandablerecycleview;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.ysy.blogdemo.R;

/**
 * Author: yeshiyuan
 * Date: 5/24/16.
 */
public class CrimeChildViewHolder extends ChildViewHolder {
    public TextView mCrimeDateText;
    public CheckBox mCrimeSolvedCheckBox;

    public CrimeChildViewHolder(View itemView) {
        super(itemView);

        mCrimeDateText = (TextView) itemView.findViewById(R.id.child_list_item_crime_date_text_view);
        mCrimeSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.child_list_item_crime_solved_check_box);
    }
}

package com.yesy.expandablerecycleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.ysy.blogdemo.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mCrimeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_recycler_view);

        mCrimeRecyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CrimeExpandableAdapter crimeExpandableAdapter = new CrimeExpandableAdapter(this, generateCrimes());
        crimeExpandableAdapter.onRestoreInstanceState(savedInstanceState);

        mCrimeRecyclerView.setAdapter(crimeExpandableAdapter);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((CrimeExpandableAdapter) mCrimeRecyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    private List<ParentListItem> generateCrimes() {
        CrimeLab crimeLab = CrimeLab.get(this);
        List<Crime> crimes = crimeLab.getCrimes();
        List<ParentListItem> parentListItems = new ArrayList<>();
        for (Crime crime : crimes) {
            List<CrimeChild> childItemList = new ArrayList<>();
            childItemList.add(new CrimeChild(crime.getDate(), crime.isSolved()));
            crime.setChildItemList(childItemList);
            parentListItems.add(crime);
        }
        return parentListItems;
    }
}

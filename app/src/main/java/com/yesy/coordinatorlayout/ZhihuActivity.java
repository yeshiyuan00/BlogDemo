package com.yesy.coordinatorlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ysy.blogdemo.R;

import java.util.ArrayList;
import java.util.List;

public class ZhihuActivity extends AppCompatActivity {

    RecyclerView behavior_demo_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu);
        behavior_demo_recycler = (RecyclerView) findViewById(R.id.behavior_demo_recycler);

        List<String> list = new ArrayList<String>();

        for(int i=0;i<100;i++) {
            list.add("yeshiyuan");
        }

        behavior_demo_recycler.setLayoutManager(new LinearLayoutManager(this));

        behavior_demo_recycler.setAdapter(new MyAdapter(list));

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<String> mStringList;

        public MyAdapter(List<String> list) {
            mStringList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_text, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mStringList.get(position) + position);
        }

        @Override
        public int getItemCount() {
            return mStringList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.id_tv_name);
            }
        }
    }
}

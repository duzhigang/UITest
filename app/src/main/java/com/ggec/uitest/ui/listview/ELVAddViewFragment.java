package com.ggec.uitest.ui.listview;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ggec.uitest.R;

import java.util.ArrayList;

/**
 * 在ExpandableListView中每个Group里面的最后一个Item后面增加一个其他View
 * */
public class ELVAddViewFragment extends Fragment {
    private static final String TAG = "ELVAddViewFragment";

    private ExpandableListView elv;
    private DataAdapter adapter;
    private String[] kinds = {"已分组","未分组"};
    private ArrayList<String> groupOne = new ArrayList<>();
    private ArrayList<String> groupTwo = new ArrayList<>();
    private int countOne = 0;
    private int countTwo = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupOne.add("分组G0");
        groupOne.add("分组G1");
        groupOne.add("分组G2");
        groupTwo.add("设备D0");
        groupTwo.add("设备D1");
        groupTwo.add("设备D2");
        groupTwo.add("设备D3");
        View view = inflater.inflate(R.layout.fragment_elv_add_view, container, false);
        view.findViewById(R.id.btn_delete_a).setOnClickListener(view1 -> {
            if (countOne > 0) {
                countOne--;
            }
            if (groupOne.size() > 0) {
                String str = groupOne.get(groupOne.size() - 1);
                groupOne.remove(str);
                refresh();
            } else {
                Toast.makeText(getActivity(), "GroupOne不存在数据", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_add_a).setOnClickListener(view12 -> {
            countOne++;
            groupOne.add("新增One" + countOne);
            refresh();
        });

        view.findViewById(R.id.btn_delete_b).setOnClickListener(view13 -> {
            if (countTwo > 0) {
                countTwo--;
            }
            if (groupTwo.size() > 0) {
                String str = groupTwo.get(groupTwo.size() - 1);
                groupTwo.remove(str);
                refresh();
            } else {
                Toast.makeText(getActivity(), "GroupTwo不存在数据", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_add_b).setOnClickListener(view14 -> {
            countTwo++;
            groupTwo.add("新增Two" + countTwo);
            refresh();
        });

        adapter = new DataAdapter(getActivity(), kinds, groupOne, groupTwo);
        elv = view.findViewById(R.id.elv_data);
        elv.setAdapter(adapter);
        elv.setGroupIndicator(null);
        elv.setAdapter(adapter);
        // 将子Group默认展开
        for (int i = 0; i < 2; i++) {
            elv.expandGroup(i);
        }
        elv.setOnGroupClickListener((expandableListView, view15, i, l) -> true);
        elv.setOnChildClickListener((expandableListView, view16, i, i1, l) -> {
            Log.i(TAG,"选中的Item为：groupPosition = " + i + ",childPosition = " + i1);
            return false;
        });
//        addFooterView();
//        addHeaderView();
        timerHandler1.postDelayed(runnable1, 2000);
        timerHandler2.postDelayed(runnable2, 6000);
        return view;
    }

    Handler timerHandler1 = new Handler();
    Handler timerHandler2 = new Handler();
    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            groupOne.remove(2);
            Log.i(TAG,"groupOne = " + groupOne.toString());
            refresh();
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            groupTwo.add(0, "新增的数据Two");
            Log.i(TAG,"groupTwo = " + groupTwo.toString());
            refresh();
        }
    };

    private void refresh() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void addFooterView() {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.group_item, null);
        TextView tv = ll.findViewById(R.id.tv_group_item_name);
        tv.setText("底部Item");
        elv.addFooterView(ll);
    }

    private void addHeaderView() {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.group_item, null);
        TextView tv = ll.findViewById(R.id.tv_group_item_name);
        tv.setText("顶部Item");
        elv.addHeaderView(ll);
    }
}

package com.ggec.uitest.ui.listview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ggec.uitest.R;
import com.ggec.uitest.ui.dialog.MyAlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ggec on 2018/11/2.
 * 测试ListView中长按移除某个Item的用法
 */

public class LVFragment extends Fragment {
    private static final String TAG = "LVFragment";

    private List<String> listDatas = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lv, container, false);
        String[] arrs = {"第一组数据","第二组数据","第三组数据","第四组数据","第五组数据","第六组数据"};
        Collections.addAll(listDatas, arrs);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.one_item_left, R.id.tv_one_item_left_name, listDatas);
        ListView lvData = view.findViewById(R.id.lv_data);
        lvData.setAdapter(adapter);
        lvData.setOnItemLongClickListener((parent, view1, position, id) -> {
            Log.v(TAG,"长按Item：" + position);
            showDeleteDialog(position);
            return false;
        });

        return view;
    }

    private void showDeleteDialog(final int pos) {
        FragmentManager fm = getFragmentManager();
        String content = "删除".concat(listDatas.get(pos)).concat("?");
        MyAlertDialog deleteDialog = MyAlertDialog.newInstance(null,content,null, true, null, true);
        deleteDialog.setCallback(position -> {
            switch (position) {
                case 1:
                    if (listDatas.remove(pos) != null) {
                        Log.i(TAG,"删除Item成功，pos = " + pos);
                    } else {
                        Log.w(TAG,"删除Item失败，pos = " + pos);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        });
        deleteDialog.show(fm, "delete_dialog");
    }
}

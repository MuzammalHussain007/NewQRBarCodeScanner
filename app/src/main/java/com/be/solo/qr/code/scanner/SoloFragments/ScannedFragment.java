package com.be.solo.qr.code.scanner.SoloFragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.be.solo.qr.code.scanner.Interface.CheckPosition;
import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloAdapters.ScanneAdapter;
import com.be.solo.qr.code.scanner.SoloAdapters.SoloHistoryAdapter;
import com.be.solo.qr.code.scanner.SoloModels.Scanned;
import com.be.solo.qr.code.scanner.SoloModels.SoloHistoryModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ScannedFragment extends Fragment implements CheckPosition {
    private RecyclerView recyclerView;
    public static final String SCANNED_HISTORY = "SCANNED_HISTORY";
    public static final String PREFS_SCANNED = "ScannedHistory";
    private List<Scanned> scannedList;
    private TextView textView ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanned, container, false);
        innit(view);
        getHistoryList();
        return view;

    }

    private void innit(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        scannedList = new ArrayList<>();
        textView = view.findViewById(R.id.msg_empty);
    }
    public void getHistoryList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_SCANNED, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(SCANNED_HISTORY, "null");
        Type type = new TypeToken<ArrayList<Scanned>>() {
        }.getType();
        if (type != null) {
            if (!json.equals("")) {
                scannedList = gson.fromJson(json, type);
                if (scannedList !=null){
                    if (scannedList.size() > 0) {
                        textView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                         recyclerView.setAdapter(new ScanneAdapter(scannedList,getContext(),this));
                    }
                }
            }
        }
    }

    @Override
    public void getPosition(int position) {
        if (position==0)
        {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
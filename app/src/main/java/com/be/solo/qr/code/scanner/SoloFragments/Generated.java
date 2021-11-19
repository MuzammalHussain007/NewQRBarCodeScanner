package com.be.solo.qr.code.scanner.SoloFragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.be.solo.qr.code.scanner.Interface.CheckPosition;
import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloAdapters.SoloHistoryAdapter;
import com.be.solo.qr.code.scanner.SoloModels.SoloFavoriteModel;
import com.be.solo.qr.code.scanner.SoloModels.SoloHistoryModel;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Generated extends Fragment implements CheckPosition {

    private RecyclerView recyclerView;
    private List<SoloHistoryModel> historyModelList = new ArrayList<>();
    private SoloHistoryAdapter adapter;
    public static final String HISTORY = "HISTORY";
    public static final String PREFS_HISTORY = "HISTORY";
    private TextView msg_empty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generated, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        msg_empty = view.findViewById(R.id.msg_empty);


        getHistoryList();

        return view;
    }

    public void getHistoryList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_HISTORY, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(HISTORY, "null");
        Type type = new TypeToken<ArrayList<SoloHistoryModel>>() {
        }.getType();
        if (type != null) {
            if (!json.equals("")) {
                historyModelList = gson.fromJson(json, type);
                if (historyModelList !=null){
                    if (historyModelList.size() > 0) {
                        msg_empty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new SoloHistoryAdapter(getContext(), historyModelList, adapter,this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        }
    }


    @Override
    public void getPosition(int position) {
        if(position==0)
        {
            msg_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

        }
    }
}

package com.be.solo.qr.code.scanner.SoloFragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.be.solo.qr.code.scanner.SoloActivities.SoloScanResultActivity;
import com.be.solo.qr.code.scanner.SoloAdapters.SoloFavouriteAdapter;
import com.be.solo.qr.code.scanner.SoloModels.SoloFavoriteModel;
import com.be.solo.qr.code.scanner.SoloUtils.SoloDeleteFavListener;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class SoloFavouritesFragment extends Fragment implements SoloDeleteFavListener {

    private RecyclerView recyclerView;
    private List<SoloFavoriteModel> favoriteEmailList;
    private SoloFavouriteAdapter adapter;
    public static final String PREFS_NAME = "QRCODE";
    public static final String FAVORITES = "FAVORITES";
    private TextView msg_empty;
    private Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourit_fragment, container, false);
        soloIDS(view);
        getFavList();
        return view;
    }

    private void soloIDS(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        msg_empty = view.findViewById(R.id.msg_empty);

        getActivity().setTitle("Favourites");
        setHasOptionsMenu(true);
        favoriteEmailList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).show();
    }

    private void getFavList() {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(FAVORITES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(FAVORITES, "null");
        Type type = new TypeToken<ArrayList<SoloFavoriteModel>>() {
        }.getType();
        if (json != null) {
            if (!json.equals("")) {
                favoriteEmailList = gson.fromJson(json, type);
                if (favoriteEmailList != null) {
                    if (!favoriteEmailList.isEmpty()) {
                        adapter = new SoloFavouriteAdapter(getContext(), favoriteEmailList, this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(View.VISIBLE);
                        msg_empty.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.clear_all_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.clear_all) {
//            if (favoriteEmailList.size() > 0) {
//                clearAllFavouriteList();
//            }
//        }
//        return true;
//    }
//
//    private void clearAllFavouriteList() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        builder.setMessage("Are you sure you want to clear all favourites")
//                .setTitle("Clear Favourites")
//
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        if (favoriteEmailList != null) {
//                            SoloSharedPreference sharedPref = new SoloSharedPreference();
//                            sharedPref.clearAllFavourities(requireContext());
//                            favoriteEmailList.clear();
//                            adapter.notifyDataSetChanged();
//                            recyclerView.setVisibility(View.GONE);
//                            msg_empty.setVisibility(View.VISIBLE);
//                        }
//                    }
//                })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                }).show();
//    }

    @Override
    public void deleted() {
        recyclerView.setVisibility(View.GONE);
        msg_empty.setVisibility(View.VISIBLE);
    }


}
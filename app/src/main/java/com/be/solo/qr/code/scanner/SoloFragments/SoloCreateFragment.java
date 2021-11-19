package com.be.solo.qr.code.scanner.SoloFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloAdapters.SoloCreateQRAdapter;
import com.be.solo.qr.code.scanner.SoloModels.SoloCreatingQRModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SoloCreateFragment extends Fragment {

    private RecyclerView recyclerView;
    private SoloCreateQRAdapter createQRadapter;
    private EditText search;
    private List<SoloCreatingQRModel> creatingQRList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_qr_fragment,container,false);
        soloIDS(view);
        addData();
        setAdapter();
        soloListener();
        return view;
    }

    private void soloListener() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        createQRadapter = new SoloCreateQRAdapter(creatingQRList,getContext());
        recyclerView.setAdapter(createQRadapter);
    }

    private void soloIDS(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        search = view.findViewById(R.id.search);

        getActivity().setTitle("Create QR");
    }

    private void addData() {

        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_email,"Email",R.color.email_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_uil_texts,"Message",R.color.texts_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_location, "Location", R.color.location_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_uil_phoness, "Phone", R.color.phone_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_url, "Url", R.color.location_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_wifi, "WiFi", R.color.wifi_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_chat, "Message", R.color.message_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.calenderss, "Event", R.color.event_color));
        creatingQRList.add(new SoloCreatingQRModel(R.drawable.ic_base_line_contact, "Contact", R.color.contact_color));

    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    private void filter(String text) {

        ArrayList<SoloCreatingQRModel> filteredList = new ArrayList<>();
        for (SoloCreatingQRModel item : creatingQRList) {
            if (String.valueOf(item.getItem_name()).toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        createQRadapter.filterList(filteredList);
    }
}

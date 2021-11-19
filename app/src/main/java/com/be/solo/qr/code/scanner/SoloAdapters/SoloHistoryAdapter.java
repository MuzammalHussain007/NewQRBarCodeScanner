package com.be.solo.qr.code.scanner.SoloAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.be.solo.qr.code.scanner.Interface.CheckPosition;
import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloActivities.SoloTextDetailActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloURLsDetailActivity;
import com.be.solo.qr.code.scanner.SoloModels.SoloHistoryModel;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;
import com.bumptech.glide.Glide;

import java.util.List;

public class SoloHistoryAdapter extends RecyclerView.Adapter<SoloHistoryAdapter.HistoryViewHolder> {
    public static final String HISTORY = "HISTORY";
    public static final String PREFS_HISTORY = "HISTORY";
    private List<SoloHistoryModel> history;
    private Context context;
    private List<SoloHistoryModel> historyModelList;
    public SoloHistoryAdapter historyAdapter;
    private CheckPosition checkPosition;

    public SoloHistoryAdapter(Context context, List<SoloHistoryModel> historyModelList, SoloHistoryAdapter adapter,CheckPosition checkPosition) {
        this.context = context;
        this.historyModelList = historyModelList;
        historyAdapter = adapter;
        this.checkPosition = checkPosition;
    }


    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.generated_item_history_custom_design,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, final int position) {

        holder.title.setText(historyModelList.get(position).getContent().toString());
        holder.time.setText(historyModelList.get(position).getDate());
        Glide.with(context).load(historyModelList.get(position).getIcon()).into(holder.icon);
        holder.icon.setBackgroundResource(historyModelList.get(position).getColor());
         //TODO text sai ni aaraha ??
        if (URLUtil.isValidUrl(historyModelList.get(position).getContent())){
            Log.d("ahd______", "valid");

//            holder.icon.setBackgroundResource(R.drawable.url);
        }
        else {

//            holder.icon.setBackgroundResource(R.drawable.text);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startResultDetailActivity(historyModelList.get(position).getContent(),
                        historyModelList.get(position).getDate(),position);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 removeHistory(position);
                historyModelList.remove(position);
                notifyDataSetChanged();
                if (historyModelList.size()==0)
                {
                 checkPosition.getPosition(0);
                }
            }
        });

    }

    private void removeHistory(int historyModel) {

        SoloSharedPreference sharedPref = new SoloSharedPreference();
        sharedPref.removeHistory(context,historyModel);
    }

    private void startResultDetailActivity(String content, String date,int pos) {

        if (URLUtil.isValidUrl(content)){
            Intent intent = new Intent(context, SoloURLsDetailActivity.class);
            intent.putExtra("date",date);
            intent.putExtra("content",content);
            intent.putExtra("Url", "Url");
            intent.putExtra("color", String.valueOf(historyModelList.get(pos).getColor()));
            intent.putExtra("icon", String.valueOf(historyModelList.get(pos).getIcon()));
            context.startActivity(intent);
        }
        else {
            Intent intent = new Intent(context, SoloTextDetailActivity.class);
            intent.putExtra("date",date);
            intent.putExtra("content",content);
            intent.putExtra("Text","Text");
            intent.putExtra("color", String.valueOf(historyModelList.get(pos).getColor()));
            intent.putExtra("icon", String.valueOf(historyModelList.get(pos).getIcon()));
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView title,time;
        ConstraintLayout container;
        ImageView icon,delete;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            container = itemView.findViewById(R.id.container);
            icon = itemView.findViewById(R.id.icon);
            delete = itemView.findViewById(R.id.btn_remove);
        }
    }
}

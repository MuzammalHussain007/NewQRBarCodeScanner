package com.be.solo.qr.code.scanner.SoloAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.be.solo.qr.code.scanner.Interface.CheckPosition;
import com.be.solo.qr.code.scanner.R;
import com.be.solo.qr.code.scanner.SoloActivities.SoloTextDetailActivity;
import com.be.solo.qr.code.scanner.SoloActivities.SoloURLsDetailActivity;
import com.be.solo.qr.code.scanner.SoloModels.Scanned;
import com.be.solo.qr.code.scanner.SoloUtils.SoloSharedPreference;

import java.util.List;

public class ScanneAdapter  extends RecyclerView.Adapter<ScanneAdapter.ScanneAdapterHolder>{
    private List<Scanned> scannedList;
    private Context context ;
    private CheckPosition checkPosition;

    public ScanneAdapter(List<Scanned> scannedList, Context context,CheckPosition checkPosition) {
        this.scannedList = scannedList;
        this.context = context;
        this.checkPosition = checkPosition;
    }

    @NonNull
    @Override
    public ScanneAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_history_custom_design,parent,false);
       return new ScanneAdapterHolder(view);
    }
    private void startResultDetailActivity(String content, String date) {

        if (URLUtil.isValidUrl(content)){
            Intent intent = new Intent(context, SoloURLsDetailActivity.class);
            intent.putExtra("date",date);
            intent.putExtra("content",content);
            intent.putExtra("Url","Url");
            intent.putExtra("icon",String.valueOf(R.drawable.ic_scan_qr));
            intent.putExtra("color",String.valueOf(R.color.blue_button_color));
            context.startActivity(intent);
        }
        else {
            Intent intent = new Intent(context, SoloTextDetailActivity.class);
            intent.putExtra("date",date);
            intent.putExtra("content",content);
            intent.putExtra("Text","Text");
            intent.putExtra("icon",String.valueOf(R.drawable.ic_scan_qr));
            intent.putExtra("color",String.valueOf(R.color.blue_button_color));
            context.startActivity(intent);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ScanneAdapterHolder holder, int position) {
        holder.title.setText(scannedList.get(position).getTitle());
        holder.date.setText(scannedList.get(position).getTime());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SoloSharedPreference().removeScannedHistory(context,position);
                scannedList.remove(position);
                notifyDataSetChanged();
                if (scannedList.size()==0)
                {
                    checkPosition.getPosition(0);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startResultDetailActivity(scannedList.get(position).getTitle(),scannedList.get(position).getTime());
            }
        });

    }

    @Override
    public int getItemCount() {
        return scannedList.size();
    }

    public class ScanneAdapterHolder extends RecyclerView.ViewHolder
    {
        private TextView title,date;
        private ImageView delete;

        public ScanneAdapterHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.time);
            delete=itemView.findViewById(R.id.btn_remove);
        }
    }
}

package com.fst.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fst.skytop.R;
import com.fst.modal.MyBidsVO;
import com.fst.modal.WinReportVO;

import java.util.List;

/**
 * Created by Sudhir Singh on 25,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class WinReportAdapter extends RecyclerView.Adapter<WinReportAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<WinReportVO> eventsList;

    public WinReportAdapter(Context context, List<WinReportVO> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
    }

    @Override
    public WinReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.win_report_adapter, parent, false);

        return new WinReportAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(WinReportAdapter.ViewHolder holder, int position) {

        WinReportVO myBidsVO=eventsList.get(position);

        holder.  txtWinReportEventDate.setText(myBidsVO.date);
        holder. txtWinReportEventName.setText(myBidsVO.eventTime);
        holder. txtWinReportEventNumber.setText(myBidsVO.winFirstNum+myBidsVO.winSecondNum);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
//        return 30;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtWinReportEventDate;
        TextView txtWinReportEventName ;
        TextView txtWinReportEventNumber;


        public ViewHolder(View newView) {
            super(newView);

            txtWinReportEventDate = (TextView) newView.findViewById(R.id.txtWinReportEventDate);
            txtWinReportEventName = (TextView) newView.findViewById(R.id.txtWinReportEventName);
            txtWinReportEventNumber = (TextView) newView.findViewById(R.id.txtWinReportEventNumber);

        }
    }
}

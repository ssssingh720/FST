package com.fst.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fst.skytop.R;
import com.fst.modal.MyBidsVO;

import java.util.List;

/**
 * Created by Sudhir Singh on 25,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class BidReportAdapter extends RecyclerView.Adapter<BidReportAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<MyBidsVO> eventsList;
    private boolean isWinPointVisible;

    public BidReportAdapter(Context context, List<MyBidsVO> eventsList,boolean isWinPointVisible) {
        this.context = context;
        this.eventsList = eventsList;
        this.isWinPointVisible=isWinPointVisible;
    }

    @Override
    public BidReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bid_report_adapter, parent, false);

        return new BidReportAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(BidReportAdapter.ViewHolder holder, int position) {

        MyBidsVO myBidsVO = eventsList.get(position);

//        if (myBidsVO.status.equalsIgnoreCase("0")) {
            holder.txtBidId.setTextColor(context.getResources().getColor(R.color.white));
//            Html.fromHtml("<u>Text to underline</u>");
//        } else {
//            holder.txtBidId.setTextColor(context.getResources().getColor(R.color.active_points));
//        }

        holder.txtBidDate.setText(myBidsVO.eventDtTm);
        holder.txtBidId.setText(myBidsVO.bidId);
        holder.txtBidEvents.setText(myBidsVO.eventTime);
//        holder.txtBidCategory.setText(myBidsVO.bidCat);
        holder.txtBidChoice.setText(myBidsVO.bidName+" "+myBidsVO.lucyNum);
        holder.txtBidPoints.setText(myBidsVO.pointsBid);

        if(isWinPointVisible){
            holder.txtWinPoints.setVisibility(View.VISIBLE);
            if(myBidsVO.winPoints!=null && !myBidsVO.winPoints.equalsIgnoreCase("null"))
            holder.txtWinPoints.setText(myBidsVO.winPoints);
        }else{
            holder.txtWinPoints.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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

        TextView txtBidDate;
        TextView txtBidId;
        TextView txtBidEvents;
        TextView txtWinPoints;
        TextView txtBidChoice;
        TextView txtBidPoints;
//        ImageView imgEditBid;

        public ViewHolder(View newView) {
            super(newView);

            txtBidDate = (TextView) newView.findViewById(R.id.txtBidDate);
            txtBidId = (TextView) newView.findViewById(R.id.txtBidId);
            txtBidEvents = (TextView) newView.findViewById(R.id.txtBidEvents);
            txtWinPoints = (TextView) newView.findViewById(R.id.txtWinPoints);
            txtBidChoice = (TextView) newView.findViewById(R.id.txtBidChoice);
            txtBidPoints = (TextView) newView.findViewById(R.id.txtBidPoints);
//            imgEditBid = (ImageView) newView.findViewById(R.id.imgEditBid);
        }
    }
}

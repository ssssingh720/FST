package com.fst.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fst.skytop.R;
import com.fst.modal.AccountVO;
import com.fst.modal.MyBidsVO;

import java.util.List;

/**
 * Created by Sudhir Singh on 26,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<AccountVO> eventsList;

    public AccountAdapter(Context context, List<AccountVO> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
    }

    @Override
    public AccountAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_adapter, parent, false);

        return new AccountAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AccountAdapter.ViewHolder holder, int position) {

        AccountVO myBidsVO=eventsList.get(position);

        holder.  txtAccountReportDate.setText(myBidsVO.dtTm);
        holder. txtAccountReportParticular.setText(myBidsVO.displayName);
        holder. txtActRptPP.setText(myBidsVO.w);
        holder. txtActRptRP.setText(myBidsVO.r);

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

        TextView txtAccountReportDate;
        TextView txtAccountReportParticular ;
        TextView txtActRptPP;
        TextView txtActRptRP;


        public ViewHolder(View newView) {
            super(newView);

            txtAccountReportDate = (TextView) newView.findViewById(R.id.txtAccountReportDate);
            txtAccountReportParticular = (TextView) newView.findViewById(R.id.txtAccountReportParticular);
            txtActRptPP = (TextView) newView.findViewById(R.id.txtActRptPP);
            txtActRptRP = (TextView) newView.findViewById(R.id.txtActRptRP);

        }
    }
}

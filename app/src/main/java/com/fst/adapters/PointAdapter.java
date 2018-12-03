package com.fst.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fst.skytop.R;
import com.fst.customViews.MyBounceInterpolator;
import com.fst.modal.PointsVO;

import java.util.List;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<PointsVO> pointList;

    public PointAdapter(Context context, List<PointsVO> pointList) {
        this.context = context;
        this.pointList = pointList;
    }

    @Override
    public PointAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.points_adapter, parent, false);

        return new PointAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(PointAdapter.ViewHolder holder, int position) {

        if (pointList.get(position).isPointSelected()) {
            final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            holder.txtPoints.startAnimation(myAnim);
            holder.txtPoints.setBackgroundResource(R.drawable.point_btn_choosen);
        } else {
            holder.txtPoints.setBackgroundResource(R.drawable.point_btn_unchoosen);
        }


        if (pointList.get(position).getStatus() == 1) {

            holder.txtPoints.setVisibility(View.VISIBLE);
            holder.txtPoints.setText(pointList.get(position).getPoint());
        } else {
            holder.txtPoints.setVisibility(View.GONE);
        }

        holder.txtPoints.setTag(position);
        holder.lnrPoints.setTag(position);

        holder.txtPoints.setOnClickListener(this);
        holder.lnrPoints.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txtPoints:
            case R.id.lnrPoints:

                int positionCliked = (Integer) view.getTag();

                for (int counter = 0; counter < pointList.size(); counter++) {

                    pointList.get(counter).setPointSelected(false);

                }

                pointList.get(positionCliked).setPointSelected(true);
                notifyDataSetChanged();

                break;

        }
    }

    @Override
    public int getItemCount() {
        return pointList.size();
//        return 30;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPoints;

        LinearLayout lnrPoints;

        public ViewHolder(View view) {
            super(view);

            txtPoints = (TextView) view.findViewById(R.id.txtPoints);
            lnrPoints = (LinearLayout) view.findViewById(R.id.lnrPoints);
        }
    }
}

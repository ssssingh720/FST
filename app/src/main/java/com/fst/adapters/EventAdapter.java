package com.fst.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fst.skytop.R;
import com.fst.activities.EditBidActivity;
import com.fst.customViews.MyBounceInterpolator;
import com.fst.fragment.PlayScreenFragment;
import com.fst.modal.EventsVO;

import java.util.List;


/**
 * Created by Sudhir Singh on 07,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<EventsVO> eventsList;
    private ImageView imgOverlay;
    private PlayScreenFragment playScreenFragment;
    private EditBidActivity editBidActivity;

    public EventAdapter(Context context, List<EventsVO> eventsList, ImageView imgOverlay,
                        PlayScreenFragment playScreenFragment, EditBidActivity editBidActivity) {
        this.context = context;
        this.eventsList = eventsList;
        this.imgOverlay = imgOverlay;
        this.playScreenFragment = playScreenFragment;
        this.editBidActivity = editBidActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_adapter, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


//        holder.imgJury.setTag(position);


        if (eventsList.get(position).isEventSelected()) {
            imgOverlay.setVisibility(View.GONE);
            final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
            MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
            myAnim.setInterpolator(interpolator);
            holder.lnrEvent.startAnimation(myAnim);
            holder.txtEventSelected.setBackgroundResource(R.drawable.event_btn_choosen);
        } else {
            holder.txtEventSelected.setBackgroundResource(R.drawable.event_btn_unchoosen);
        }

        holder.txtEventTime.setText(eventsList.get(position).getMyeventtime());
        holder.txtEventName.setText(eventsList.get(position).getEvent_time());

//
        holder.lnrEvent.setTag(position);
        holder.txtEventSelected.setTag(position);
        if (playScreenFragment != null) {
            holder.lnrEvent.setOnClickListener(this);
            holder.txtEventSelected.setOnClickListener(this);
        } else {
            holder.lnrEvent.setClickable(false);
            holder.txtEventSelected.setClickable(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lnrEvent:
            case R.id.txtEventSelected:

                int positionCliked = (Integer) view.getTag();

                for (int counter = 0; counter < eventsList.size(); counter++) {
                    eventsList.get(counter).setEventSelected(false);
                }

                if (playScreenFragment != null) {
                    playScreenFragment.refreshScreenData();
                }

                if (editBidActivity != null) {
                    editBidActivity.refreshScreenData();
                }

                eventsList.get(positionCliked).setEventSelected(true);
                imgOverlay.setVisibility(View.GONE);
                notifyDataSetChanged();

                break;

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

        public TextView txtEventTime;
        public TextView txtEventName;
        public TextView txtEventSelected;
        LinearLayout lnrEvent;
//        public ImageView imgJury;
//        public TextView txtJuryDesig;
//        public TextView txtJuryCompany;
//        //        public TextView txtJuryProf;
//        LinearLayout lnrSpeakerDetail;

        public ViewHolder(View view) {
            super(view);

            lnrEvent = (LinearLayout) view.findViewById(R.id.lnrEvent);
//
//            lnrSpeakerDetail = (LinearLayout) view.findViewById(R.id.lnrSpeakerDetail);
            txtEventTime = (TextView) view.findViewById(R.id.txtEventTime);
            txtEventName = (TextView) view.findViewById(R.id.txtEventName);
            txtEventSelected = (TextView) view.findViewById(R.id.txtEventSelected);

////            txtJuryProf = (TextView) view.findViewById(R.id.txtJuryProf);
//            imgJury = (ImageView) view.findViewById(R.id.imgJury);
//            txtJuryDesig = (TextView) view.findViewById(R.id.txtJuryDesig);
//            txtJuryCompany = (TextView) view.findViewById(R.id.txtJuryCompany);
        }
    }
}

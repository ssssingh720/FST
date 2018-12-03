package com.fst.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fst.Utility.Constants;
import com.fst.Utility.Utils;
import com.fst.modal.ChoiceVO;
import com.fst.skytop.R;

import java.util.List;

/**
 * Created by Sudhir Singh on 14,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ViewHolder> implements View.OnClickListener {

    private Activity context;
    private List<ChoiceVO> choiceVOList;


    public ChoiceAdapter(Activity context, List<ChoiceVO> choiceVOList) {
        this.context = context;
        this.choiceVOList = choiceVOList;

    }

    @Override
    public ChoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choice_adapter, parent, false);

        return new ChoiceAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ChoiceAdapter.ViewHolder holder, int position) {


        holder.txtPoints.setTag(position);

        if (choiceVOList.get(position).getBlock_status() == 0) {
            holder.txtPoints.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            holder.txtPoints.setBackgroundColor(context.getResources().getColor(R.color.active_points));
        }

        String bidCat = choiceVOList.get(position).getBid_category();
        if (bidCat.equalsIgnoreCase("T10") || bidCat.equalsIgnoreCase("T20") ||
                bidCat.equalsIgnoreCase("group")) {
            holder.txtCatName.setVisibility(View.VISIBLE);
            holder.txtCatName.setText(choiceVOList.get(position).getCat_name());

            try {

                if (bidCat.equalsIgnoreCase("T10")) {
                    String descp[] = choiceVOList.get(position).getDescp().split(",");
                    String firstLineNumbers = "";
                    for (int counter = 0; counter < 5; counter++) {
                        if (firstLineNumbers.length() == 0) {
                            firstLineNumbers = descp[counter];
                        } else {
                            firstLineNumbers = firstLineNumbers + "," + descp[counter];
                        }
                    }

                    firstLineNumbers = firstLineNumbers + ",";
                    String secondLineNumbers = "";
                    for (int secondCounter = 5; secondCounter < descp.length; secondCounter++) {
                        if (secondLineNumbers.length() == 0) {
                            secondLineNumbers = descp[secondCounter];
                        } else {
                            secondLineNumbers = secondLineNumbers + "," + descp[secondCounter];
                        }
                    }

                    holder.txtPoints.setText(firstLineNumbers + "\n" + secondLineNumbers);

                } else if (bidCat.equalsIgnoreCase("group")) {
                    String descp[] = choiceVOList.get(position).getDescp().split(",");
                    if (descp != null && descp.length > 6) {
                        String firstLineNumbers = "";
                        for (int counter = 0; counter < 6; counter++) {
                            if (firstLineNumbers.length() == 0) {
                                firstLineNumbers = descp[counter];
                            } else {
                                firstLineNumbers = firstLineNumbers + "," + descp[counter];

                            }
                        }

                        firstLineNumbers = firstLineNumbers + ",";
                        String secondLineNumbers = "";
                        for (int secondCounter = 6; secondCounter < descp.length; secondCounter++) {
                            if (secondLineNumbers.length() == 0) {
                                secondLineNumbers = descp[secondCounter];
                            } else {
                                secondLineNumbers = secondLineNumbers + "," + descp[secondCounter];
                            }
                        }

                        holder.txtPoints.setText(firstLineNumbers + "\n" + secondLineNumbers);

                    } else {
                        holder.txtPoints.setText(choiceVOList.get(position).getDescp());
                    }
                } else if (bidCat.equalsIgnoreCase("T20")) {

                    String descp[] = choiceVOList.get(position).getDescp().split(",");
                    String firstLineNumbers = "";
                    for (int counter = 0; counter < 7; counter++) {
                        if (firstLineNumbers.length() == 0) {
                            firstLineNumbers = descp[counter];
                        } else {
                            firstLineNumbers = firstLineNumbers + "," + descp[counter];
                        }
                    }
                    firstLineNumbers = firstLineNumbers + ",";

                    String secondLineNumbers = "";
                    for (int secondCounter = 7; secondCounter < 13; secondCounter++) {
                        if (secondLineNumbers.length() == 0) {
                            secondLineNumbers = descp[secondCounter];
                        } else {
                            secondLineNumbers = secondLineNumbers + "," + descp[secondCounter];
                        }
                    }

                    secondLineNumbers = secondLineNumbers + ",";

                    String thirdLineNumbers = "";
                    for (int thirdCounter = 13; thirdCounter < descp.length; thirdCounter++) {
                        if (thirdLineNumbers.length() == 0) {
                            thirdLineNumbers = descp[thirdCounter];
                        } else {
                            thirdLineNumbers = thirdLineNumbers + "," + descp[thirdCounter];
                        }
                    }

                    holder.txtPoints.setText(firstLineNumbers + "\n" + secondLineNumbers + "\n" + thirdLineNumbers);

                }

            }catch (Exception e){
                holder.txtPoints.setText(choiceVOList.get(position).getDescp());
            }

        } else {
            try {
                holder.txtPoints.setText(choiceVOList.get(position).getDescp());
                int number = Integer.valueOf(choiceVOList.get(position).getDescp());
                if (number % 2 == 0) {
                    holder.txtPoints.setTextColor(context.getResources().getColor(R.color.even_choice_color));
                } else {
                    holder.txtPoints.setTextColor(context.getResources().getColor(R.color.odd_choice_color));
                }

            } catch (Exception e) {

            }
            holder.txtCatName.setVisibility(View.GONE);
        }

        holder.lnrPoints.setTag(position);
        holder.lnrPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionClicked = (Integer) view.getTag();
                if (choiceVOList.get(positionClicked).getBlock_status() == 1) {
                    Intent mediaOwnerIntent = new Intent();
                    mediaOwnerIntent.putExtra(Constants.CHOICE_INTENT_DATA, choiceVOList.get(positionClicked));
                    context.setResult(Activity.RESULT_OK, mediaOwnerIntent);
                    context.finish();
                } else {
                    Utils.showAlertDialog(context, "This number is not available.");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lnrBid:
            case R.id.imgBidSelect:


                break;
        }
    }


    @Override
    public int getItemCount() {
        return choiceVOList.size();
//        return 30;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPoints;
        public TextView txtCatName;
        private LinearLayout lnrPoints;

        public ViewHolder(View view) {
            super(view);

            txtPoints = (TextView) view.findViewById(R.id.txtPoints);
            txtCatName = (TextView) view.findViewById(R.id.txtCatName);
            lnrPoints = (LinearLayout) view.findViewById(R.id.lnrPoints);
        }
    }
}

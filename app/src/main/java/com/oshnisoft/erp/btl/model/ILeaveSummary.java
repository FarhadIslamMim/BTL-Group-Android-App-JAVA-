package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * Created by monir.sobuj on 6/8/17.
 */

public class ILeaveSummary implements IItem<ILeaveSummary, ILeaveSummary.ViewHolder>, Serializable {

    @SerializedName("name")
    @Expose
    private String leaveType;
    @SerializedName("total_leave")
    @Expose
    private int totalLeave;

    @SerializedName("get_leave")
    @Expose
    private int dayCount;



    private Object tag;// defines if this item is isSelectable
    private boolean isSelectable = true;
    private boolean isEnabled = true;
    private boolean isSelected = false; // defines if the item is selected


    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public ILeaveSummary withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public ILeaveSummary withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ILeaveSummary withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ILeaveSummary withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public int getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(int totalLeave) {
        this.totalLeave = totalLeave;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @Override
    public int getType() {
        return R.id.rvSummary;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_leave_type;
    }

    @Override
    public View generateView(Context ctx) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), null, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    @Override
    public View generateView(Context ctx, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(LayoutInflater.from(ctx).inflate(getLayoutRes(), parent, false));
        bindView(viewHolder, Collections.EMPTY_LIST);
        return viewHolder.itemView;
    }

    private ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public ViewHolder getViewHolder(ViewGroup parent) {
        return getViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false));
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        Context ctx = holder.itemView.getContext();
        holder.txtType.setText(leaveType);

        //String history = "<span style=\"color:#01991f;\">" + dayCount + "</span> " + "/" +" <span style=\"color:#01991f;\">"+totalLeave+"</span> ";
        String history =  dayCount +  "/" + totalLeave;
        //holder.txtHistory.setText(Html.fromHtml(history));
        holder.txtHistory.setText(history);

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtType.setText(null);
        holder.txtHistory.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public ILeaveSummary withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        TextView txtHistory;

        public ViewHolder(View itemView) {
            super(itemView);

            txtType = itemView.findViewById(R.id.txtType);
            txtHistory = itemView.findViewById(R.id.txtHistory);


        }
    }
}

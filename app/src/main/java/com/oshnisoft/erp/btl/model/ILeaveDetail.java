package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.content.res.Resources;
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

public class ILeaveDetail implements IItem<ILeaveDetail, ILeaveDetail.ViewHolder>, Serializable {

    @SerializedName("leave_type")
    @Expose
    private String leaveType;
    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("day_count")
    @Expose
    private int dayCount;

    @SerializedName("purpose")
    @Expose
    private String reason;

    @SerializedName("status")
    @Expose
    private String status;



    private Object tag;// defines if this item is isSelectable
    private boolean isSelectable = true;
    private boolean isEnabled = true;
    private boolean isSelected = false; // defines if the item is selected


    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public ILeaveDetail withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public ILeaveDetail withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ILeaveDetail withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ILeaveDetail withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return R.id.rvDetailList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_leave;
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
        Resources res = ctx.getResources();
        String days = res.getQuantityString(R.plurals.number_days, dayCount, dayCount);
        holder.txtCount.setText(days);
        holder.txtStatus.setText(status);
        holder.txtStartDate.setText(startDate);
        holder.txtEndDate.setText(endDate);
        holder.txtReason.setText(reason);
    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtType.setText(null);
        holder.txtCount.setText(null);
        holder.txtStatus.setText(null);
        holder.txtStartDate.setText(null);
        holder.txtEndDate.setText(null);
        holder.txtReason.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public ILeaveDetail withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        TextView txtCount;
        TextView txtStatus;
        TextView txtStartDate;
        TextView txtEndDate;
        TextView txtReason;

        public ViewHolder(View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);
            txtCount = itemView.findViewById(R.id.txtCount);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
            txtReason = itemView.findViewById(R.id.txtReason);
        }
    }
}

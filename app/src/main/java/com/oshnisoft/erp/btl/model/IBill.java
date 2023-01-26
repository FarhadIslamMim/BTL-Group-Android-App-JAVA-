package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import io.realm.annotations.Ignore;


public class IBill implements IItem<IBill, IBill.ViewHolder>, Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("daily_summary")
    @Expose
    private String dailySummary;
    @SerializedName("da_amount")
    @Expose
    private String daAmount;
    @SerializedName("ta_amount")
    @Expose
    private String taAmount;
    @SerializedName("is_holiday")
    @Expose
    private String isHoliday;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @Ignore
    private Object tag;// defines if this item is isSelectable
    @Ignore
    private boolean isSelectable = true;
    @Ignore
    private boolean isEnabled = true;
    @Ignore
    private boolean isSelected = false; // defines if the item is selected

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDailySummary() {
        return dailySummary;
    }

    public void setDailySummary(String dailySummary) {
        this.dailySummary = dailySummary;
    }

    public String getDaAmount() {
        return daAmount;
    }

    public void setDaAmount(String daAmount) {
        this.daAmount = daAmount;
    }

    public String getTaAmount() {
        return taAmount;
    }

    public void setTaAmount(String taAmount) {
        this.taAmount = taAmount;
    }

    public String getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(String isHoliday) {
        this.isHoliday = isHoliday;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IBill withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IBill withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IBill withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IBill withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    @Override
    public int getType() {
        return R.id.rvBill;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_bill_history;
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


        holder.date.setText(date);
        if (isHoliday.equalsIgnoreCase("YES")) {
            holder.holiday.setChecked(true);
        } else {
            holder.holiday.setChecked(false);
        }
        holder.taBill.setText("TA : " + taAmount);
        holder.daBill.setText("DA : " + daAmount);
        holder.remarks.setText("Daily Summary:" + dailySummary);


    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.date.setText(null);
        holder.holiday.setChecked(false);
        holder.taBill.setText(null);
        holder.daBill.setText(null);
        holder.remarks.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IBill withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        CheckBox holiday;
        TextView taBill;
        TextView daBill;
        TextView remarks;

        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            holiday = itemView.findViewById(R.id.holiday);
            taBill = itemView.findViewById(R.id.taBill);
            remarks = itemView.findViewById(R.id.remarks);
            daBill = itemView.findViewById(R.id.daBill);


        }
    }
}

package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import io.realm.annotations.Ignore;


/**
 * Created by monir.sobuj on 6/8/17.
 */

public class IAttendance implements IItem<IAttendance, IAttendance.ViewHolder>, Serializable {

    private long id;
    private String attendance_date;
    private String login_time;
    private String is_late;
    private String in_address;
    private String in_note;
    private String logout_time;
    private String is_early;
    private String out_address;
    private String out_note;



    @Ignore
    private Object tag;// defines if this item is isSelectable
    @Ignore
    private boolean isSelectable = true;
    @Ignore
    private boolean isEnabled = true;
    @Ignore
    private boolean isSelected = false; // defines if the item is selected


    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public IAttendance withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public IAttendance withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public IAttendance withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public IAttendance withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAttendance_date() {
        return attendance_date;
    }

    public void setAttendance_date(String attendance_date) {
        this.attendance_date = attendance_date;
    }

    public String getLogin_time() {
        return login_time;
    }

    public void setLogin_time(String login_time) {
        this.login_time = login_time;
    }

    public String getIs_late() {
        return is_late;
    }

    public void setIs_late(String is_late) {
        this.is_late = is_late;
    }

    public String getIn_address() {
        return in_address;
    }

    public void setIn_address(String in_address) {
        this.in_address = in_address;
    }

    public String getIn_note() {
        return in_note;
    }

    public void setIn_note(String in_note) {
        this.in_note = in_note;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(String logout_time) {
        this.logout_time = logout_time;
    }

    public String getIs_early() {
        return is_early;
    }

    public void setIs_early(String is_early) {
        this.is_early = is_early;
    }

    public String getOut_address() {
        return out_address;
    }

    public void setOut_address(String out_address) {
        this.out_address = out_address;
    }

    public String getOut_note() {
        return out_note;
    }

    public void setOut_note(String out_note) {
        this.out_note = out_note;
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
        return R.id.rvList;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_attendance;
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
        String dateStr = attendance_date.split("-")[2];
        String inStr = login_time + " Address: " + in_address + " Summary: " + in_note;
        String outStr = logout_time + " Address: " + out_address + " Summary: " + out_note;
        boolean isLate = is_late.equalsIgnoreCase("Yes");
        boolean isEarly = is_early.equalsIgnoreCase("Yes");
        if(isEarly){
            holder.txtOut.setTextColor(ctx.getResources().getColor(com.mikepenz.materialize.R.color.md_red_900));
        } else {
            holder.txtOut.setTextColor(ctx.getResources().getColor(com.mikepenz.materialize.R.color.md_green_900));
        }
        if(isLate){
            holder.txtIn.setTextColor(ctx.getResources().getColor(com.mikepenz.materialize.R.color.md_red_900));
        } else {
            holder.txtIn.setTextColor(ctx.getResources().getColor(com.mikepenz.materialize.R.color.md_green_900));
        }
        holder.txtDate.setText(dateStr);
        holder.txtIn.setText(inStr);
        holder.txtOut.setText(outStr);

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtDate.setText(null);
        holder.txtIn.setText(null);
        holder.txtOut.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public IAttendance withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate;
        TextView txtIn;
        TextView txtOut;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.txtDate);
            txtIn = itemView.findViewById(R.id.txtIn);
            txtOut = itemView.findViewById(R.id.txtOut);
        }
    }
}

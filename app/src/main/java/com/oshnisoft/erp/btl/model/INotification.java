package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * Created by monir.sobuj on 6/8/17.
 */

public class INotification implements IItem<INotification, INotification.ViewHolder>, Serializable {

    private long id;
    private long seen_id;
    private String title;
    private String content;
    private String created_at;



    private Object tag;// defines if this item is isSelectable
    private boolean isSelectable = true;
    private boolean isEnabled = true;
    private boolean isSelected = false; // defines if the item is selected


    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public INotification withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public INotification withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public INotification withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public INotification withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSeen_id() {
        return seen_id;
    }

    public void setSeen_id(long seen_id) {
        this.seen_id = seen_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
        return R.layout.item_notification;
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
        holder.txtTitle.setText(title);
        holder.txtContent.setText(content);
        holder.txtNoticeDate.setText(DateTimeUtils.changeDateTimeFormat(created_at, DateTimeUtils.FORMAT_FOR_ZONE, DateTimeUtils.FORMAT_APP_DEFAULT));

    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtTitle.setText(null);
        holder.txtContent.setText(null);
        holder.txtNoticeDate.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public INotification withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtContent;
        TextView txtNoticeDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtNoticeTitle);
            txtContent = itemView.findViewById(R.id.txtNoticeContent);
            txtNoticeDate = itemView.findViewById(R.id.txtNoticeDate);


        }
    }
}

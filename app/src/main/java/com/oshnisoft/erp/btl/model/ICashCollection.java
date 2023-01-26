package com.oshnisoft.erp.btl.model;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.oshnisoft.erp.btl.ui.cashCollection.FullImageDialog;

import com.google.gson.annotations.SerializedName;
import com.mikepenz.fastadapter.IItem;
import com.oshnisoft.erp.btl.R;
import com.oshnisoft.erp.btl.utils.DateTimeUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import io.realm.annotations.Ignore;



public class ICashCollection implements IItem<ICashCollection, ICashCollection.ViewHolder>, Serializable {

    private long id;
    private long user_id;
    private long bank_id;
    private double amount;
    private String date;
    @SerializedName("image_url")
    private String image;
    private String feedback;
    private String status;
    private String note;
    private String bankName;
    private String bankAccount;
    PaymentUser user;

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
    public ICashCollection withTag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public ICashCollection withEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public ICashCollection withSetSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    @Override
    public boolean isSelectable() {
        return isSelectable;
    }

    @Override
    public ICashCollection withSelectable(boolean selectable) {
        this.isSelectable = selectable;
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getBank_id() {
        return bank_id;
    }

    public void setBank_id(long bank_id) {
        this.bank_id = bank_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public PaymentUser getUser() {
        return user;
    }

    public void setUser(PaymentUser user) {
        this.user = user;
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
        return R.layout.item_cash_collection;
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
        String statusStr = "";
        if(TextUtils.isEmpty(feedback)){
            statusStr = status;
        } else {
            statusStr += " Feedback: " + feedback;
        }
        String bank = bankName + " [" + bankAccount + "]";
        String userCode = user.getName() + "["+ user.getCode() + "]";
        holder.txtStatus.setText(statusStr);
        holder.txtUserCode.setText(userCode);
        holder.txtBankId.setText(bank);
        holder.txtAmount.setText(String.format("BDT. %.2f", amount));
        holder.txtNote.setText(note);
        holder.txtDate.setText(DateTimeUtils.changeDateTimeFormat(date, DateTimeUtils.FORMAT5, DateTimeUtils.FORMAT9));
        if(TextUtils.isEmpty(image)){
            holder.ivImage.setVisibility(View.GONE);
            holder.ivNoImage.setVisibility(View.VISIBLE);
        } else {
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.ivNoImage.setVisibility(View.GONE);
        }
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new FullImageDialog(image);
                dialogFragment.show(((AppCompatActivity) ctx).getSupportFragmentManager(), "full_image_dialog");
            }
        });


    }

    @Override
    public void unbindView(ViewHolder holder) {
        holder.txtUserCode.setText(null);
        holder.txtDate.setText(null);
        holder.txtStatus.setText(null);
        holder.txtAmount.setText(null);
        holder.txtBankId.setText(null);
        holder.txtNote.setText(null);

    }

    @Override
    public boolean equals(int code) {
        return false;
    }

    @Override
    public ICashCollection withIdentifier(long identifier) {
        return this;
    }

    @Override
    public long getIdentifier() {
        return id;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUserCode;
        TextView txtBankId;
        TextView txtDate;
        TextView txtStatus;
        TextView txtAmount;
        TextView txtNote;
        public ImageView ivImage;
        ImageView ivNoImage;

        public ViewHolder(View itemView) {
            super(itemView);

            txtUserCode = itemView.findViewById(R.id.txtUserCode);
            txtBankId = itemView.findViewById(R.id.txtBankId);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtNote = itemView.findViewById(R.id.txtNote);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivNoImage = itemView.findViewById(R.id.ivNoImage);


        }
    }
}

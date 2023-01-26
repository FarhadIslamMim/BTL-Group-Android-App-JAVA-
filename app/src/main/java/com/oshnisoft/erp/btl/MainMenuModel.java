package com.oshnisoft.erp.btl;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.oshnisoft.erp.btl.utils.SharedPrefsUtils;
import com.oshnisoft.erp.btl.utils.StringConstants;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmModel;


public class MainMenuModel extends AbstractItem<MainMenuModel, MainMenuModel.ViewHolder> implements RealmModel {

    private String menuText;
    private int menuIcon, columns = 3;
    private int[] colorSerials = new int[]{0, 1, 2, 1, 2, 0, 2, 0, 1};


    @Inject
    App context;

    public MainMenuModel(){
        App.getComponent().inject(this);
    }

    public String getMenuText() {
        return menuText;
    }

    public void setMenuText(String menuText) {
        this.menuText = menuText;
    }

    public int getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(int menuIcon) {
        this.menuIcon = menuIcon;
    }

    @Override
    public int getType() {
        return R.id.mainMenu;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_main_menu_grid;
    }

    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        Context ctx = holder.itemView.getContext();
        holder.menuText.setText(menuText);
        holder.menuI.setImageDrawable(context.getResources().getDrawable(menuIcon));
        holder.recyclerItemMainMenu.setMinimumHeight(SharedPrefsUtils.getIntegerPreference(ctx, StringConstants.PREF_MENU_H, 150));

    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

        holder.menuText.setText(null);
        holder.menuI.setImageDrawable(null);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView menuText;
        ImageView menuI;
        RelativeLayout recyclerItemMainMenu;

        public ViewHolder(View itemView){
            super(itemView);

            menuText                                    = (TextView) itemView.findViewById(R.id.menuText);
            menuI                                       = (ImageView) itemView.findViewById(R.id.menuIcon);
            recyclerItemMainMenu                        = (RelativeLayout) itemView.findViewById(R.id.recyclerItemMainMenu);
        }
    }
}

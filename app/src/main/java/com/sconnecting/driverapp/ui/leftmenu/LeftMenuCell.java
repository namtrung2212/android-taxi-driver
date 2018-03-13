package com.sconnecting.driverapp.ui.leftmenu;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by TrungDao on 8/16/16.
 */

public abstract class LeftMenuCell extends RecyclerView.ViewHolder {
    public LeftMenuCell(View itemView) {
        super(itemView);
    }


    public interface OnItemClickListener {
        void onItemClick(LeftMenuObject item);
    }

    public abstract void bind(final LeftMenuObject item, final OnItemClickListener listener);


    public abstract void updateWithModel(LeftMenuObject item);

}

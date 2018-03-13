package com.sconnecting.driverapp.ui.leftmenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sconnecting.driverapp.base.BaseActivity;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.ui.taxi.history.NotYetPaidScreen;
import com.sconnecting.driverapp.ui.taxi.history.NotYetPickupScreen;
import com.sconnecting.driverapp.ui.taxi.history.OnTheWayScreen;
import com.sconnecting.driverapp.ui.taxi.history.TravelHistoryScreen;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.sconnecting.driverapp.ui.taxi.search.LateOrderSearchScreen;
import com.sconnecting.driverapp.ui.taxi.search.RequestedLateOrdersScreen;
import com.sconnecting.driverapp.data.models.TravelOrder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class LeftMenuList extends RecyclerView.Adapter<LeftMenuCell> {

    List<LeftMenuObject> dataSource;
    RecyclerView tableView;

    LinearLayoutManager mLinearLayoutManager;



    public LeftMenuList(RecyclerView table){

        this.tableView = table;
        mLinearLayoutManager = new LinearLayoutManager(tableView.getContext());
        this.tableView.setLayoutManager(mLinearLayoutManager);


    }

    public void reloadData( ) {

        List<LeftMenuObject> data = new ArrayList<>();

        data.add( new LeftMenuObject(true,0,6,"Hành trình","",null,null));
        data.add( new LeftMenuObject(false,0,6,"Hiện tại","Home",null,0));
        data.add( new LeftMenuObject(false,0,6,"Chưa khởi hành","NotYetPickup",null,1));
        data.add( new LeftMenuObject(false,0,6,"Trong hành trình","OnTheWay",null,2));
        data.add( new LeftMenuObject(false,0,6,"Chưa thanh toán","NotYetPaid",null,3));
        data.add( new LeftMenuObject(false,0,6,"Lịch sử","History",null,4));
        data.add( new LeftMenuObject(false,0,6,"Thông báo","Notification",null,5));


        data.add( new LeftMenuObject(true,1,2,"Quét khách","",null,null));
        data.add( new LeftMenuObject(false,1,2,"Quét khách đặt trước","LateOrderSearch",null,0));
        data.add( new LeftMenuObject(false,1,2,"Chưa phản hồi","RequestedLateOrders",null,1));

        data.add( new LeftMenuObject(true,1,3,"Doanh thu","",null,null));
        data.add( new LeftMenuObject(false,1,3,"Doanh thu ngày","calendar",null,0));
        data.add( new LeftMenuObject(false,1,3,"Doanh thu tháng","calendar",null,1));
        data.add( new LeftMenuObject(false,1,3,"Doanh thu năm","calendar",null,2));

        data.add( new LeftMenuObject(true,2,2,"Cài đặt","",null,null));
        data.add( new LeftMenuObject(false,2,2,"Đổi tài","ExchangeDrivers",null,0));
        data.add( new LeftMenuObject(false,2,2,"Hỗ trợ","Support",null,1));

        reloadData(data);

    }

    public void reloadData( List<LeftMenuObject> data) {

        this.dataSource = data;
        tableView.setAdapter(this);

    }
    public void appendData( List<LeftMenuObject> data) {

        this.dataSource.addAll(data);
        tableView.setAdapter(this);

    }

    @Override
    public int getItemViewType(int position) {

        LeftMenuObject obj = this.dataSource.get(position);

        if(obj.isGroup == false){

            return 0;

        }else{

            return 1;
        }

    }



    @Override
    public LeftMenuCell onCreateViewHolder(ViewGroup viewGroup, int viewtype) {

        if(viewtype == 0){

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.base_leftmenu_cellitem, viewGroup,false);
            return new LeftMenuCellItem(view);

        }else  if(viewtype == 1){

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.base_leftmenu_cellgroup, viewGroup,false);
            return new LeftMenuCellGroup(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(LeftMenuCell cell, int i) {

        LeftMenuObject item = dataSource.get(i);
        cell.updateWithModel(item);
        cell.bind(item, new LeftMenuCell.OnItemClickListener() {
            @Override
            public void onItemClick(LeftMenuObject item) {


                if(item.section == 0){

                    if(item.index == 0){ //Home


                        SCONNECTING.orderManager.resetToLastOpenningOrder(null);

                        ((BaseActivity)tableView.getContext()).showLeftMenu(false);


                    }else if(item.index == 1){ //NotYetPickup

                        Intent intent = new Intent(tableView.getContext(), NotYetPickupScreen.class);
                        intent.putExtra("caller", tableView.getContext().getClass().getSimpleName());
                        ((Activity)tableView.getContext()).startActivity(intent);
                        ((Activity)tableView.getContext()).overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        ((BaseActivity)tableView.getContext()).showLeftMenu(false);

                    }else if(item.index == 2){ //OnTheWay

                        Intent intent = new Intent(tableView.getContext(), OnTheWayScreen.class);
                        intent.putExtra("caller", tableView.getContext().getClass().getSimpleName());
                        ((Activity)tableView.getContext()).startActivity(intent);
                        ((Activity)tableView.getContext()).overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        ((BaseActivity)tableView.getContext()).showLeftMenu(false);

                    }else if(item.index == 3){ //NotYetPaid

                        Intent intent = new Intent(tableView.getContext(), NotYetPaidScreen.class);
                        intent.putExtra("caller", tableView.getContext().getClass().getSimpleName());
                        ((Activity)tableView.getContext()).startActivity(intent);
                        ((Activity)tableView.getContext()).overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        ((BaseActivity)tableView.getContext()).showLeftMenu(false);

                    }else if(item.index == 4){ //History

                        Intent intent = new Intent(tableView.getContext(), TravelHistoryScreen.class);
                        intent.putExtra("caller", tableView.getContext().getClass().getSimpleName());
                        ((Activity)tableView.getContext()).startActivity(intent);
                        ((Activity)tableView.getContext()).overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        ((BaseActivity)tableView.getContext()).showLeftMenu(false);
                    }

                }else if(item.section == 1){

                    if(item.index == 0){ //LateOrderSearch

                        Intent intent = new Intent(tableView.getContext(), LateOrderSearchScreen.class);
                        intent.putExtra("caller", tableView.getContext().getClass().getSimpleName());
                        ((Activity)tableView.getContext()).startActivity(intent);
                        ((Activity)tableView.getContext()).overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        ((BaseActivity)tableView.getContext()).showLeftMenu(false);

                    }else if(item.index == 1){ //NotYetResponse

                        Intent intent = new Intent(tableView.getContext(), RequestedLateOrdersScreen.class);
                        intent.putExtra("caller", tableView.getContext().getClass().getSimpleName());
                        ((Activity)tableView.getContext()).startActivity(intent);
                        ((Activity)tableView.getContext()).overridePendingTransition(R.anim.pull_in_right,R.anim.push_out_left);
                        ((BaseActivity)tableView.getContext()).showLeftMenu(false);

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataSource ? dataSource.size() : 0);
    }


}

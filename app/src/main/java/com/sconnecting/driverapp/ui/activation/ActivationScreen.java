package com.sconnecting.driverapp.ui.activation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sconnecting.driverapp.AppDelegate;
import com.sconnecting.driverapp.R;
import com.sconnecting.driverapp.SCONNECTING;
import com.sconnecting.driverapp.base.listener.GetBoolValueListener;
import com.sconnecting.driverapp.base.listener.GetOneListener;
import com.sconnecting.driverapp.base.listener.GetStringValueListener;
import com.sconnecting.driverapp.data.entity.BaseModel;
import com.sconnecting.driverapp.base.AnimationHelper;
import com.sconnecting.driverapp.base.listener.Completion;
import com.sconnecting.driverapp.data.controllers.DriverController;
import com.sconnecting.driverapp.data.controllers.UserController;
import com.sconnecting.driverapp.ui.taxi.order.OrderScreen;
import com.sconnecting.driverapp.data.models.User;

import org.parceler.Parcels;

/**
 * Created by TrungDao on 8/6/16.
 */


public class ActivationScreen extends AppCompatActivity {


    TextView lblDriverName;
    EditText txtCitizenID;
    EditText txtActivationCode;
    Button btnGetCode;
    Button btnCancel;
    Button btnCommitCode;
    Button btnDone;
    TextView lblStatus;


    String currentRequestID;
    String DriverId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.other_activation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppDelegate.CurrentActivity = this;
        if(txtCitizenID.getVisibility() == View.VISIBLE) {
            txtCitizenID.selectAll();
            final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(txtCitizenID, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void setContentView(int layoutResID)
    {

        super.setContentView(layoutResID);

        findViewById(R.id.constraintLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService( getApplicationContext().INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

            }
        });


        lblDriverName =(TextView) findViewById(R.id.lblDriverName);
        txtCitizenID =(EditText) findViewById(R.id.txtCitizenID);
        txtActivationCode =(EditText) findViewById(R.id.txtActivationCode);
        lblStatus =(TextView) findViewById(R.id.lblStatus);

        btnGetCode =(Button) findViewById(R.id.btnGetCode);
        btnCancel =(Button) findViewById(R.id.btnCancel);
        btnCommitCode =(Button) findViewById(R.id.btnCommitCode);
        btnDone =(Button) findViewById(R.id.btnDone);


        lblDriverName.setVisibility(View.GONE);
        txtCitizenID.setVisibility(View.VISIBLE);
        txtActivationCode.setVisibility(View.GONE);
        btnGetCode.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);
        btnCommitCode.setVisibility(View.GONE);
        btnDone.setVisibility(View.GONE);

        btnGetCode.requestFocus();
        txtCitizenID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    txtCitizenID.selectAll();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService( getApplicationContext().INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(txtCitizenID, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        txtActivationCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    txtActivationCode.selectAll();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService( getApplicationContext().INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(txtActivationCode, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });


        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        txtCitizenID.clearFocus();
                        txtActivationCode.clearFocus();
                        lblDriverName.clearFocus();


                        String strCMND = txtCitizenID.getText().toString();

                        if(strCMND.isEmpty() == false){

                            DriverController.RequestForActivationCode(strCMND, "VN", new GetStringValueListener() {
                                @Override
                                public void onCompleted(Boolean success, String result) {

                                    if(success && result.equals("WrongCitizenID")) {
                                        lblStatus.setText("Chứng minh nhân dân không đúng, vui lòng kiểm tra lại!");
                                        return;
                                    }

                                    if(success && result != null && result.isEmpty() == false){
                                        currentRequestID = result;
                                        lblStatus.setText("Đã gửi mã kích hoạt đến số điện thoại của bạn.");

                                        txtCitizenID.setVisibility(View.GONE);
                                        btnGetCode.setVisibility(View.GONE);
                                        btnCancel.setVisibility(View.GONE);
                                        btnCommitCode.setVisibility(View.VISIBLE);
                                        txtActivationCode.setVisibility(View.VISIBLE);

                                        txtActivationCode.setText("");
                                        txtActivationCode.requestFocus();

                                    }else{

                                        lblStatus.setText("Gửi mã kích hoạt không thành công.");

                                    }

                                }
                            });

                        }else{
                            lblStatus.setText("Vui lòng nhập chứng minh nhân dân của bạn.");
                        }

                    }
                });
            }
        });


        btnCommitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        txtCitizenID.clearFocus();
                        txtActivationCode.clearFocus();
                        lblDriverName.clearFocus();


                        if(currentRequestID != null){

                            lblStatus.setText("Đang kiểm tra mã kích hoạt...");
                            btnCommitCode.setVisibility(View.GONE);


                            String strCMND = txtCitizenID.getText().toString().trim();

                            String activateCode = txtActivationCode.getText().toString().trim();


                            new DriverController().CheckForActivationCode(currentRequestID, activateCode, strCMND, new GetStringValueListener() {
                                @Override
                                public void onCompleted(Boolean success,final String userId) {

                                    if(!success || userId == null) {
                                        lblStatus.setText("Mã kích hoạt không đúng, vui lòng kiểm tra lại!");
                                        return;
                                    }

                                    SCONNECTING.driverManager.login(userId, new GetBoolValueListener() {
                                            @Override
                                            public void onCompleted(Boolean success, Boolean isLogged) {

                                                btnGetCode.setVisibility(View.GONE);
                                                btnCommitCode.setVisibility(View.GONE);
                                                txtActivationCode.setVisibility(View.GONE);

                                                if(!isLogged){

                                                    lblStatus.setText("Kết nối không hợp lệ.");
                                                    btnCancel.setVisibility(View.VISIBLE);
                                                    btnDone.setVisibility(View.GONE);
                                                    return;

                                                }

                                                btnCancel.setVisibility(View.GONE);
                                                btnDone.setVisibility(View.VISIBLE);
                                                lblDriverName.setVisibility(View.VISIBLE);

                                                lblStatus.setText("Kích hoạt thành công.");

                                                new UserController().getById(true,userId, new GetOneListener() {
                                                    @Override
                                                    public void onGetOne(Boolean success,BaseModel item) {
                                                        if(item != null){
                                                            lblDriverName.setText(((User)item).Name);
                                                            lblDriverName.requestFocus();
                                                        }
                                                    }
                                                });


                                                DriverId = userId;
                                            }
                                    });




                                }
                            });

                        }
                    }
                });

            }

        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        txtCitizenID.clearFocus();
                        txtActivationCode.clearFocus();
                        lblDriverName.clearFocus();

                        btnDone.setVisibility(View.GONE);
                        btnGetCode.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.GONE);
                        btnCommitCode.setVisibility(View.GONE);

                        txtActivationCode.setVisibility(View.GONE);
                        lblDriverName.setVisibility(View.GONE);
                        txtCitizenID.setVisibility(View.VISIBLE);
                        txtActivationCode.setText("");

                        lblStatus.setText("Vui lòng nhập số điện thoại của bạn.");

                        txtCitizenID.selectAll();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService( getApplicationContext().INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(txtCitizenID, InputMethodManager.SHOW_IMPLICIT);
                    }
                });


            }
        });
        
        
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AnimationHelper.animateButton(v, new Completion() {
                    @Override
                    public void onCompleted() {

                        txtCitizenID.clearFocus();
                        txtActivationCode.clearFocus();
                        lblDriverName.clearFocus();


                        lblStatus.setText( "Cập nhật thông tin cá nhân....");

                        btnDone.setVisibility(View.GONE);
                        btnGetCode.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.GONE);
                        btnCommitCode.setVisibility(View.GONE);
                        final InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService( getApplicationContext().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);

                        SCONNECTING.driverManager.initCurrentDriver(new GetBoolValueListener() {
                            @Override
                            public void onCompleted(Boolean success, Boolean isValidDriver) {

                                if(isValidDriver == false) {

                                    Intent intent = new Intent(getApplicationContext(), ActivationScreen.class);
                                    startActivity(intent);

                                }else{
                                    DriverController.ActivateDriverAccount(DriverId, new Completion() {
                                        @Override
                                        public void onCompleted() {


                                            SCONNECTING.Start(new Completion() {
                                                @Override
                                                public void onCompleted() {


                                                    SCONNECTING.orderManager.resetToLastOpenningOrder(null);

                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        });



                    }
                });
            }
        });
        
    }

}
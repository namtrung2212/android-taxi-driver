package com.sconnecting.driverapp.base;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.sconnecting.driverapp.base.listener.Completion;

/**
 * Created by TrungDao on 8/3/16.
 */

public class AnimationHelper {

    public static void animateButton(View view) {
        animateButton(view, new Completion() {
            @Override
            public void onCompleted() {

            }
        });
    }

    public static void animateButton(View view, final Completion listener){

        YoYo.with(new BaseViewAnimator() {
            @Override
            protected void prepare(View target) {
                getAnimatorAgent().playTogether(
                        ObjectAnimator.ofFloat(target,"alpha",0,1, 1 ,1),
                        ObjectAnimator.ofFloat(target,"scaleX",0.95f,1.03f,0.95f,1),
                        ObjectAnimator.ofFloat(target,"scaleY",0.95f,1.03f,0.95f,1)
                );
            }
        }).duration(300).withListener(new Animator.AnimatorListener(){

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if(listener != null)
                    listener.onCompleted();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(view);
    }

    public static void setOnClick(View view,final Completion listener){

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimationHelper.animateButton(v, listener);

            }
        });
    }

    public static void hideKeyBoard(View view) {
        InputMethodManager inManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

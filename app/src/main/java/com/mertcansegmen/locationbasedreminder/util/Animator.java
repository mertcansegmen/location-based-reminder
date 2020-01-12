package com.mertcansegmen.locationbasedreminder.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Animator {

    public static void animateFloatingActionButton(FloatingActionButton button) {
        ScaleAnimation disappearAnim = new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        disappearAnim.setFillBefore(true);
        disappearAnim.setFillAfter(true);
        disappearAnim.setFillEnabled(true);
        disappearAnim.setDuration(300);
        disappearAnim.setInterpolator(new OvershootInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return Math.abs(input -1f);
            }
        });
        disappearAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                ScaleAnimation appearAnim = new ScaleAnimation(0,1,0,1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                appearAnim.setFillBefore(true);
                appearAnim.setFillAfter(true);
                appearAnim.setFillEnabled(true);
                appearAnim.setDuration(300);
                appearAnim.setInterpolator(new OvershootInterpolator());
                button.startAnimation(appearAnim);
            }

            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}

        });
        button.startAnimation(disappearAnim);
    }

    public static void removeViewWithFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(1f, 0f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }

    public static void addViewWithFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0f, 1f);
        anim.setDuration(300);
        view.startAnimation(anim);
    }
}
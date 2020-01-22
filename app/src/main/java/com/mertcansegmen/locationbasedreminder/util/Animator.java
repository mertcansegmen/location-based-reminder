package com.mertcansegmen.locationbasedreminder.util;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class Animator {

    public static void fadeOut(View view) {
        YoYo.with(Techniques.FadeOut).duration(300).playOn(view);
    }

    public static void fadeIn(View view) {
        YoYo.with(Techniques.FadeIn).duration(300).playOn(view);
    }
}
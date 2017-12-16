package com.jesus.waiapp;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jesus Manuel on 21/07/2017.
 */

public class ScrollingFABBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    private int toolbarHeight;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = Utils.getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();


            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = 2*(fab.getHeight() + fabBottomMargin) ;

            double ratio = (float)dependency.getY()/(double)toolbarHeight;
            fab.setTranslationY((float) (-distanceToScroll * ratio));
        }
        return true;
    }
}
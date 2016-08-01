package com.yesy.coordinatorlayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author: yeshiyuan
 * Date: 7/13/16.
 */
public class MyBottomBarBehavior extends CoordinatorLayout.Behavior<View> {

    public MyBottomBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //确定所提供的子视图是否有另一个特定的同级视图作为布局从属。
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //这个方法是说明这个子控件是依赖AppBarLayout的
        return dependency instanceof AppBarLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.abs(dependency.getTop());//获取更随布局的顶部位置
        child.setTranslationY(translationY);
        return true;
    }
}

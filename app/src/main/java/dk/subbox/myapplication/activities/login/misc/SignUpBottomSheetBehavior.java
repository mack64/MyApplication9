package dk.subbox.myapplication.activities.login.misc;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SignUpBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

    private boolean isSlideAble = true;

    public void setSlideAble(boolean slideAble) {
        isSlideAble = slideAble;
    }

    public boolean isSlideAble() {
        return isSlideAble;
    }

    public SignUpBottomSheetBehavior(){
        super();
    }

    public SignUpBottomSheetBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {

        if (isSlideAble){
            return true;
        }else {
            return false;
        }
        //return super.onInterceptTouchEvent(parent, child, event);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent event) {

        if (isSlideAble){
            return true;
        }else {
            return false;
        }
        //return super.onTouchEvent(parent, child, event);
    }

    public static <V extends View> BottomSheetBehavior<V> from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params)
                .getBehavior();
        if (!(behavior instanceof BottomSheetBehavior)) {
            throw new IllegalArgumentException(
                    "The view is not associated with BottomSheetBehavior");
        }
        return (BottomSheetBehavior<V>) behavior;
    }

}

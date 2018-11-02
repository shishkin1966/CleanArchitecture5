package shishkin.cleanarchitecture.mvi.common;

import android.content.Context;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar.SnackbarLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class LinearLayoutBehavior extends BottomSheetBehavior<LinearLayout> {

    public LinearLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {
        if (dependency != null && child != null) {
            final float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
            child.setTranslationY(translationY);
        }
        return true;
    }
}
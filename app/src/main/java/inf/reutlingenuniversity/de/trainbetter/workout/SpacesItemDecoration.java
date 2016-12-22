package inf.reutlingenuniversity.de.trainbetter.workout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by EL on 19.12.2016.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    public SpacesItemDecoration(int space) {
        this.space = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space * 2;

        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) parent.getLayoutParams();
        marginLayoutParams.setMargins(space, space, space, 0);
        parent.setLayoutParams(marginLayoutParams);

        if (parent.getChildLayoutPosition(view) == 0 || parent.getChildLayoutPosition(view) == 1) {
            outRect.top = space;
        }
        outRect.left = space;
        outRect.right = space;

    }
}
package inf.reutlingenuniversity.de.trainbetter.workout;

import android.graphics.Rect;
import android.os.Debug;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by EL on 19.12.2016.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1) {
            outRect.top = mSpace;
        }
        if (parent.getChildAdapterPosition(view) % 2 == 0) {
            outRect.right = mSpace / 2;
            outRect.left = mSpace;
        } else {
            outRect.left = mSpace / 2;
            outRect.right = mSpace;
        }
    }
}
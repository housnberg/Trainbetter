package inf.reutlingenuniversity.de.trainbetter.workout;

/**
 * Created by EL on 11.01.2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import inf.reutlingenuniversity.de.trainbetter.model.Image;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ExerciseImageAdapter extends FragmentStatePagerAdapter {

    public static final String IMAGE_ID = "id";

    private List<Image> images;

    public ExerciseImageAdapter(FragmentManager fm, List<Image> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        ExerciseImageFragment exerciseTutorialFragment = new ExerciseImageFragment();
        Bundle imageBundle = new Bundle();
        imageBundle.putString(IMAGE_ID, images.get(position).getObjectId());
        exerciseTutorialFragment.setArguments(imageBundle);

        return exerciseTutorialFragment;
    }

    @Override
    public int getCount() {
        return images == null ? 0 : images.size();
    }
}

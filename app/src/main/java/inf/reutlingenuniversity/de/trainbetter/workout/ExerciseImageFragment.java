package inf.reutlingenuniversity.de.trainbetter.workout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import inf.reutlingenuniversity.de.trainbetter.R;
import inf.reutlingenuniversity.de.trainbetter.model.Image;

/**
 * Created by EL on 11.01.2017.
 */

public class ExerciseImageFragment extends Fragment {

    private ImageView exerciseImageView;
    private TextView exerciseImageDescriptionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_exercise_image, container, false);

        exerciseImageView = (ImageView) rootView.findViewById(R.id.exercise_image);
        exerciseImageDescriptionTextView = (TextView) rootView.findViewById(R.id.exercise_image_description);

        Image image = ParseObject.createWithoutData(Image.class, getArguments().getString(ExerciseImageAdapter.IMAGE_ID));
        try {
            image.fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        exerciseImageDescriptionTextView.setText(image.getDescription());

        ParseFile imageFile = image.getImageFile();

        if (imageFile != null) {
            imageFile.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap titleImageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        exerciseImageView.setImageBitmap(titleImageBitmap);
                    }
                }

            });
        }

        return rootView;
    }

}
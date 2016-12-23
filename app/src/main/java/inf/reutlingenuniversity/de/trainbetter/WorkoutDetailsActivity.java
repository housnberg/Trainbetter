package inf.reutlingenuniversity.de.trainbetter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.utils.Helper;

/**
 * Created by EL on 22.12.2016.
 */

public class WorkoutDetailsActivity extends LoggedInActivity {

    private Toolbar toolbar;
    private ImageView titleImageView;
    private Workout workout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_details);
        titleImageView = (ImageView) findViewById(R.id.title_image);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            workout = ParseObject.createWithoutData(Workout.class, extras.getString("id"));
            try {
                workout.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        initToolbar();

        ParseFile titleImage = workout.getTitleImage();

        if (titleImage != null) {
            titleImage.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap titleImageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        titleImageView.setImageBitmap(titleImageBitmap);
                    }
                }

            });
        }

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(workout.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}

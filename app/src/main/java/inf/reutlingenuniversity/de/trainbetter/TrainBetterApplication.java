package inf.reutlingenuniversity.de.trainbetter;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import inf.reutlingenuniversity.de.trainbetter.model.Exercise;
import inf.reutlingenuniversity.de.trainbetter.model.Image;
import inf.reutlingenuniversity.de.trainbetter.model.Workout;
import inf.reutlingenuniversity.de.trainbetter.model.WorkoutExercise;

public class TrainBetterApplication extends Application {

    private final static String PARSE_APPLICATION_ID_NAME = "PARSE_APPLICATION_ID";
    private final static String PARSE_APPLICATION_URL_NAME = "PARSE_APPLICATION_URL";

    @Override
    public void onCreate() {
        super.onCreate();

        String parseAppId = null;
        String parseAppUrl = null;

        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            parseAppId = bundle.getString(PARSE_APPLICATION_ID_NAME);
            parseAppUrl = bundle.getString(PARSE_APPLICATION_URL_NAME);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ParseObject.registerSubclass(Workout.class);
        ParseObject.registerSubclass(Exercise.class);
        ParseObject.registerSubclass(WorkoutExercise.class);
        ParseObject.registerSubclass(Image.class);

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(parseAppId)
                .server(parseAppUrl)
                .build());

        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }

}

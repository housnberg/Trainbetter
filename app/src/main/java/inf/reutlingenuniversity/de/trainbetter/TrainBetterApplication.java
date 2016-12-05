package inf.reutlingenuniversity.de.trainbetter;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseACL;

public class TrainBetterApplication extends Application {

    private final static String PARSE_APPLICATION_ID_NAME = "PARSE_APPLICATION_ID";
    private final static String PARSE_APPLICATION_URL_NAME = "PARSE_APPLICATION_URL";

    @Override
    public void onCreate() {
        super.onCreate();

        String parseAppId = null;
        String parseAppUrl = null;

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            parseAppId = bundle.getString(PARSE_APPLICATION_ID_NAME);
            parseAppUrl = bundle.getString(PARSE_APPLICATION_URL_NAME);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(parseAppId)
                .server(parseAppUrl)   // '/' important after 'parse'
                .build());

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}

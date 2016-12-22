package inf.reutlingenuniversity.de.trainbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.parse.ParseUser;

/**
 * Created by EL on 22.12.2016.
 */

public class LoggedInActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {
            ParseUser.logOutInBackground();
            logOut();
            return true;
        } else if (itemId == R.id.action_settings) {
            //Settings
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        Intent logOutIntent = new Intent(LoggedInActivity.this, StartActivity.class);
        startActivity(logOutIntent);
    }

}

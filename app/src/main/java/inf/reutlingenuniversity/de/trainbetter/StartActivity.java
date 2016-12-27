package inf.reutlingenuniversity.de.trainbetter;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import inf.reutlingenuniversity.de.trainbetter.loginout.LoginFragment;
import inf.reutlingenuniversity.de.trainbetter.registration.RegistrationFragment;

public class StartActivity extends AppCompatActivity {

    public static final String FROM = "from";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View contentWrapper;

    private Fragment loginFragment;
    private Fragment registrationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        contentWrapper = findViewById(R.id.content_wrapper);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        initToolbar();
    }



    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        loginFragment = new LoginFragment();
        registrationFragment = new RegistrationFragment();
        pagerAdapter.addFragment(loginFragment, getResources().getString(R.string.login));
        pagerAdapter.addFragment(registrationFragment, getResources().getString(R.string.register));

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_main, menu);

        //Dont show the "Logout" - Menu item if youre not logged in
        MenuItem logoutMenuItem = menu.findItem(R.id.action_logout);
        logoutMenuItem.setVisible(false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            //Settings
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void startMainActivity(int from) {
        Intent loginIntent = new Intent(StartActivity.this, WorkoutsOverviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(StartActivity.FROM, from);
        loginIntent.putExtras(bundle);
        this.startActivity(loginIntent);
    }

    public View getContentWrapper() {
        return contentWrapper;
    }

}

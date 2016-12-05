package inf.reutlingenuniversity.de.trainbetter;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.parse.ParseUser;

import inf.reutlingenuniversity.de.trainbetter.loginout.LoginFragment;
import inf.reutlingenuniversity.de.trainbetter.registration.RegistrationFragment;



public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Fragment loginFragment;
    private Fragment registrationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
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

        /*
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        switch (viewPager.getCurrentItem()) {
                            case 0:
                                workoutFragment.shareFab(null);
                                exercisesFragment.shareFab(mSharedFab);
                                mSharedFab.show();
                                break;
                            case 1:
                            default:
                                exercisesFragment.shareFab(null);
                                workoutFragment.shareFab(mSharedFab); // Share FAB to new displayed fragment
                                mSharedFab.show();
                                break;
                        }
                        //mSharedFab.show(); // Show animation
                        break;
                }
            }
        });
        */
    }


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
            return true;
        } else if (itemId == R.id.action_settings) {
            //Settings
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}

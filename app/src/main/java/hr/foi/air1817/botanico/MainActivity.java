package hr.foi.air1817.botanico;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.watering.GardenSettings;

import butterknife.ButterKnife;
import hr.foi.air1817.botanico.firebaseMessaging.BotanicoNotificationManager;
import hr.foi.air1817.botanico.fragments.InfoFragment;
import hr.foi.air1817.botanico.fragments.NotificationsFragment;
import hr.foi.air1817.botanico.fragments.PlantListFragment;


public class MainActivity extends AppCompatActivity implements android.app.FragmentManager.OnBackStackChangedListener, GardenSettings.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private android.app.FragmentManager mFm;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setToolbar();
        manageNavigationMenu();

        mDrawerToggle = setupDrawerToggle();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mFm = getFragmentManager();
        mFm.addOnBackStackChangedListener(this);

        mToolbar.setNavigationOnClickListener(navigationClick);

        BotanicoNotificationManager.getInstance(getApplicationContext()).createChannel();

        PlantListFragment plf = new PlantListFragment();
        android.app.FragmentTransaction fm = getFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container, plf);
        fm.commit();
    }

    public void openAddGardenActivity(View view) {
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(android.app.Fragment newFr){
        android.app.FragmentTransaction fm = getFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container, newFr);
        fm.commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() != 0){
            // there is something on the stack, I'm in the fragment
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            else{
                getFragmentManager().popBackStack();
            }
        } else {
            // I'm on the landing page, close the drawer or exit
            if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            else{
                super.onBackPressed();
            }
        }
    }

    public void setToolbar(){
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public void manageNavigationMenu(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView;
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()){
                            //TODO Dodat ostale fragmente iz glavnog izbornika
                            case R.id.nav_home:
                                getSupportActionBar().setTitle(R.string.nav_home);
                                PlantListFragment plf = new PlantListFragment();
                                changeFragment(plf);
                                break;
                            case R.id.nav_info:
                                getSupportActionBar().setTitle(R.string.nav_info);
                                InfoFragment infoFragment = new InfoFragment();
                                changeFragment(infoFragment);
                                break;
                            case R.id.nav_notification_settings:
                                getSupportActionBar().setTitle(R.string.nav_notification_settings);
                                NotificationsFragment notificationsFragment = new NotificationsFragment();
                                changeFragment(notificationsFragment);
                                break;
                        }

                        return true;
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public void onBackStackChanged() {
        mDrawerToggle.setDrawerIndicatorEnabled(mFm.getBackStackEntryCount() == 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(mFm.getBackStackEntryCount() > 0);
        mDrawerToggle.syncState();
    }



    View.OnClickListener navigationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getFragmentManager().getBackStackEntryCount() == 0) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            else{
                onBackPressed();
            }
        }
    };

    public void expandCollapseSettings(View view){
        View v = findViewById(R.id.notifications_layout);

        if(v.getVisibility() == View.GONE){
            v.setVisibility(View.VISIBLE);
        }
        else v.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

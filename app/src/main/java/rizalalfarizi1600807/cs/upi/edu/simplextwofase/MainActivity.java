package rizalalfarizi1600807.cs.upi.edu.simplextwofase;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Fragment.HomeFragment;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Fragment.RiwayatFragment;
import rizalalfarizi1600807.cs.upi.edu.simplextwofase.Fragment.TentangFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private RiwayatFragment riwayatFragment;
    private TentangFragment tentangFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setTitle("Simplex Calculator");
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_nav);
        mBottomNav.setItemIconTintList(null);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);



        homeFragment = new HomeFragment();
        riwayatFragment = new RiwayatFragment();
        tentangFragment = new TentangFragment();
        onNavItemClick();
    }

    public void onNavItemClick(){
        setFragment(homeFragment);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_riwayat:
                        setFragment(riwayatFragment);
                        return true;
                    case R.id.nav_tentang:
                        setFragment(tentangFragment);
                        return true;

                        default:
                            return false;
                }

            }
        });
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
}

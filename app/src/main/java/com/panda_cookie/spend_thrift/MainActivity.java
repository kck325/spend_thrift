package com.panda_cookie.spend_thrift;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.panda_cookie.spend_thrift.adapters.SpendThriftMainAdapter;
import com.panda_cookie.spend_thrift.fragments.SettingsFragment;
import com.panda_cookie.spend_thrift.fragments.StatsFragment;
import com.panda_cookie.spend_thrift.fragments.TokensFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        // Set up the ViewPager and adapter
        SpendThriftMainAdapter sectionsPagerAdapter = new SpendThriftMainAdapter(this);
        sectionsPagerAdapter.addFragment(new TokensFragment(), "Tokens");
        sectionsPagerAdapter.addFragment(new StatsFragment(), "Stats");
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(((SpendThriftMainAdapter) viewPager.getAdapter()).getPageTitle(position)));
        tabLayoutMediator.attach();

        // Set up the settings icon and listener
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .addToBackStack(null)
                        .commit();
                tabLayout.setVisibility(View.GONE);
                getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            tabLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
                return true;
            }
            return super.onOptionsItemSelected(item);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}

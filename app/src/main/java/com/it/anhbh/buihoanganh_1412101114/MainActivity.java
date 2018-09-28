package com.it.anhbh.buihoanganh_1412101114;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.it.anhbh.buihoanganh_1412101114.adapters.ViewPagerAdapter;
import com.it.anhbh.buihoanganh_1412101114.constants.Constants;
import com.it.anhbh.buihoanganh_1412101114.models.News;
import com.it.anhbh.buihoanganh_1412101114.utilities.Utility;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    NavigationView navigationView;
    TextView tvCopyright, tvISource, tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        navigationView = findViewById(R.id.nav_view);
        tvCopyright = findViewById(R.id.tv_copyright);
        tvISource = findViewById(R.id.tv_isource);
        tvVersion = findViewById(R.id.tv_version);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setTabRippleColor(null);
        tabLayout.setupWithViewPager(viewPager);

        tvCopyright.setText(Html.fromHtml("&#169; " + Calendar.getInstance().get(Calendar.YEAR) + " - AnhBH"));
        tvISource.setText(Html.fromHtml("Nguồn tin theo <strong>24h.com.vn</strong>"));
        tvVersion.setText("Phiên bản " + BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = null;

        switch (id) {
            case R.id.nav_saved:
                intent = new Intent(MainActivity.this, SavedActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_history:
                intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_review_app:
                /*Uri uri = Uri.parse("");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);*/
                break;
            case R.id.nav_send_email:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "anhbh995@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "[" + getResources().getString(R.string.app_name) + "] Góp ý cho phiên bản " + BuildConfig.VERSION_NAME + " (trên " + Utility.getDeviceInformation() + ")");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }
}

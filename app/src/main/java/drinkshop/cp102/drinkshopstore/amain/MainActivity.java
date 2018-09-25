package drinkshop.cp102.drinkshopstore.amain;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import drinkshop.cp102.drinkshopstore.R;
import drinkshop.cp102.drinkshopstore.amain.news.NewsFragment;
import drinkshop.cp102.drinkshopstore.amain.order.OrderListFragment;
import drinkshop.cp102.drinkshopstore.amain.product.ProductListFragment;
import drinkshop.cp102.drinkshopstore.halper.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    public MainActivity() {
        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.item_Product: //商品頁面
                        fragment = new ProductListFragment();
                        changeFragment(fragment);
                        setTitle(R.string.text_Product);
                        return true;
                    case R.id.item_Order: //訂單頁面
                        fragment = new OrderListFragment();
                        changeFragment(fragment);
                        setTitle(R.string.text_Order);
                        return true;
                    case R.id.item_News: //輪播牆頁面
                        fragment = new NewsFragment(); // ***OrderFragment要改成輪播牆的Fragment
                        changeFragment(fragment);
                        setTitle(R.string.text_News);
                        return true;
                    default:
                        initContent();
                        break;
                }
                return false;
            }

        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Navigation（底下的選單列） */
        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);  // 使用 BottomNavigationViewHelper 改變造型
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        initContent();

    }

    /**
     * 初始畫面
     */
    private void initContent() {
        Fragment fragment = new ProductListFragment();
        changeFragment(fragment);
        setTitle(R.string.text_Product);
    }

    /**
     * 替換 fragment
     */
    private void changeFragment(Fragment fragment)  {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    /* popups the alert dialog after the user clicks back button
    and back stack entry count is 0 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (keyCode == KeyEvent.KEYCODE_BACK && count == 0) {
            new AlertDialogFragment().show(getSupportFragmentManager(), "exit");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class AlertDialogFragment
            extends DialogFragment implements DialogInterface.OnClickListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.text_Exit)
                    .setIcon(R.drawable.ic_alert)
                    .setMessage(R.string.msg_WantToExit)
                    .setPositiveButton(R.string.text_Yes, this)
                    .setNegativeButton(R.string.text_No, this)
                    .create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                    break;
                default:
                    dialog.cancel();
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestLocationPermissions();
    }

    private static final int REQ_PERMISSIONS = 0;

    private void requestLocationPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        int result = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQ_PERMISSIONS);
        }
    }

}
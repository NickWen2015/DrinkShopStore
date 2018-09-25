package drinkshop.cp102.drinkshopstore.amain.product;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import drinkshop.cp102.drinkshopstore.Common;
import drinkshop.cp102.drinkshopstore.R;
import drinkshop.cp102.drinkshopstore.bean.Product;
import drinkshop.cp102.drinkshopstore.task.CommonTask;
import drinkshop.cp102.drinkshopstore.task.ImageTask;

public class ProductListFragment extends Fragment {
    private static final String TAG = "ProductListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvProducts;
    private CommonTask productGetAllTask, productDeleteTask;
    private ImageTask productImageTask;
    private FragmentActivity activity;

    FloatingActionButton btAdd;
    ProductsRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        handleView(view);  // 連結UI


        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        rvProducts.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ProductsRecyclerViewAdapter(activity, showAllProduct());
        rvProducts.setAdapter(adapter);



        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ProductInsertFragment();
                switchFragment(fragment);
            }
        });
        return view;
    }



    /**
     * 取得所有的商品資訊
     * */
    private List<Product> showAllProduct() {
        List<Product> products = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/ProductServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllProduct");
            String jsonOut = jsonObject.toString();
            CommonTask getAllProductTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getAllProductTask.execute().get();
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                products = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
        return products;
    }

    /**
     * 連結UI
     *
     * @param view 畫面
     * */
    private void handleView(View view) {
        btAdd = view.findViewById(R.id.btAdd);
        rvProducts = view.findViewById(R.id.rvProduct);
    }


    /**
     * adapter
     *
     *
     *
     *
     *
     * */
    private class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Product> products;
        private int imageSize;

        ProductsRecyclerViewAdapter(Context context, List<Product> products) {
            layoutInflater = LayoutInflater.from(context);
            this.products = products;
            /* 螢幕寬度除以4當作將圖的尺寸 */
            imageSize = getResources().getDisplayMetrics().widthPixels / 3;

        }

        @Override
        public int getItemCount() {
            return products.size();
        } //要幾列,spots有幾列就幾列

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_product, parent, false);
            return new MyViewHolder(itemView);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProduct;
            TextView tvCategoryName;
            TextView tvProductName;
            TextView tvMPrice;
            TextView tvLPrice;

            MyViewHolder(View itemView) {
                super(itemView);
                ivProduct = itemView.findViewById(R.id.ivProduct);
                tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvMPrice = itemView.findViewById(R.id.tvMPrice);
                tvLPrice = itemView.findViewById(R.id.tvLPrice);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Product product = products.get(position);
            String url = Common.URL + "/ProductServlet";
            final int product_id = product.getId();
            productImageTask = new ImageTask(url, product_id, imageSize, myViewHolder.ivProduct); //把imageView傳過去,直接show圖.因為主執行緒等待時間可以處理user往下滑的圖的id...,所以不用get
            productImageTask.execute();
            myViewHolder.tvCategoryName.setText("類別："+product.getCategory());
            myViewHolder.tvProductName.setText("品名："+product.getName());
            myViewHolder.tvMPrice.setText("M單價：" + product.getMPrice());
            myViewHolder.tvLPrice.setText("L單價：" + product.getLPrice());
//            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Fragment fragment = new ProductDetailFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("product", product); // product沒有圖,不會超過容量
//                    fragment.setArguments(bundle);
//                    switchFragment(fragment);
//                }
//            });
            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.update:
                                    Fragment fragment = new ProductUpdateFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("product", product);
                                    fragment.setArguments(bundle);
                                    switchFragment(fragment);
                                    break;
                                case R.id.delete: //設id為比對依據,見Spot.java裡的equals
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL + "/ProductServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "productDelete");
                                        jsonObject.addProperty("product_id", product.getId());
                                        int count = 0;
                                        try {
                                            productDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = productDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.msg_DeleteFail);
                                        } else {
                                            products.remove(product); //移除本機端資料.
                                            ProductsRecyclerViewAdapter.this.notifyDataSetChanged(); //畫面要重刷
                                            Common.showToast(activity, R.string.msg_DeleteSuccess);
                                        }
                                    } else {
                                        Common.showToast(activity, R.string.msg_NoNetwork);
                                    }
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }

    }

    private void switchFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().
                    replace(R.id.content, fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (productGetAllTask != null) {
            productGetAllTask.cancel(true);
            productGetAllTask = null;
        }

        if (productImageTask != null) {
            productImageTask.cancel(true);
            productImageTask = null;
        }

        if (productDeleteTask != null) {
            productDeleteTask.cancel(true);
            productDeleteTask = null;
        }
    }
}

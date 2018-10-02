package drinkshop.cp102.drinkshopstore.amain.order;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopstore.Common;

import drinkshop.cp102.drinkshopstore.R;
import drinkshop.cp102.drinkshopstore.task.CommonTask;

public class OrderListFragment extends Fragment {
    private static final String TAG = "OrderListFragment";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvOrders;
    private Button btScanQRCode;

    private FragmentActivity activity;
    private OrdersRecyclerViewAdapter adapter;

    private CommonTask getAllOrderTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        handleView(view);

        rvOrders.setLayoutManager(new LinearLayoutManager(activity));
        if(adapter == null) {
            adapter = new OrdersRecyclerViewAdapter(activity, showAllOrders());
        }
        rvOrders.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                adapter.notifyDataSetChanged();
                rvOrders.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        btScanQRCode.setOnClickListener(new View.OnClickListener() {  // 按下ＱＲ掃描器
            @Override
            public void onClick(View v) {
                /* 若在Activity內需要呼叫IntentIntegrator(Activity)建構式建立IntentIntegrator物件；
                 * 而在Fragment內需要呼叫IntentIntegrator.forSupportFragment(Fragment)建立物件，
                 * 掃瞄完畢時，Fragment.onActivityResult()才會被呼叫 */
                // IntentIntegrator integrator = new IntentIntegrator(this);
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(OrderListFragment.this);
                // Set to true to enable saving the barcode image and sending its path in the result Intent.
                integrator.setBarcodeImageEnabled(true);
                // Set to false to disable beep on scan.
                integrator.setBeepEnabled(false); // true抓到圖,會逼一聲
                // Use the specified camera ID.
                integrator.setCameraId(0); // 後鏡頭.前鏡頭是1
                // By default, the orientation is locked. Set to false to not lock.
                integrator.setOrientationLocked(false); // 設定無效,都是橫的
                // Set a prompt to display on the capture screen.
                integrator.setPrompt("請掃描客戶端 QRCode"); // 提示文字
                // Initiates a scan
                integrator.initiateScan();
            }
        });

        return view;
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //轉成文字
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            int orderID = Integer.valueOf(intentResult.getContents());
            String result = changeOrderStatusByOrderId(orderID, "1");

            if(result == null) {
                Toast.makeText(activity, "找不到訂單！！！", Toast.LENGTH_LONG).show();
            } else {
                adapter.notifyDataSetChanged();
                Toast.makeText(activity, "訂單修改完成！！！", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(activity, "找不到訂單！！！", Toast.LENGTH_LONG).show();
        }
    }

    private String changeOrderStatusByOrderId(int orderId, String orderStatus) {
        String result = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/OrdersServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "changeOrderStatusByOrderId");
            jsonObject.addProperty("orderId", orderId);
            jsonObject.addProperty("orderStatus", orderStatus);

            String jsonOut = jsonObject.toString();
            getAllOrderTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getAllOrderTask.execute().get();
                Type listType = new TypeToken<String>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                result = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
        return result;
    }

    /**
     * 連結UI畫面（fragment_order_list）
     * */
    private void handleView(View view) {
        rvOrders = view.findViewById(R.id.rvOrder);
        btScanQRCode = view.findViewById(R.id.btScanQRCode);
    }

    private List<Order> showAllOrders() {
        List<Order> orders = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/OrdersServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllOrder");
            String jsonOut = jsonObject.toString();
            getAllOrderTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getAllOrderTask.execute().get();
                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                orders = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
        return orders;
    }

    private class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Order> orders = new ArrayList<>();


        OrdersRecyclerViewAdapter(Context context, List<Order> orders) {
            layoutInflater = LayoutInflater.from(context);
            this.orders = orders;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvOrderId;
            TextView tvInvoice;
            TextView tvMember;
            TextView tvDate;

            MyViewHolder(View itemView) {
                super(itemView);
                tvOrderId = itemView.findViewById(R.id.tvOrderId);
                tvInvoice = itemView.findViewById(R.id.tvInvoice);
                tvMember = itemView.findViewById(R.id.tvMember);
                tvDate = itemView.findViewById(R.id.tvDate);
            }
        }

        @Override
        public int getItemCount() {
            return orders.size();
        } //要幾列,orders有幾列就幾列

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_order, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final Order order = orders.get(position);
            myViewHolder.tvOrderId.setText("訂單號碼：" + String.valueOf(order.getOrder_id()));
            myViewHolder.tvInvoice.setText("發票號碼：" + String.valueOf(order.getInvoice()));
            myViewHolder.tvMember.setText("會員名稱：" + String.valueOf(order.getMember_name()));
            myViewHolder.tvDate.setText("訂購時間：" + String.valueOf(order.getOrder_accept_time()));
//            myViewHolder.tvDate.setText(String.format(Locale.getDefault(), "%tF %<tT", order.getOrder_accept_time()));
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment fragment = new OrderDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("orderId", order.getOrder_id());
                    fragment.setArguments(bundle);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.content, fragment, "OrderDetailFragment");
                    transaction.addToBackStack("OrderDetailFragment");
                    transaction.commit();

                }
            });
        }

    }





}
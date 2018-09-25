package drinkshop.cp102.drinkshopstore.amain.order;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopstore.Common;
import drinkshop.cp102.drinkshopstore.R;
import drinkshop.cp102.drinkshopstore.task.CommonTask;


public class OrderDetailFragment extends Fragment {
    private final static String TAG = "OrderDetailFragment";
    private FragmentActivity activity;
    private FragmentManager fragmentManager;

    private TextView tvOrderNo;
    private RecyclerView rvOrderDetail;
    private TextView tvTextTotalCap;
    private TextView tvTextTotalAmount;
    private Button btCancel;

    private OrderDetailRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        fragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        handleViews(view);

        Bundle bundle = getArguments();
        if (bundle == null || bundle.getInt("orderId") == 0) {
            Common.showToast(activity, R.string.msg_NoOrdersFound);
            fragmentManager.popBackStack();
            return null;
        }

        int orderId = bundle.getInt("orderId");
        List<OrderDetail> orderDetails =  getOrderDetail(orderId);

        tvOrderNo.setText("" + orderId);

        rvOrderDetail.setLayoutManager(new LinearLayoutManager(activity));
        if(adapter == null) {
            adapter = new OrderDetailRecyclerViewAdapter(activity, orderDetails, tvTextTotalCap, tvTextTotalAmount);
        }
        rvOrderDetail.setAdapter(adapter);

//       dataBindViews();
//       btCompleted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String qrCodeText = etQRCodeText.getText().toString(); // 取user輸入的值
//                Log.d(TAG, qrCodeText);
//
//                // QR code image's length is the same as the width of the window,
//                int dimension = getResources().getDisplayMetrics().widthPixels; // qr code圖大小
//
//                // Encode with a QR Code image
//                QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrCodeText, null, // 產生qr code
//                        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), // qr code的樣式,可參考https://zxing.appspot.com/generator/
//                        dimension);
//                try {
//                    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap(); // 產生＆顯示bitmap
//                    myImage.setImageBitmap(bitmap);
//
//                } catch (WriterException e) {
//                    Log.e(TAG, e.toString());
//                }
//            }
//        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 回前一個Fragment */
                fragmentManager.popBackStack();
            }
        });

        return view;
    }

    /**
     * 連結UI
     * */
    private void handleViews(View view) {
        tvOrderNo = view.findViewById(R.id.tvOrderNo);
        rvOrderDetail = view.findViewById(R.id.rvOrderDetail);
        tvTextTotalCap = view.findViewById(R.id.tvTextTotalCap);
        tvTextTotalAmount = view.findViewById(R.id.tvTextTotalAmount);
        btCancel = view.findViewById(R.id.btCancel);
    }

    private List<OrderDetail> getOrderDetail(int orderId) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        CommonTask getOrderDetailTask;

        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/OrdersServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getOrderDetailByOrderId");
            jsonObject.addProperty("orderId", orderId);
            String jsonOut = jsonObject.toString();
            getOrderDetailTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = getOrderDetailTask.execute().get();
                Type listType = new TypeToken<List<OrderDetail>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                orderDetailList = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
        return orderDetailList;
    }




    /**
     * adapter
     *
     *
     *
     *
     *
     *
     * */
    private class OrderDetailRecyclerViewAdapter extends RecyclerView.Adapter<OrderDetailRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<OrderDetail> orderDetails;

        WeakReference<TextView> textViewWeakReferenceTextTotalCap;
        WeakReference<TextView> textViewWeakReferenceTextTotalAmount;



        public OrderDetailRecyclerViewAdapter(Context context, List<OrderDetail> orderDetail, TextView tvTextTotalCap, TextView tvTextTotalAmount) {
            layoutInflater = LayoutInflater.from(context);
            this.orderDetails = orderDetail;
            textViewWeakReferenceTextTotalCap = new WeakReference<>(tvTextTotalCap);
            textViewWeakReferenceTextTotalAmount = new WeakReference<>(tvTextTotalAmount);
        }

        @Override
        public int getItemCount() {
            return orderDetails.size();
        } //要幾列,orderDetail有幾列就幾列

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_order_detail, parent, false);
            return new MyViewHolder(itemView);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName;
            TextView tvIceName;
            TextView tvSugarName;
            TextView tvSizeName;
            TextView tvOrderProductQuantity;
            TextView tvOrderProductPrice;

            MyViewHolder(View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvIceName = itemView.findViewById(R.id.tvIceName);
                tvSugarName = itemView.findViewById(R.id.tvSugarName);
                tvSizeName = itemView.findViewById(R.id.tvMPrice);
                tvOrderProductQuantity = itemView.findViewById(R.id.tvOrderProductQuantity);
                tvOrderProductPrice = itemView.findViewById(R.id.tvOrderProductPrice);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final OrderDetail orderDetail = orderDetails.get(position);

            setTotalCapAndTotalAmount();

            String productName = orderDetail.getProduct_name();
            String iceName = orderDetail.getIce_name();
            String sugarName = orderDetail.getSugar_name();
            String sizeName = orderDetail.getSize_name();
            int productQuantity = orderDetail.getProduct_quantity();
            int productPrice = orderDetail.getProduct_price();

            myViewHolder.tvProductName.setText("品名：" + productName);
            myViewHolder.tvIceName.setText("冰量：" + iceName);
            myViewHolder.tvSugarName.setText("甜度：" + sugarName);
            myViewHolder.tvSizeName.setText("尺寸：" + sizeName);
            myViewHolder.tvOrderProductQuantity.setText("數量：" + productQuantity);
            myViewHolder.tvOrderProductPrice.setText("小計：" + productQuantity * productPrice);
        }

        /**
         * 設定總杯數及總金額
         * */
        private void setTotalCapAndTotalAmount() {
            int TotalCap = 0;
            int TotalAmount = 0;


            for(OrderDetail orderDetail : orderDetails) {
                int productQuantity = orderDetail.getProduct_quantity();
                int productPrice = orderDetail.getProduct_price();

                TotalCap += productQuantity;
                TotalAmount += (productQuantity * productPrice);
            }

            textViewWeakReferenceTextTotalCap.get().setText("" + TotalCap);
            textViewWeakReferenceTextTotalAmount.get().setText("" + TotalAmount);
        }

    }

    private boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}

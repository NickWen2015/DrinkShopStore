package drinkshop.cp102.drinkshopstore.amain.news;


import android.app.Activity;
import android.os.Bundle;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import drinkshop.cp102.drinkshopstore.Common;
import drinkshop.cp102.drinkshopstore.R;
import drinkshop.cp102.drinkshopstore.bean.News;
import drinkshop.cp102.drinkshopstore.task.CommonTask;
import drinkshop.cp102.drinkshopstore.task.ImageTask;


public class NewsFragment extends Fragment {

    private final static String TAG = "NewsFragment";

    private RecyclerView rvNews;
    private CommonTask newsGetAllTask, newsDeleteTask;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageTask newsImageTask;
    private FragmentActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.news_list, container, false);
        activity = getActivity();
        swipeRefreshLayout(view);
        addFloatingActionButton(view);
        activityRecyclerView(view);


        return view;
    }

    /**
     * 新增活動按鈕
     */
    private void addFloatingActionButton(View view) {
        FloatingActionButton btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new NewsInsertFragment();
                changeFragment(fragment);
            }
        });
        Log.e(TAG, "addFloatingActionButton：成功");
    }

    /**
     * 下拉更新
     */
    private void swipeRefreshLayout(View view) {
        swipeRefreshLayout =
                view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);  //開始動畫（轉圈）
                swipeRefreshLayout.setRefreshing(false); //結束動畫
            }
        });
        Log.e(TAG, "swipeRefreshLayout：成功");
    }

    private void activityRecyclerView(View view) {
        rvNews = view.findViewById(R.id.rvNews);
        rvNews.setLayoutManager(new LinearLayoutManager(activity));
        rvNews.setAdapter(new MyNewsRecyclerViewAdapter(activity)); //activity＝context
        Log.e(TAG, "activityRecyclerView：成功");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private class MyNewsRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<News> myNews;
        private int imageSize;

        MyNewsRecyclerViewAdapter(Activity context) {
            this.myNews = getAllNews();
            this.layoutInflater = LayoutInflater.from(context);
            /* 螢幕寬度除以4當作將圖的尺寸 */
            this.imageSize = getResources().getDisplayMetrics().widthPixels / 4; //client決定圖的大小。按比例的模式設定,不受螢幕大小或解析度限制
        }

        private List<News> getAllNews() {
            List<News> myNews = new ArrayList<>();
            if (Common.networkConnected(activity)) {
                String url = Common.URL + "/NewsServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getAllNews"); //向server取得所有
                String jsonOut = jsonObject.toString();
                newsGetAllTask = new CommonTask(url, jsonOut); //設檢查機制
                try {
                    String jsonIn = newsGetAllTask.execute().get(); //這邊用get是因為只是純文字,主執行緒不會等太久
                    Type listType = new TypeToken<List<News>>() {  //泛型用TypeToken
                    }.getType();
                    myNews = new Gson().fromJson(jsonIn, listType);
                } catch (Exception e) {
                    Log.e(TAG, "GetAllNews : " + e.toString());
                }
                if (myNews == null || myNews.isEmpty()) {
                    Common.showToast(activity, R.string.msg_NoNewsFound);
                }
            } else {
                Common.showToast(activity, R.string.msg_NoNetwork);
            }
            return myNews;
        }

        @Override
        public int getItemCount() {
            return myNews.size();
        }  //得知純文字檔的筆數

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.activity_news, parent, false);
            return new MyViewHolder(itemView);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvActivityName, tvActivityDateStart, tvActivityDateEnd;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivNewsPage);
                tvActivityName = itemView.findViewById(R.id.tvActivityName);
                tvActivityDateStart = itemView.findViewById(R.id.tvActivityDateStart);
                tvActivityDateEnd = itemView.findViewById(R.id.tvActivityDateEnd);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final News news = myNews.get(position);   //取得文字
            String url = Common.URL + "/NewsServlet";
            int id = news.getActivity_id();
            newsImageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView); //向server端取圖片,ImageTask邊取邊Show。這邊沒有用get(),主執行緒就不會在這邊等待。
            newsImageTask.execute();   //先show文字,圖先用小綠人圖,之後再替換
            myViewHolder.tvActivityName.setText(news.getActivity_name());
            myViewHolder.tvActivityDateStart.setText(news.getActivity_date_start());
            myViewHolder.tvActivityDateEnd.setText(news.getActivity_date_end());

            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {  //長按跳出popup menu
                    PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.update:  //更新活動
                                    Fragment fragment = new NewsUpdateFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("news", news);
                                    fragment.setArguments(bundle);
                                    changeFragment(fragment);
                                    break;
                                case R.id.delete: //刪除活動
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL + "/NewsServlet";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "newsDelete");
                                        jsonObject.addProperty("news", new Gson().toJson(news));
                                        int count = 0;
                                        try {
                                            newsDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = newsDeleteTask.execute().get();
                                            count = Integer.valueOf(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.msg_DeleteFail);
                                        } else {
                                            myNews.remove(news);
                                            MyNewsRecyclerViewAdapter.this.notifyDataSetChanged();
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

    private void changeFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().
                    replace(R.id.content, fragment).addToBackStack(null).commit(); //切換到Detail頁面,user可以按back鍵回去上一頁

        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (newsGetAllTask != null) {
            newsGetAllTask.cancel(true);  //解除參照,但實體還在記憶體
            newsGetAllTask = null;
        }

        if (newsImageTask != null) {
            newsImageTask.cancel(true);
            newsGetAllTask = null;
        }

        if (newsDeleteTask != null) {
            newsDeleteTask.cancel(true);
            newsGetAllTask = null;
        }
    }
}



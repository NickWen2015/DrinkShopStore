package drinkshop.cp102.drinkshopstore.amain.product;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import idv.ron.store.R;
import idv.ron.store.main.Common;
import idv.ron.store.main.task.CommonTask;

public class ProductInsertCategoryFragment extends Fragment {
    private final static String TAG = "ProductInsertCategoryFragment";
    private FragmentActivity activity;
    private FragmentManager fragmentManager;

    private Button btFinishInsert, btCancel;
    private EditText etCategoryName;


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
        View rootView = inflater.inflate(R.layout.fragment_category_insert, container, false);
        findViews(rootView);

        btFinishInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category_name = etCategoryName.getText().toString().trim();
                if (category_name.length() <= 0) {
                    Common.showToast(getActivity(), R.string.msg_Category_nameIsInvalid);
                    return;
                }

                if (Common.networkConnected(activity)) {
                    String url = Common.URL + "/ProductServlet";
                    Category category = new Category(0, category_name);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "productInsert"); // 告訴server要做什麼
                    jsonObject.addProperty("category", new Gson().toJson(category));

                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.valueOf(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(getActivity(), R.string.msg_InsertFail);
                    } else {
                        Common.showToast(getActivity(), R.string.msg_InsertSuccess);
                    }
                } else {
                    Common.showToast(getActivity(), R.string.msg_NoNetwork);
                }
                /* 回前一個Fragment */
                fragmentManager.popBackStack();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 回前一個Fragment */
                fragmentManager.popBackStack();
            }
        });

        return rootView;
    }

//    private void switchFragment(Fragment fragment) {
//        if (getFragmentManager() != null) {
//            getFragmentManager().beginTransaction().
//                    replace(R.id.content, fragment).addToBackStack(null).commit();
//        }
//    }


    private void findViews(View rootView) {

        btFinishInsert = rootView.findViewById(R.id.btFinishInsert);
        btCancel = rootView.findViewById(R.id.btCancel);
        etCategoryName = rootView.findViewById(R.id.etCategoryName);

    }

    private boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


}


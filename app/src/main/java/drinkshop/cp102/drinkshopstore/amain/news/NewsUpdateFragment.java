package drinkshop.cp102.drinkshopstore.amain.news;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import drinkshop.cp102.drinkshopstore.Common;
import drinkshop.cp102.drinkshopstore.R;
import drinkshop.cp102.drinkshopstore.bean.News;
import drinkshop.cp102.drinkshopstore.task.CommonTask;
import drinkshop.cp102.drinkshopstore.task.ImageTask;

import static android.app.Activity.RESULT_OK;

public class NewsUpdateFragment extends Fragment {
    private final static String TAG = "NewsUpdateFragment";
    private FragmentActivity activity;
    private FragmentManager fragmentManager;
    private ImageView ivNews;
    private Button  btPickPicture, btFinishUpdate, btCancel;
    private TextView tvId;
    private EditText etActivityName, etActivityDateStart, etActivityDateEnd;
    private News news;
    private byte[] image;
    private static final int REQ_PICK_IMAGE = 0;
    private static final int REQ_CROP_PICTURE = 1;
    private Uri croppedImageUri;

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
        View rootView = inflater.inflate(R.layout.news_update, container, false);
        handleViews(rootView);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("news") == null) {
            Common.showToast(activity, R.string.msg_NoNewsFound);
            fragmentManager.popBackStack();
            return null;
        }
        news = (News) bundle.getSerializable("news");
     dataBindViews();

        btPickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_IMAGE);
            }
        });

        btFinishUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activityName = etActivityName.getText().toString();
                if (activityName.length() <= 0) {
                    Common.showToast(activity, R.string.msg_NameIsInvalid);
                    return;
                }
                String activityDateStart = etActivityDateStart.getText().toString();
                String activityDateEnd = etActivityDateEnd.getText().toString();

                if (image == null) {
                    Common.showToast(activity, R.string.msg_NoImage);
                    return;
                }

                if (Common.networkConnected(activity)) {
                    String url = Common.URL + "/NewsServlet";
                News newNews = new News(news.getActivity_id(), activityName, activityDateStart,activityDateEnd); //比insert多了一個id
                String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "newsUpdate");
                jsonObject.addProperty("news", new Gson().toJson(newNews));//把物件變成JSON字串
                jsonObject.addProperty("imageBase64", imageBase64);
                int count = 0;
                try {
                    String result = new CommonTask(url, jsonObject.toString()).execute().get();
                    count = Integer.valueOf(result);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (count == 0) {
                    Common.showToast(activity, R.string.msg_UpdateFail);
                } else {
                    Common.showToast(activity, R.string.msg_UpdateSuccess);
                }
            } else {
                Common.showToast(activity, R.string.msg_NoNetwork);
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
    private void handleViews(View rootView) {
        ivNews = rootView.findViewById(R.id.ivNewsPage);
        btPickPicture = rootView.findViewById(R.id.btPickPicture);
        btFinishUpdate = rootView.findViewById(R.id.btFinishInsert);
        tvId = rootView.findViewById(R.id.tvId);
        btCancel = rootView.findViewById(R.id.btCancel);
        etActivityName = rootView.findViewById(R.id.etActivityName);
        etActivityDateStart = rootView.findViewById(R.id.etActivityDateStart);
        etActivityDateEnd = rootView.findViewById(R.id.etActivityEnd);
    }

    private void dataBindViews() {
        String url = Common.URL + "/NewsServlet";
        int id = news.getActivity_id();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        Bitmap bitmap = null;
        try {
            bitmap = new ImageTask(url, id, imageSize).execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            ivNews.setImageBitmap(bitmap);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
        } else {
            ivNews.setImageResource(R.drawable.default_image);
        }
        ivNews.setImageBitmap(bitmap);
        tvId.setText(String.valueOf(id));
        etActivityName.setText(news.getActivity_name());
        etActivityDateStart.setText(news.getActivity_date_start());
        etActivityDateEnd.setText(news.getActivity_date_end());
    }
    private boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_PICK_IMAGE:
                    Uri uri = intent.getData();
                    crop(uri);
                    break;
                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE: " + croppedImageUri.toString());
                    try {
                        Bitmap picture = BitmapFactory.decodeStream(
                                activity.getContentResolver().openInputStream(croppedImageUri));
                        ivNews.setImageBitmap(picture);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        image = out.toByteArray();
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        croppedImageUri = Uri.fromFile(file);
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // the recipient of this Intent can read soruceImageUri's data
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // set image source Uri and type
            cropIntent.setDataAndType(sourceImageUri, "image/*");
            // send crop message
            cropIntent.putExtra("crop", "true");
            // aspect ratio of the cropped area, 0 means user define
            cropIntent.putExtra("aspectX", 0); // this sets the max width
            cropIntent.putExtra("aspectY", 0); // this sets the max height
            // output with and height, 0 keeps original size
            cropIntent.putExtra("outputX", 0);
            cropIntent.putExtra("outputY", 0);
            // whether keep original aspect ratio
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, croppedImageUri);
            // whether return data by the intent
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQ_CROP_PICTURE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Common.showToast(activity, "This device doesn't support the crop action!");
        }
    }
}


package drinkshop.cp102.drinkshopstore.amain.product;

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
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

import drinkshop.cp102.drinkshopstore.Common;
import drinkshop.cp102.drinkshopstore.R;
import drinkshop.cp102.drinkshopstore.task.CommonTask;

import static android.app.Activity.RESULT_OK;

public class ProductInsertFragment extends Fragment {
    private final static String TAG = "ProductInsertFragment";
    private FragmentActivity activity;
    private FragmentManager fragmentManager;
    private ImageView ivProduct;
    private Button btTakePicture, btPickPicture, btFinishInsert, btCancel, btAddCategory;
    private EditText etProductName, etSizeName, etProductPrice;
    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_IMAGE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private Uri contentUri, croppedImageUri;
    private Spinner spCategory;
    private CommonTask categoryGetAllTask;

    public ProductInsertFragment() { }

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
        View rootView = inflater.inflate(R.layout.fragment_product_insert, container, false);
        findViews(rootView);
        btTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                file = new File(file, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                if (isIntentAvailable(activity, intent)) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Common.showToast(getActivity(), R.string.text_NoCameraApp);
                }
            }
        });

        btPickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_PICK_IMAGE);
            }
        });

        btFinishInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object item = spCategory.getSelectedItem();
                String category_name = item.toString().trim();
//                String category_name = etCategoryName.getText().toString().trim();
                if (category_name.length() <= 0) {
                    Common.showToast(getActivity(), R.string.msg_Category_nameIsInvalid);
                    return;
                }
                String product_name = etProductName.getText().toString().trim();
                String size_name = etSizeName.getText().toString().trim();
                String product_price = etProductPrice.getText().toString().trim();

                if (image == null) {
                    Common.showToast(getActivity(), R.string.msg_NoImage);
                    return;
                }

                if (Common.networkConnected(activity)) {
                    String url = Common.URL + "/ProductServlet";
                    Product product = new Product(0, category_name, product_name, size_name, product_price);
                    String imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "productInsert"); // 告訴server要做什麼
                    jsonObject.addProperty("product", new Gson().toJson(product));
                    jsonObject.addProperty("imageBase64", imageBase64);
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

        btAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProductInsertCategoryFragment();
                switchFragment(fragment);
            }
        });

        return rootView;
    }

    private void switchFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().
                    replace(R.id.content, fragment).addToBackStack(null).commit();
        }
    }

    private void showAllCategories() {
        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/ProductServlet";
            List<Category> categories = null;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllCategory");
            String jsonOut = jsonObject.toString();
            categoryGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = categoryGetAllTask.execute().get();
                Type listType = new TypeToken<List<Category>>() {
                }.getType();
                categories = new Gson().fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (categories == null || categories.isEmpty()) {
                Common.showToast(activity, R.string.msg_NoCategoriesFound);
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCategory.setAdapter(adapter);
            }

        } else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllCategories();
    }

    private void findViews(View rootView) {
        ivProduct = rootView.findViewById(R.id.ivProduct);
        btTakePicture = rootView.findViewById(R.id.btTakePicture);
        btPickPicture = rootView.findViewById(R.id.btPickPicture);
        btFinishInsert = rootView.findViewById(R.id.btFinishInsert);
        btCancel = rootView.findViewById(R.id.btCancel);
        etProductName = rootView.findViewById(R.id.etProductName);
        etSizeName = rootView.findViewById(R.id.etSizeName);
        etProductPrice = rootView.findViewById(R.id.etProductPrice);
        spCategory = rootView.findViewById(R.id.spCategory);
        btAddCategory = rootView.findViewById(R.id.btAddCategory);
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
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_IMAGE:
                    Uri uri = intent.getData();
                    crop(uri);
                    break;
                case REQ_CROP_PICTURE:
                    Log.d(TAG, "REQ_CROP_PICTURE: " + croppedImageUri.toString());
                    try {
                        Bitmap picture = BitmapFactory.decodeStream(
                                activity.getContentResolver().openInputStream(croppedImageUri));
                        ivProduct.setImageBitmap(picture);
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
            Common.showToast(getActivity(), "This device doesn't support the crop action!");
        }
    }

//}
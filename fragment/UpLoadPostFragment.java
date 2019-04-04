package com.aktivo.fragment;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Common_Methods;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.Methods;
import com.aktivo.response.BaseCommanRespons;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApi;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.Activity.MIActivity;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.freesoulapps.preview.android.Preview;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpLoadPostFragment extends BaseFragment implements View.OnClickListener, Preview.PreviewListener {

    @BindView(R.id.sv_post_image)
    SimpleDraweeView sv_post_image;
    @BindView(R.id.sv_user_image)
    SimpleDraweeView sv_user_image;
    @BindView(R.id.edt_post_tag)
    EditText edt_post_tag;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.upload_post_title)
    TextView upload_post_title;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.tv_edit)
    TextView tv_edit;
    @BindView(R.id.play_icon)
    ImageView play_icon;
    @BindView(R.id.cardViewPreview)
    CardView cardViewPreview;
    @BindView(R.id.preview)
    Preview mPreview;
    @BindView(R.id.iv_camera)
    ImageView iv_camera;
    @BindView(R.id.video_view)
    VideoView video_view;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.tv_click)
    TextView tv_click;
    @BindView(R.id.deletemedia)
    ImageView deletemedia;

    int count_last=0;
    UserDetailTable userDetailTable;
    String user_id = "";
    boolean is_preview_done = false;
    String path_image_and_video = "", socialid = "", mediatype = "";
    String post_type = "", getThumbImage;
    String path_upload_serever = "";
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String STATUS_KEY = "status";
    public static final String POST_KEY = "postkey";
    public static final String SOCIAL_ID = "socialid";
    public static final String THUMB_IMAGE = "thumbimage";
    public static final String SOCIALMEDIA_ID = "social_media_id";
    public static final String MEDIA_TYPE = "media_type";
    boolean is_statu_comming_from = false;
    boolean is_link_preview = false;
    String link_str = "", posttype = "", caption = "", getSocialmediaId = "";
    public static final int PHOTO_TAKE_CAMERA = 5;
    public static final int PHOTO_GALLARY_OPEN = 6;
    String mCurrentPhotoPath = "";
    Uri selectedImage;

    public static UpLoadPostFragment getInstance(String path, String type, String text_status, String postkey, String socialid, String thumbimage, String socialmedia_id, String mediatype) {
        UpLoadPostFragment upLoadPostFragment = new UpLoadPostFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        bundle.putString(TYPE, type);
        bundle.putString(STATUS_KEY, text_status);
        bundle.putString(POST_KEY, postkey);
        bundle.putString(SOCIAL_ID, socialid);
        bundle.putString(THUMB_IMAGE, thumbimage);
        bundle.putString(SOCIALMEDIA_ID, socialmedia_id);
        bundle.putString(MEDIA_TYPE, mediatype);
        upLoadPostFragment.setArguments(bundle);
        return upLoadPostFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.up_load_post_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHeader();
        setFont();
        getBundleDataValue();
        initComponet();
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
                mActivity.tagmanager("Camera button","social_camera_click");
            }
        });
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (Common_Methods.isPermissionNotGranted(mActivity, permissions)) {
                requestPermissions(permissions, CommonKeys.PERMISSION_CODE);
                return;
            } else {
                openPhotoDilogGallery();
            }
        } else {
            openPhotoDilogGallery();
        }
    }

    private void openPhotoDilogGallery() {

       /* final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("**//*");
       // startActivityForResult(galleryIntent, PHOTO_GALLARY_OPEN);*/
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("*/*");
        startActivityForResult(photoPickerIntent, PHOTO_GALLARY_OPEN);
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_choose_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setTitle(R.string.choose_option);
        // dialog.show();

        TextView camera_btn = (TextView) dialog.findViewById(R.id.camera);
        TextView galary = (TextView) dialog.findViewById(R.id.galery);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
      /*  camera_btn.setText(""+languageTable.getFROM_CAMERA());
        galary.setText(""+languageTable.getFROM_GALLERY());
        tv_header.setText(""+languageTable.getSELECT_OPTION());*/
//        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

        camera_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

/*
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            *//* create instance of File with name img.jpg *//*
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                            *//* put uri as extra in intent object *//*
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                startActivityForResult(intent,PHOTO_TAKE_CAMERA);*/
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.setType("video/*, image/*");
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("test", "==>" + e.getLocalizedMessage());
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = null;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            // only for gingerbread and newer versions
                            try {
                                takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(mActivity, "com.dalbo_driver.fileprovider", createImageFile());

                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

                                startActivityForResult(takePictureIntent, PHOTO_TAKE_CAMERA);
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("test", "eerroor=>>" + e.getLocalizedMessage());
                            }
                        } else {
                               /* try {
                                    photoURI = Uri.fromFile(createImageFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }*/

                            try {
                                dispatchTakePictureIntent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                              /*  Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            //create instance of File with name img.jpg *//**//*
                                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                            *//* put uri as extra in intent object *//*
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                                intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                                startActivityForResult(intent,PHOTO_TAKE_CAMERA);*/
                        }
                    }
                }
                dialog.dismiss();
            }

        });

        galary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                try {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PHOTO_GALLARY_OPEN);
                   /* Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , PHOTO_GALLARY_OPEN);*/

                    //startActivityForResult(new avtImageChooser().create(), 500);
                } catch (Exception e) {
                }
                dialog.dismiss();
            }

        });
    }

    private File createImageFile() throws IOException {
        mCurrentPhotoPath = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        File direct = new File(Environment.getExternalStorageDirectory() + "/Activo");
        // File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!direct.exists())
            direct.mkdirs();
        File image = new File(direct, "IMG_" + timeStamp + ".JPG");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        } else {

            mCurrentPhotoPath = image.getAbsolutePath();
        }
        Log.e("test", "===>" + mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(createImageFile());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PHOTO_TAKE_CAMERA);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intetnData) {
        super.onActivityResult(requestCode, resultCode, intetnData);
        // retrive selected photo
        switch (requestCode) {

            case PHOTO_TAKE_CAMERA:
                if (resultCode == RESULT_OK) {


                }
                break;
            case PHOTO_GALLARY_OPEN:
                if (resultCode == RESULT_OK) {
                    sv_post_image.setVisibility(View.VISIBLE);
                   /* if (intetnData != null) {
                        Uri selectedImage = intetnData.getData();
                        if (selectedImage != null) {
                            String mPath = getRealPathFromURI(selectedImage);
                            Log.e("test","image path=>>"+mPath);
                         mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance(mPath),mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        }
                    }*/
                    selectedImage = intetnData.getData();
                    String path = String.valueOf(selectedImage);

                    Uri uri = intetnData.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path1 = myFile.getAbsolutePath();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }
                    Log.e("test", "file name=>" + displayName);

                    if (Validation.isRequiredField(displayName)) {
                        String extension = displayName.substring(displayName.lastIndexOf("."));
                        boolean is_extenction = false;
                        String type = "1";
                        if (Validation.isRequiredField(extension)) {
                            Log.e("test", "extension name=>" + extension);

                            if (extension.equalsIgnoreCase(".jpg")) {
                                type = "1";
                                // MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_IMAGE);
                                is_extenction = true;
                            } else if (extension.equalsIgnoreCase(".png")) {
                                type = "1";
                                // MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_IMAGE);
                                is_extenction = true;

                            } else if (extension.equalsIgnoreCase(".JPEG")) {
                                type = "1";
                                is_extenction = true;
                                // MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_IMAGE);

                            } else if (extension.equalsIgnoreCase(".gif")) {
                                type = "1";
                                is_extenction = true;
                                // MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_IMAGE);

                            } else if (extension.equalsIgnoreCase(".3gp")) {
                                type = "2";
                                is_extenction = true;
                                // MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_VIDEO);

                            } else if (extension.equalsIgnoreCase(".avi")) {
                                type = "2";
                                // MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_VIDEO);
                                is_extenction = true;

                            } else if (extension.equalsIgnoreCase(".fiv")) {
                                type = "2";
                                //  MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_VIDEO);

                            } else if (extension.equalsIgnoreCase(".wmv")) {
                                type = "2";
                                is_extenction = true;
                                // MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_VIDEO);

                            } else if (extension.equalsIgnoreCase(".mov")) {
                                type = "2";
                                //MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_VIDEO);
                                is_extenction = true;

                            } else if (extension.equalsIgnoreCase(".mp4")) {
                                type = "2";
                                //  MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_VIDEO);
                                is_extenction = true;

                            } else if (extension.equalsIgnoreCase(".swf")) {
                                type = "2";
                                //MyPreferences.setPref(mActivity, CommonKeys.POST_TYPE, CommonKeys.TYPE_VIDEO);
                                is_extenction = true;

                            }
                        } else {
                            Log.e("test", "extension null");
                        }
                        if (is_extenction) {
                            deletemedia.setVisibility(View.VISIBLE);
                            sv_post_image.setImageURI(selectedImage);
                            path_upload_serever = String.valueOf(intetnData.getData());
                            path_upload_serever = Common_Methods.getRealPathFromURI(mActivity, Uri.parse(path_upload_serever));


                            if (type.equalsIgnoreCase("2")) {
                               video_view.setVideoURI(Uri.parse(path));
                                tv_click.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        progressbar.setVisibility(View.VISIBLE);
                                        video_view.setVisibility(View.VISIBLE);
                                        tv_click.setVisibility(View.GONE);
                                        video_view.setVideoPath(path_image_and_video);
                                        video_view.start();
                                        sv_post_image.setVisibility(View.GONE);
                                        play_icon.setVisibility(View.GONE);
                                        video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                            @Override
                                            public void onPrepared(MediaPlayer mp) {
                                                tv_click.setVisibility(View.VISIBLE);
                                                progressbar.setVisibility(View.GONE);
                                                tv_click.setSelected(true);
                                            }
                                        });
                                        video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                            @Override
                                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                                tv_click.setVisibility(View.VISIBLE);

                                                return false;
                                            }
                                        });
                                        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                tv_click.setSelected(false);
                                            }
                                        });
                                    }
                                });

                                String thumbnail_path = Common_Methods.getVideoThumb(mActivity, path);
                                Bitmap bmp = BitmapFactory.decodeFile(thumbnail_path);
                                if (bmp != null) {
                                    sv_post_image.setImageBitmap(bmp);
                                }
                            } else {
                                play_icon.setVisibility(View.GONE);
                            }
                            // mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance(String.valueOf(selectedImage), type,"","ADD"), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                        } else {
                            Toast.makeText(mActivity, "unvalid formate file", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;
        }
    }

    private void setFont() {
        tv_cancel.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_post.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        upload_post_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_user_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        edt_post_tag.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));

    }

    private void getBundleDataValue() {
        userDetailTable = UserDetailTable.getUserDetail();
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.getPhoto())) {
                sv_user_image.setImageURI(userDetailTable.getPhoto());
            }
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                user_id = userDetailTable.get_id();
            }
            tv_user_name.setText(userDetailTable.getFirstname() + " " + userDetailTable.getLastname());

        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(PATH)) {
                String path = bundle.getString(PATH);
                if (Validation.isRequiredField(path)) {

                    if (bundle.containsKey(THUMB_IMAGE)) {
                        getThumbImage = bundle.getString(THUMB_IMAGE);
                    }

                    path_image_and_video = bundle.getString(PATH);
                    path_upload_serever = Common_Methods.getRealPathFromURI(mActivity, Uri.parse(path_image_and_video));
                    if (Validation.isRequiredField(path_image_and_video)) {
                        if (bundle.containsKey(TYPE)) {
                            deletemedia.setVisibility(View.GONE);

                            post_type = bundle.getString(TYPE);
                            if (bundle.containsKey(POST_KEY)) {
                                posttype = bundle.getString(POST_KEY);
                            }

                            if (bundle.containsKey(MEDIA_TYPE)) {
                                mediatype = bundle.getString(MEDIA_TYPE);
                            }
                            if (mediatype.equalsIgnoreCase("image") || mediatype.equalsIgnoreCase("video")) {
                                iv_camera.setVisibility(View.VISIBLE);
                                deletemedia.setVisibility(View.VISIBLE);
                                sv_post_image.setVisibility(View.VISIBLE);
                            }else {
                                iv_camera.setVisibility(View.GONE);
                            }

                            if (post_type.equalsIgnoreCase("2")) {
                                if (posttype.equalsIgnoreCase("EDIT")) {
                                    Log.e("get media type", mediatype);
                                    deletemedia.setVisibility(View.VISIBLE);

                                    if (mediatype.equalsIgnoreCase("video")) {
                                        iv_camera.setVisibility(View.VISIBLE);
                                        play_icon.setVisibility(View.GONE);
                                        tv_click.setVisibility(View.VISIBLE);
                                        tv_click.setSelected(false);

                                        sv_post_image.setImageURI(getThumbImage);

                                        tv_click.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                progressbar.setVisibility(View.VISIBLE);
                                                video_view.setVisibility(View.VISIBLE);
                                                tv_click.setVisibility(View.GONE);
                                                video_view.setVideoPath(path_image_and_video);
                                                video_view.start();
                                                sv_post_image.setVisibility(View.GONE);
                                                play_icon.setVisibility(View.GONE);
                                                video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        tv_click.setVisibility(View.VISIBLE);
                                                        progressbar.setVisibility(View.GONE);
                                                        tv_click.setSelected(true);
                                                    }
                                                });
                                                video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                                    @Override
                                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                                        tv_click.setVisibility(View.VISIBLE);

                                                        return false;
                                                    }
                                                });
                                                video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                    @Override
                                                    public void onCompletion(MediaPlayer mp) {
                                                        tv_click.setSelected(false);
                                                    }
                                                });
                                            }
                                        });
                                    }

                                } else {
                                    tv_click.setVisibility(View.VISIBLE);
                                    play_icon.setVisibility(View.GONE);
                                    video_view.setVideoURI(Uri.parse(path));
                                    tv_click.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            progressbar.setVisibility(View.VISIBLE);
                                            video_view.setVisibility(View.VISIBLE);
                                            tv_click.setVisibility(View.GONE);
                                            video_view.setVideoPath(path_image_and_video);
                                            video_view.start();
                                            sv_post_image.setVisibility(View.GONE);
                                            play_icon.setVisibility(View.GONE);
                                            video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mp) {
                                                    tv_click.setVisibility(View.VISIBLE);
                                                    progressbar.setVisibility(View.GONE);
                                                    tv_click.setSelected(true);
                                                }
                                            });
                                            video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                                @Override
                                                public boolean onError(MediaPlayer mp, int what, int extra) {
                                                    tv_click.setVisibility(View.VISIBLE);

                                                    return false;
                                                }
                                            });
                                            video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mp) {
                                                    tv_click.setSelected(false);
                                                }
                                            });
                                        }
                                    });
                                    play_icon.setVisibility(View.VISIBLE);
                                    String thumbnail_path = Common_Methods.getVideoThumb(mActivity, path_image_and_video);
                                    Bitmap bmp = BitmapFactory.decodeFile(thumbnail_path);
                                    if (bmp != null) {
                                        sv_post_image.setImageBitmap(bmp);
                                    }
                                }
                            } else if (post_type.equalsIgnoreCase("4")) {
                                sv_post_image.setVisibility(View.GONE);
                                if (!is_link_preview) {
                                    link_str = path_image_and_video;
                                    is_link_preview = true;
                                    cardViewPreview.setVisibility(View.VISIBLE);
                                    is_preview_done=false;
                                    if (!path_image_and_video.contains("http://") || !path_image_and_video.contains("https://")){
                                        path_image_and_video = "http://" + path_image_and_video;
                                    }
                                    Log.e("test","url =="+path_image_and_video);
                                    mPreview.setData(path_image_and_video);
                                    is_statu_comming_from = true;
                                }
                            } else {
                                //type video
                                sv_post_image.setVisibility(View.VISIBLE);
                                sv_post_image.setImageURI(path_image_and_video);
                                Bitmap bmp = BitmapFactory.decodeFile(path_image_and_video);
                                if (bmp != null) {
                                   // sv_post_image.setImageBitmap(bmp);
                                }
                              /*  path_upload_serever = Common_Methods.getRealPathFromURI(mActivity, Uri.parse(path_image_and_video));
                                if(path_upload_serever!=null){
                                    sv_post_image.setImageURI(path_upload_serever);
                                }*/

                            }
                        }
                    }

                } else {
                    is_statu_comming_from = true;
                    sv_post_image.setVisibility(View.GONE);
                }

                if (bundle.containsKey(STATUS_KEY)) {
                    String status = bundle.getString(STATUS_KEY);
                    // if(Validation.isEmailValid(status)){
                    edt_post_tag.setText(status);
                    caption = edt_post_tag.getText().toString();
                    edt_post_tag.setSelection(status.length());
                    //   }
                }

                if (bundle.containsKey(SOCIALMEDIA_ID)) {
                    getSocialmediaId = bundle.getString(SOCIALMEDIA_ID);
                    Log.e("get media id", getSocialmediaId);
                }

                if (bundle.containsKey(POST_KEY)) {
                    posttype = bundle.getString(POST_KEY);
                    Log.e("get post type", posttype);
                    if (posttype.equalsIgnoreCase("EDIT")) {
                       // iv_camera.setVisibility(View.VISIBLE);
                        tv_edit.setVisibility(View.VISIBLE);
                        tv_post.setVisibility(View.GONE);
                    } else {
                        //iv_camera.setVisibility(View.GONE);
                        tv_edit.setVisibility(View.GONE);
                        tv_post.setVisibility(View.VISIBLE);
                    }
                }

                if (bundle.containsKey(SOCIAL_ID)) {
                    socialid = bundle.getString(SOCIAL_ID);
                }

            } else {
                is_statu_comming_from = true;
                sv_post_image.setVisibility(View.GONE);

            }
        } else {
            is_statu_comming_from = true;
            sv_post_image.setVisibility(View.GONE);

        }
    }

    private void setHeader() {
        mActivity.setToolbarBottomVisibility(false);
        mActivity.setToolbarTopVisibility(false);
    }

    private void initComponet() {
        edt_post_tag.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {
                    //do what you want
                    String str_value = edt_post_tag.getText().toString();
                    if (str_value != null) {
                        if (Validation.isUrl(str_value)) {
                            link_str = str_value;
                            is_link_preview = true;
                            cardViewPreview.setVisibility(View.VISIBLE);
                            if (!str_value.contains("http://") || !str_value.contains("https://")){
                                str_value = "http://" + str_value;
                            }
                            Log.e("test","url =="+str_value);
                            mPreview.setData(str_value);
                            is_preview_done=false;
                            is_statu_comming_from = true;

                        } else {
                            // cardViewPreview.setVisibility(View.GONE);
                        }
                        Log.e("test", "call Key enter");
                    }
                }
                    return false;
                }

        });

        deletemedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediatype.equalsIgnoreCase("video")) {
                    Log.e("get video path", path_image_and_video);
                    path_image_and_video = "";
                    deletemedia.setVisibility(View.GONE);
                    sv_post_image.setVisibility(View.GONE);
                    video_view.setVisibility(View.GONE);
                    tv_click.setVisibility(View.GONE);
                    play_icon.setVisibility(View.GONE);
                } else {
                    sv_post_image.setVisibility(View.GONE);
                    deletemedia.setVisibility(View.GONE);
                    Log.e("get image video path", path_image_and_video);
                    path_image_and_video = "";
                }
            }
        });
        mPreview.setListener(this);
        edt_post_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //    is_statu_comming_from = true;
                String str_value = s.toString();
                Log.e("test", "count_last==>>>" + count_last);
                Log.e("test", "start==>>>" + start);
                if (str_value != null) {
                    if (Validation.isUrl(str_value)) {
                        if(count_last<s.length()){
                            link_str = str_value;
                            is_link_preview = true;
                            is_preview_done=false;
                            cardViewPreview.setVisibility(View.VISIBLE);
                            if (!str_value.contains("http://") || !str_value.contains("https://")){
                                str_value = "http://" + str_value;
                            }
                                Log.e("test","url =="+str_value);
                            mPreview.setData(str_value);
                            Log.e("test", "==>>>" + s.toString());
                            is_statu_comming_from = true;
                        }

                    }else {
                        if(!is_preview_done){
                            count_last=0;
                            is_link_preview = false;
                            cardViewPreview.setVisibility(View.GONE);
                        }
                    }
                }else {
                    if(!is_preview_done){
                        is_link_preview = false;
                        cardViewPreview.setVisibility(View.GONE);
                    }
                }
                Log.e("test", "before==>>>" + before);
                Log.e("test", "count==>>>" + count);
                //Log.e("test", "start==>>>" + start);
                Log.e("test", "str_value==>>>" + str_value);
                count_last=s.length();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        if (m.matches()) {

            Log.e("test", "valid Url");
            return true;
        } else {
            return false;
        }
    }

    @OnClick({R.id.tv_cancel, R.id.tv_post, R.id.tv_edit,R.id.delete_link})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.delete_link:
                String txt_value=edt_post_tag.getText().toString();
                if (Validation.isUrl(txt_value)) {
                    edt_post_tag.setText("");
                }
                count_last=0;
                link_str="";
                is_link_preview = false;
                cardViewPreview.setVisibility(View.GONE);
                break;

            case R.id.tv_post:
                mActivity.tagmanager("Post Button","social_post_click");
                String write_something = edt_post_tag.getText().toString();
                if (ConnectionUtil.isInternetAvailable(mActivity)) {
                    if (is_statu_comming_from) {
                        if (Validation.isRequiredField(write_something) || Validation.isRequiredField(link_str)) {
                            callUploadLinkStatusApi(write_something);
                        } else {
                            mActivity.showCroutonsMessage(mActivity, mActivity.getResources().getString(R.string.write_post_empty));
                        }
                    } else {
                        callUploadApi(write_something);
                    }
                }
                break;
            case R.id.tv_edit:

                if((Validation.isRequiredField(edt_post_tag.getText().toString())) || (Validation.isRequiredField(link_str)) || sv_post_image.getVisibility()==View.VISIBLE) {
                    if (is_statu_comming_from) {
                        if (Validation.isRequiredField(edt_post_tag.getText().toString()) || Validation.isRequiredField(link_str)) {
                            callEditLinkStatusApi(edt_post_tag.getText().toString());
                        } else {
                            common_methods.setCutemDialogMessage(mActivity,  mActivity.getResources().getString(R.string.write_post_empty));
                        }
                    } else {
                        if (selectedImage == null) {
                            //    callwithoutEditImageVideoApi(edt_post_tag.getText().toString(), path_image_and_video);
                            if (caption.equalsIgnoreCase(edt_post_tag.getText().toString()) && selectedImage == null) {
                                mActivity.onBackPressed();
                                if (path_image_and_video == null || path_image_and_video.equalsIgnoreCase("")) {
                                    getSocialmediaId = "";
                                    callwithoutEditImageVideoApi(edt_post_tag.getText().toString(), "");
                                }
                            } else if (!caption.equalsIgnoreCase(edt_post_tag.getText().toString()) && selectedImage == null) {
                                Log.e("hit", "No change");
                               // callwithoutEditImageVideoApi(edt_post_tag.getText().toString(), "");

                                if (path_image_and_video == null || path_image_and_video.equalsIgnoreCase("")) {
                                    getSocialmediaId = "";
                                    callwithoutEditImageVideoApi(edt_post_tag.getText().toString(), "");
                                }else {
                                    callwithoutEditImageVideoApi(edt_post_tag.getText().toString(), "");
                                }
                            }
                        } else {
                            callEditImageVideoApi(edt_post_tag.getText().toString());
                        }

                    }
                }else {
                    common_methods.setCutemDialogMessage(mActivity,  mActivity.getResources().getString(R.string.write_post_empty));
                }

                break;
            case R.id.tv_cancel:
                mActivity.tagmanager("Upload Post 'Cancel' button","social_upload_cancel_click");
                mActivity.hideKeyboard();
                mActivity.onBackPressed();
                break;
            default:
                break;
        }
    }
    private void callUploadApi(String write_something) {
        Methods.isProgressShow(mActivity);
        File file;
        if (Validation.isRequiredField(path_upload_serever)) {
            file = new File(path_upload_serever);
            //      }
            final MultipartBody.Part body;
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                if (post_type.equalsIgnoreCase("1")) {
                    body = MultipartBody.Part.createFormData(WebApi.IMAGE_0, file.getAbsolutePath(), requestFile);
                } else {
                    body = MultipartBody.Part.createFormData(WebApi.VIDEO_0, file.getAbsolutePath(), requestFile);
                }
            } else {
                Log.e("test", "ERROR  ==> file get null");
                return;
            }
            if (body == null) {
                return;
                //if body found null it returns it's
            }
            RequestBody write_somethingbody = RequestBody.create(MediaType.parse("text/plain"), write_something);
            RequestBody user_idBody = RequestBody.create(MediaType.parse("text/plain"), user_id);

            WebApiClient.getInstance(mActivity).getWebApi().callPostUploadImageANDVideo(user_idBody, write_somethingbody, body).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();

                                mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.pushFragmentDontIgnoreCurrent(new SocialTimeLineFragment(), MIActivity.FRAGMENT_JUST_ADD);
                            } else {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();
                            }
                        }
                    }
                    Methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                    Methods.isProgressHide();
                    common_methods.setExceptionMessage(t, mActivity);

                    Log.e("test", "=>>" + t.getLocalizedMessage());
                }
            });
        }
    }

    private void callUploadLinkStatusApi(String write_something) {
        if (is_link_preview) {
            Methods.isProgressShow(mActivity);
            Log.e("link", link_str + " " + is_link_preview);

            WebApiClient.getInstance(mActivity).getWebApi().callPostUploadLink(user_id, write_something, link_str).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();

                                mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.pushFragmentDontIgnoreCurrent(new SocialTimeLineFragment(), MIActivity.FRAGMENT_JUST_ADD);
                            } else {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();
                            }
                        }
                    }
                    Methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                    Methods.isProgressHide();
                    common_methods.setExceptionMessage(t, mActivity);
                    Log.e("test", "=>>" + t.getLocalizedMessage());
                }
            });
        } else {
            Methods.isProgressShow(mActivity);
            WebApiClient.getInstance(mActivity).getWebApi().callPostUploadStatus(user_id, write_something).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();

                                mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.pushFragmentDontIgnoreCurrent(new SocialTimeLineFragment(), MIActivity.FRAGMENT_JUST_ADD);
                            } else {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();
                            }
                        }
                    }
                    Methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                    Methods.isProgressHide();
                    common_methods.setExceptionMessage(t, mActivity);
                    Methods.isProgressHide();

                    Log.e("test", "=>>" + t.getLocalizedMessage());
                }
            });
        }


    }

    // Edit Image and Video

    private void callEditImageVideoApi(String write_something) {
        Methods.isProgressShow(mActivity);
        File file;
        if (Validation.isRequiredField(path_upload_serever)) {
            file = new File(path_upload_serever);
            //      }
            final MultipartBody.Part body;
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                if (post_type.equalsIgnoreCase("1")) {
                    body = MultipartBody.Part.createFormData(WebApi.IMAGE_0, file.getAbsolutePath(), requestFile);
                } else {
                    body = MultipartBody.Part.createFormData(WebApi.VIDEO_0, file.getAbsolutePath(), requestFile);
                }
            } else {
                Log.e("test", "ERROR  ==> file get null");
                return;
            }
            if (body == null) {
                return;
                //if body found null it returns it's
            }
            RequestBody write_somethingbody = RequestBody.create(MediaType.parse("text/plain"), write_something);
            RequestBody social_idBody = RequestBody.create(MediaType.parse("text/plain"), socialid);

            WebApiClient.getInstance(mActivity).getWebApi().callEditImageVideoApi(social_idBody, write_somethingbody, body).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();

                                mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.pushFragmentDontIgnoreCurrent(new SocialTimeLineFragment(), MIActivity.FRAGMENT_JUST_ADD);
                            } else {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();
                            }
                        }
                    }
                    Methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                    Methods.isProgressHide();
                    common_methods.setExceptionMessage(t, mActivity);

                    Log.e("test", "=>>" + t.getLocalizedMessage());
                }
            });
        }
    }

    private void callEditLinkStatusApi(String write_something) {
        if (is_link_preview) {
            Methods.isProgressShow(mActivity);

            WebApiClient.getInstance(mActivity).getWebApi().callPostUploadLink(user_id, write_something, link_str).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();

                                mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.pushFragmentDontIgnoreCurrent(new SocialTimeLineFragment(), MIActivity.FRAGMENT_JUST_ADD);
                            } else {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();
                            }
                        }
                    }
                    Methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                    Methods.isProgressHide();
                    common_methods.setExceptionMessage(t, mActivity);
                    Log.e("test", "=>>" + t.getLocalizedMessage());
                }
            });
        } else {
            Methods.isProgressShow(mActivity);
            WebApiClient.getInstance(mActivity).getWebApi().callPostEditStatus(socialid, write_something).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();

                                mActivity.clearBackStack();
                                mActivity.clearBackStackFragmets();
                                mActivity.pushFragmentDontIgnoreCurrent(new SocialTimeLineFragment(), MIActivity.FRAGMENT_JUST_ADD);
                            } else {
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                                Methods.isProgressHide();
                            }
                        }
                    }
                    Methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                    Methods.isProgressHide();
                    common_methods.setExceptionMessage(t, mActivity);

                    Log.e("test", "=>>" + t.getLocalizedMessage());
                }
            });
        }


    }

    private void callwithoutEditImageVideoApi(String write_something, String path_image_and_video) {
        Log.e("get media_id", getSocialmediaId);
        Log.e("get selected image", selectedImage + "");
        Methods.isProgressShow(mActivity);
        WebApiClient.getInstance(mActivity).getWebApi().callwithoutEditImageVideoApi(socialid, edt_post_tag.getText().toString(), "", getSocialmediaId).enqueue(new Callback<BaseCommanRespons>() {
            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {

                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                            Methods.isProgressHide();

                            mActivity.clearBackStack();
                            mActivity.clearBackStackFragmets();
                            mActivity.pushFragmentDontIgnoreCurrent(new SocialTimeLineFragment(), MIActivity.FRAGMENT_JUST_ADD);
                        } else {
                            mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                            Methods.isProgressHide();
                        }
                    }
                }
                Methods.isProgressHide();
            }

            @Override
            public void onFailure(Call<BaseCommanRespons> call, Throwable t) {
                Methods.isProgressHide();
                common_methods.setExceptionMessage(t, mActivity);

                Log.e("test", "=>>" + t.getLocalizedMessage());
            }
        });
    }


    @Override
    public void onDataReady(Preview preview) {
        mPreview.setMessage(preview.getLink());
        is_preview_done = true;
        Log.e("test", "call onDataReady");

    }
}

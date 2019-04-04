package com.aktivo.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.Utils.Common_Methods;
import com.aktivo.Utils.ConnectionUtil;
import com.aktivo.Utils.MyItemDecoration_socialMedia;
import com.aktivo.Utils.Utilities;
import com.aktivo.activity.FullImageActivity;
import com.aktivo.interfaceclass.OnLoadMoreListener;
import com.aktivo.response.BaseCommanRespons;
import com.aktivo.response.Likes;
import com.aktivo.response.Member_profile_details;
import com.aktivo.response.SocialTimeLineReponse;
import com.aktivo.response.SocialTimeline;
import com.aktivo.response.UserDetailTable;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.ImageUtils;
import com.commonmodule.mi.utils.Validation;
import com.facebook.drawee.view.SimpleDraweeView;
import com.freesoulapps.preview.android.Preview;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocialTimeLineFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    //thumnailiamge
    @BindView(R.id.recyclview_post)
    RecyclerView recyclview_post;
    @BindView(R.id.user_pic)
    SimpleDraweeView user_pic;
    List<SocialTimeline> videoList;
    @BindView(R.id.edt_write_something)
    TextView edt_write_something;
    @BindView(R.id.tv_not_found)
    TextView tv_not_found;
    @BindView(R.id.tv_post)
    TextView tv_post;
    @BindView(R.id.tv_post_to)
    TextView tv_post_to;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.ll_something_write)
    LinearLayout ll_something_write;
    Dialog dialogBuilder;
    ArrayList<String> arrayListImage;
    String mCurrentPhotoPath = "";
    public static final int PHOTO_TAKE_CAMERA = 5;
    public static final int PHOTO_GALLARY_OPEN = 6;
    //video payler
    //video list required function
    public static boolean isPlaying;
    public static SimpleDraweeView previousPreview;
    public static VideoView previousView;
    public static TextView tv_click, tv_name, tv_current_time;
    public static String url, name, previewUrl, selectedcategory, selectedsubcategory = "Hide this post";
    ;
    public static int clickPosition;
    public static SeekBar seekBar;
    public static String isPlayingStatus;
    public static LinearLayout ll_btm;
    public static ProgressBar progressBar;
    static int stopPosition;
    int offset_value = 1;
    public static LinearLayoutManager linearLayoutManager;
    UserDetailTable userDetailTable;
    String user_id = "";
    Handler seekBarHandler = new Handler();
    PostAdaptor postAdaptor;
    private ArrayList<String> listreport;
    private ArrayList<String> listreportstatus;
    private ReportUserAdaptor reportAdaptor;
    boolean flag = false;

    Unbinder unbinder;

    BroadcastReceiver broadcastReceiver_commentCountupdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    if (bundle.containsKey(CommonKeys.SOCIAL_POSTION)) {
                        int postion = bundle.getInt(CommonKeys.SOCIAL_POSTION);
                        String post_type = bundle.getString(CommonKeys.POSTTYPE);
                        String total_comment_count = bundle.getString(CommonKeys.TOTAL_COUNT);
                        RecyclerView.ViewHolder viewHolder = recyclview_post.findViewHolderForAdapterPosition(postion);
                        switch (post_type) {
                            case CommonKeys.MEDIAY_TYPE_STATUS:
                                PostAdaptor.ViewHolderStatus viewHolderStatus = (PostAdaptor.ViewHolderStatus) viewHolder;
                                if (viewHolderStatus != null) {
                                    viewHolderStatus.tv_comment_total.setText("" + total_comment_count);
                                }
                                break;
                            case CommonKeys.MEDIAY_TYPE_IMAGE:
                                PostAdaptor.ViewHolderImage viewHolderImage = (PostAdaptor.ViewHolderImage) viewHolder;
                                if (viewHolderImage != null) {
                                    viewHolderImage.tv_comment_total.setText("" + total_comment_count);
                                }
                                break;
                            case CommonKeys.MEDIAY_TYPE_VIDEO:
                                PostAdaptor.ViewHolderVideo viewHolderVideo = (PostAdaptor.ViewHolderVideo) viewHolder;
                                if (viewHolderVideo != null) {
                                    viewHolderVideo.tv_comment_total.setText("" + total_comment_count);
                                }
                                break;
                            case CommonKeys.MEDIAY_TYPE_LINK:
                                PostAdaptor.ViewHolderPriviewLink viewHolderPriviewLink = (PostAdaptor.ViewHolderPriviewLink) viewHolder;
                                if (viewHolderPriviewLink != null) {
                                    if (viewHolderPriviewLink != null) {
                                        viewHolderPriviewLink.tv_comment_total.setText("" + total_comment_count);
                                    }
                                }
                                break;
                            default:
                                break;
                        }

                    }
                }
            }
        }

    };
    Runnable seekBarProgressRunnable = new Runnable() {
        @Override
        public void run() {
            int totalDuration = previousView.getDuration();
            int currentTime = previousView.getCurrentPosition();
            tv_current_time.setText(Utilities.milliSecondsToTimer(previousView.getCurrentPosition()));
            int progressinsec = (int) (Utilities.getProgressPercentage(currentTime, totalDuration));
            int progressBuffer = (int) (previousView.getBufferPercentage());
            seekBar.setProgress(progressinsec);
            seekBar.setSecondaryProgress(progressBuffer);
            seekBarHandler.postDelayed(this, 100);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.social_timeline_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        LocalBroadcastManager.getInstance(mActivity).registerReceiver(broadcastReceiver_commentCountupdate, new IntentFilter(CommonKeys.COMMENT_UPDATE_COUNT_BR));
        setHeader();


        initComponet();
        setfont();
    }

    private void setfont() {
        edt_write_something.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
        tv_post.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_post_to.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
        tv_not_found.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
    }

    private void initComponet() {
        swipeRefreshLayout.setOnRefreshListener(this);
        userDetailTable = UserDetailTable.getUserDetail();
        if (userDetailTable != null) {
            if (Validation.isRequiredField(userDetailTable.get_id())) {
                user_id = userDetailTable.get_id();
            }
            if (Validation.isRequiredField(userDetailTable.getPhoto())) {
                user_pic.setImageURI(userDetailTable.getPhoto());
            }
        }
        videoList = new ArrayList<>();
        postAdaptor = new PostAdaptor(videoList, recyclview_post);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclview_post.setLayoutManager(linearLayoutManager);
        recyclview_post.setAdapter(postAdaptor);
        recyclview_post.addItemDecoration(new MyItemDecoration_socialMedia());
        mActivity.tagmanager("Social Wall Screen", "social_view");

        recyclview_post.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (flag == false) {
                    flag = true;
                    mActivity.tagmanager("Social Wall Screen", "social_scroll");
                }

            }
        });
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callSocialTimeLineApi(1, true, false);
                }
            }, 300);
        } else {
            tv_not_found.setVisibility(View.VISIBLE);
        }

        //Load more
        postAdaptor.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        if (!videoList.contains(null)) {
                            videoList.add(null);
                            postAdaptor.notifyItemInserted(videoList.size() - 1);
                        }
                    }
                };
                handler.post(r);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (videoList.contains(null)) {
                            videoList.remove(videoList.indexOf(null));
                        }
                        offset_value = offset_value + 1;
                        postAdaptor.notifyItemRemoved(videoList.size());
                        callSocialTimeLineApi(offset_value, false, false);
                        System.out.println("offset_value = " + offset_value);

                    }
                }, 1000);

            }
        });
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (clickPosition < linearLayoutManager.findFirstVisibleItemPosition()
                        || clickPosition > linearLayoutManager.findLastVisibleItemPosition()) {

                    if (isPlaying) {
                        isPlaying = false;
                        tv_click.setVisibility(View.VISIBLE);
                        tv_click.setSelected(false);

                        progressBar.setVisibility(View.GONE);
                        previousPreview.setImageURI(previewUrl);
                        previousPreview.setVisibility(View.VISIBLE);

                        // tv_name.setText(name);
                        //tv_name.setVisibility(View.VISIBLE);

                        previousView.stopPlayback();
                        previousView.setVisibility(View.GONE);
                        ll_btm.setVisibility(View.GONE);

                        ll_btm.setVisibility(View.GONE);

                        resetStatics();
                    }
                }
            }
        };
        // adding scroll listener to notify when scrolling event happens.
        recyclview_post.addOnScrollListener(onScrollListener);
    }

    Call<SocialTimeLineReponse> socialTimeLineReponseCall;

    private void callSocialTimeLineApi(int page, final boolean is_firstLoad, final boolean is_swif_refresh) {
        tv_not_found.setVisibility(View.GONE);
        if (!is_swif_refresh) {
            if (is_firstLoad) {
                progress_bar.setVisibility(View.VISIBLE);
            }
        }
        socialTimeLineReponseCall = WebApiClient.getInstance(mActivity).getWebApi().callSocialTimeLneApi(user_id, String.valueOf(page));
        socialTimeLineReponseCall.enqueue(new Callback<SocialTimeLineReponse>() {
            @Override
            public void onResponse(Call<SocialTimeLineReponse> call, Response<SocialTimeLineReponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            List<SocialTimeline> data = response.body().getData();

                            if (is_firstLoad || is_swif_refresh) {
                                if (videoList != null && !videoList.isEmpty()) {
                                    videoList.clear();
                                }
                            }
                            final Member_profile_details member_profile_details = response.body().getMember_profile_details();
                            if (member_profile_details != null) {

                                UserDetailTable userDetailTable = UserDetailTable.getUserDetail();
                                if (Validation.isRequiredField(member_profile_details.getPhoto())) {

                                    if (userDetailTable != null) {
                                        userDetailTable.setPhoto(member_profile_details.getPhoto());
                                    }
                                    user_pic.setImageURI(member_profile_details.getPhoto());
                                }
                                if (Validation.isRequiredField(member_profile_details.getFirstname())) {
                                    if (userDetailTable != null) {
                                        userDetailTable.setFirstname(member_profile_details.getFirstname());
                                    }
                                }
                                if (Validation.isRequiredField(member_profile_details.getLastname())) {
                                    if (userDetailTable != null) {
                                        userDetailTable.setLastname(member_profile_details.getLastname());
                                    }
                                }
                                userDetailTable.save();
                            }

                            if (data != null && data.size() > 0) {
                                tv_not_found.setVisibility(View.GONE);
                                for (int i = 0; i < data.size(); i++) {
                                    videoList.add(data.get(i));
                                }
                                if (videoList != null && videoList.size() > 0) {
                                    tv_not_found.setVisibility(View.GONE);

                                    if (postAdaptor != null) {
                                        postAdaptor.updateData(videoList);
                                    }
                                } else {
                                    tv_not_found.setVisibility(View.VISIBLE);
                                }

                            } else {


                            }
                        } else {

                        }
                    }
                }
                if (is_swif_refresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                progress_bar.setVisibility(View.GONE);

                if (videoList != null && videoList.size() > 0) {
                    tv_not_found.setVisibility(View.GONE);

                    if (postAdaptor != null) {
                        postAdaptor.updateData(videoList);
                    }
                } else {
                    tv_not_found.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<SocialTimeLineReponse> call, Throwable t) {
                if (is_visible_fragment) {
                    common_methods.setExceptionMessage(t, mActivity);
                    if (is_swif_refresh) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    progress_bar.setVisibility(View.GONE);
                    if (videoList != null && videoList.size() > 0) {
                        tv_not_found.setVisibility(View.GONE);
                        if (postAdaptor != null) {
                            postAdaptor.updateData(videoList);
                        }
                    } else {
                        tv_not_found.setVisibility(View.VISIBLE);
                    }
                }
            }

        });
    }

    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.SOICAL_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();
        if (Validation.isRequiredField(common_methods.getTodayHaveData().getSocial_wall_background_image())) {
            mActivity.setBackgroudnImage(common_methods.getTodayHaveData().getSocial_wall_background_image());
        }
        //  mActivity.setBackgroudnImage("http://1h23on8hs5s44c2iqm9tze2x.wpengine.netdna-cdn.com/wp-content/uploads/2014/02/Studio-5.jpg");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();
        }
    }

    @OnClick({R.id.iv_camera, R.id.edt_write_something, R.id.iv_menu})
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_camera:
                checkStoragePermission();
                break;
            case R.id.edt_write_something:
                mActivity.pushFragmentDontIgnoreCurrent(new UpLoadPostFragment(), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                break;
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            default:
                break;
        }

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CommonKeys.PERMISSION_CODE:
                if (Common_Methods.isPermissionNotGranted(mActivity, permissions)) {
                    Common_Methods.whichPermisionNotGranted(mActivity, permissions, grantResults);
                } else {
                    //                    mapFragment.getMapAsync(ContactUsFragment.this);
                    openPhotoDilogGallery();
                }
                break;
            default:
                break;
        }
    }

    private void openMutipleImageUPloadGallrey() {


       /* Intent intent = new Intent(mActivity, PostActivity.class);
        intent.putExtra(CommonKeys.INTENT_EXTRA_LIMIT, 3);
        intent.putExtra("MSG", "IMAGE");
        startActivityForResult(intent, CommonKeys.REQUEST_CODE);*/
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
                   /* if (intetnData != null) {
                        Uri selectedImage = intetnData.getData();
                        if (selectedImage != null) {
                            String mPath = getRealPathFromURI(selectedImage);
                            Log.e("test","image path=>>"+mPath);
                         mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance(mPath),mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                        }
                    }*/
                    Uri selectedImage = intetnData.getData();
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
                            mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance(String.valueOf(selectedImage), type, "", "ADD", "", "", "", "image"), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                        } else {
                            Toast.makeText(mActivity, "unvalid formate file", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                break;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                mActivity,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
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
    public void onRefresh() {
        offset_value = 1;
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            callSocialTimeLineApi(offset_value, false, true);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public class PostAdaptor extends RecyclerView.Adapter {

        List<SocialTimeline> postListData;
        RecyclerView recyclerView;
        private final int VIEW_TYPE_STATUS = 1;
        private final int VIEW_TYPE_IMAGE = 2;
        private final int VIEW_TYPE_VIDEO = 3;
        private final int VIEW_TYPE_PRIVIEW_LINK = 4;
        private final int VIEW_TYPE_LOADING = 5;

        private final int VIEW_TYPE_ITEM = 0;
        private OnLoadMoreListener mOnLoadMoreListener;
        private boolean isLoading;
        private int totalItemCount, visibleItemCount, firstVisibleItem;

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        public PostAdaptor(List<SocialTimeline> videoList, RecyclerView recyclerView) {
            this.postListData = videoList;
            this.recyclerView = recyclerView;
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        totalItemCount = SocialTimeLineFragment.linearLayoutManager.getItemCount();
                        visibleItemCount = SocialTimeLineFragment.linearLayoutManager.getChildCount();
                        firstVisibleItem = SocialTimeLineFragment.linearLayoutManager.findFirstVisibleItemPosition();
                        Log.e("test", "firstVisibleItem" + firstVisibleItem);

                        /*if(firstVisibleItem==0){
                            ll_something_write.setVisibility(View.VISIBLE);
                        }else {
                            ll_something_write.setVisibility(View.VISIBLE);

                        }*/
                        if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount) {

                            if (mOnLoadMoreListener != null) {
                                mOnLoadMoreListener.onLoadMore();
                            }

                            isLoading = true;
                        }

                    }
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            switch (viewType) {

                case VIEW_TYPE_STATUS:
                    View v1 = inflater.inflate(R.layout.row_status, parent, false);
                    viewHolder = new ViewHolderStatus(v1);
                    break;

                case VIEW_TYPE_IMAGE:
                    View v2 = inflater.inflate(R.layout.row_post_images, parent, false);
                    viewHolder = new ViewHolderImage(v2);
                    break;

                case VIEW_TYPE_VIDEO:
                    View v3 = inflater.inflate(R.layout.row_video, parent, false);
                    viewHolder = new ViewHolderVideo(v3);
                    break;
                case VIEW_TYPE_PRIVIEW_LINK:
                    View v4 = inflater.inflate(R.layout.row_proview_link, parent, false);
                    viewHolder = new ViewHolderPriviewLink(v4);
                    break;
                case VIEW_TYPE_LOADING:
                    View v5 = inflater.inflate(R.layout.layout_loading_item, parent, false);
                    viewHolder = new ViewHolderLoadling(v5);

            }

            return viewHolder;

        }

        @Override
        public int getItemViewType(int position) {

            if (postListData.get(position) != null) {
                if (postListData.get(position).getSocial_media().size() == 0) {
                    return VIEW_TYPE_STATUS;
                } else {
                    if (postListData.get(position).getSocial_media().get(0).getMedia_type().equalsIgnoreCase("Image")) {
                        return VIEW_TYPE_IMAGE;
                    } else if (postListData.get(position).getSocial_media().get(0).getMedia_type().equalsIgnoreCase("Video")) {
                        return VIEW_TYPE_VIDEO;
                    } else if (postListData.get(position).getSocial_media().get(0).getMedia_type().equalsIgnoreCase("Link")) {
                        return VIEW_TYPE_PRIVIEW_LINK;
                    }
                    return VIEW_TYPE_LOADING;
                }
            } else {

                System.out.println("position is null = " + position);

                System.out.println("homeFeed_table_list = " + postListData.size());
                return VIEW_TYPE_LOADING;

            }

        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            if (postListData.get(position) != null) {

                String linkeCount = "0";
                String commentCount = "0";
                String created_time = "";
                String user_name = postListData.get(position).getName();
                String user_profile = postListData.get(position).getPhoto();
                String department = postListData.get(position).getDepartment();

                //final String final_action=action_like;
                if (Validation.isRequiredField(department)) {
                    department = department;
                }
                String caption_title = postListData.get(position).getCaption_title();
                Log.e("test", "=>>>" + caption_title);
                if (Validation.isRequiredField(postListData.get(position).getCreated_at())) {

                    created_time = postListData.get(position).getCreated_at();
                }

                if (Validation.isRequiredField(postListData.get(position).getLikes_count())) {
                    linkeCount = postListData.get(position).getLikes_count();
                }
                if (Validation.isRequiredField(postListData.get(position).getComments_count())) {
                    commentCount = postListData.get(position).getComments_count();
                }
                if (postListData.get(position).getSocial_media().size() == 0) {
                        /*=========================Status Type============================*/

                    final ViewHolderStatus holderStatus = (ViewHolderStatus) holder;
                    holderStatus.ll_comment.setVisibility(View.GONE);
                    holderStatus.ll_comment.setVisibility(View.GONE);
                    holderStatus.tv_like_total.setText("" + linkeCount);
                    holderStatus.tv_comment_total.setText("" + commentCount);
                    holderStatus.tv_time.setText("" + created_time);
                    holderStatus.tv_status.setText("" + caption_title);
                    holderStatus.tv_tag_line.setText("" + department);

                    holderStatus.llFollowUnfollow.setVisibility(View.GONE);
                    holderStatus.llEditDelete.setVisibility(View.GONE);

                    //fontset
                    holderStatus.tv_usre_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holderStatus.tv_tag_line.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                    holderStatus.tv_time.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holderStatus.tv_status.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                    holderStatus.tv_comment_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holderStatus.tv_like_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                    holderStatus.tv_post.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));

                    // need to set font
                    holderStatus.tv_hidepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                    holderStatus.tv_unfollowuser.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                    holderStatus.tv_reportpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                    holderStatus.tv_editpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                    holderStatus.tv_deletepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
/*
                        if(action_like.equalsIgnoreCase(CommonKeys.LIKE)){
                           holderStatus.iv_like.setColorFilter(getContext().getResources().getColor(R.color.black));
                        }else {
                            holderStatus.iv_like.setColorFilter(getContext().getResources().getColor(R.color.transparent));
                        }
*/

                    if (Validation.isRequiredField(user_name)) {
                        holderStatus.tv_usre_name.setText(user_name + " /");
                    }
                    if (Validation.isRequiredField(user_profile)) {
                        holderStatus.user_pic.setImageURI(user_profile);
                        holderStatus.user_pic_comment.setImageURI(user_profile);

                    }
                    if (Validation.isRequiredField(caption_title)) {
                        holderStatus.tv_status.setText(caption_title);
                    }

                    holderStatus.edt_comment.setHint("Reply to " + user_name + ":");
                    holderStatus.edt_comment.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

                    // for comment box >>> Status

                    holderStatus.iv_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment findoggsFragment = SocialCommentFragment.newInstance(postListData.get(position).getComments(), postListData.get(position).get_id(), CommonKeys.MEDIAY_TYPE_STATUS, position);
                            if (findoggsFragment != null) {
                                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setCustomAnimations(R.anim.open_from_bottom, R.anim.close_from_top);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.add(R.id.fragment_container, findoggsFragment);
                                fragmentTransaction.commit();
                            }

                        }

                    });

                    holderStatus.tv_post.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String comment_str = holderStatus.edt_comment.getText().toString().trim();
                            if (Validation.isRequiredField(comment_str)) {
                                callpostCommentApi(position, videoList, comment_str);
                                holderStatus.ll_comment.setVisibility(View.GONE);
                                mActivity.hideKeyboard();
                                holderStatus.edt_comment.setText("");
                                mActivity.tagmanager("Upload Post 'Post' button", "social_upload_post_click");
                            } else {
                                holderStatus.edt_comment.setError("Please Enter post");
                            }
                        }
                    });

                    holderStatus.iv_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (postListData.get(position).getLikes().size() == 0) {
                                callPostLoveApi(CommonKeys.UNLIKE, position, postListData);
                            } else {
                                String action_like = "";
                                List<Likes> likesList = postListData.get(position).getLikes();
                                if (likesList.size() == 0) {
                                    action_like = CommonKeys.UNLIKE;
                                } else {
                                    for (int i = 0; i < likesList.size(); i++) {
                                        if (user_id.equalsIgnoreCase(likesList.get(i).getMember_id())) {
                                            action_like = likesList.get(i).getFeedbacktype();
                                            break;
                                        } else {
                                            action_like = CommonKeys.UNLIKE;
                                        }
                                    }


                                }
                                callPostLoveApi(action_like, position, postListData);
                            }
                        }
                    });

                    holderStatus.img_follow_unfollow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {

                                if (holderStatus.llEditDelete.getVisibility() == View.VISIBLE) {
                                    holderStatus.llEditDelete.setVisibility(View.GONE);
                                    holderStatus.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                } else {
                                    holderStatus.llEditDelete.setVisibility(View.VISIBLE);
                                    holderStatus.img_follow_unfollow.setImageResource(R.drawable.up_arrow);
                                }

                            } else {
                                if (holderStatus.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holderStatus.llFollowUnfollow.setVisibility(View.GONE);
                                    holderStatus.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                } else {
                                    holderStatus.img_follow_unfollow.setImageResource(R.drawable.up_arrow);

                                    holderStatus.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    });

                    holderStatus.tv_hidepost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            openHidePostConfirmDialog(postListData, user_id, position);


                            if (holderStatus.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                holderStatus.llFollowUnfollow.setVisibility(View.GONE);
                            } else {
                                holderStatus.llFollowUnfollow.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                    holderStatus.tv_unfollowuser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openUnfollowConfirmDialog(postListData, user_id, position);

                            if (holderStatus.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                holderStatus.llFollowUnfollow.setVisibility(View.GONE);
                            } else {
                                holderStatus.llFollowUnfollow.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                    holderStatus.tv_reportpost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openReportUserDialoge(postListData, position);
                            if (holderStatus.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                holderStatus.llFollowUnfollow.setVisibility(View.GONE);
                            } else {
                                holderStatus.llFollowUnfollow.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    holderStatus.tv_editpost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance("", "3", postListData.get(position).getCaption_title(), "EDIT", postListData.get(position).get_id(), "", "", ""), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                        }
                    });
                    holderStatus.tv_deletepost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callDeletePostApi(postListData, position);
                            if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {

                                if (holderStatus.llEditDelete.getVisibility() == View.VISIBLE) {
                                    holderStatus.llEditDelete.setVisibility(View.GONE);
                                } else {
                                    holderStatus.llEditDelete.setVisibility(View.VISIBLE);
                                }
                            } else {

                                if (holderStatus.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holderStatus.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holderStatus.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });


                } else {

                    if (postListData.get(position).getSocial_media().get(0).getMedia_type().equalsIgnoreCase("Image")) {

                        /*======================================Image Type==================================================*/

                        final ViewHolderImage holder_Image = (ViewHolderImage) holder;
                        holder_Image.ll_comment.setVisibility(View.GONE);
                        holder_Image.tv_like_total.setText("" + linkeCount);
                        holder_Image.tv_comment_total.setText("" + commentCount);
                        holder_Image.tv_time.setText("" + created_time);
                        holder_Image.tv_tag_line.setText("" + department);

                        //fontset
                        holder_Image.tv_usre_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_Image.tv_tag_line.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_Image.tv_time.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_Image.tv_status.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                        holder_Image.tv_comment_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_Image.tv_like_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_Image.tv_post.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));


                        // need to set font
                        holder_Image.tv_hidepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_Image.tv_unfollowuser.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_Image.tv_reportpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_Image.tv_editpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_Image.tv_deletepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));

                        holder_Image.llFollowUnfollow.setVisibility(View.GONE);
                        holder_Image.llEditDelete.setVisibility(View.GONE);

      /*  if(action_like.equalsIgnoreCase(CommonKeys.LIKE)){
                                holder_Image.iv_like.setColorFilter(getContext().getResources().getColor(R.color.black));
                            }else {
                                holder_Image.iv_like.setColorFilter(getContext().getResources().getColor(R.color.transparent));
                            }*/

                        arrayListImage = new ArrayList<>();
                        // arrayListImage.clear();
                        //arrayListImage=postListData.get(position).getStringsList();
                        arrayListImage.add(postListData.get(position).getSocial_media().get(0).getFile());
                        loadImagesFromArrayList(holder_Image);

                        holder_Image.sdv_single_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mActivity, FullImageActivity.class).putExtra(CommonKeys.IMAGE_URL_POST, postListData.get(position).getSocial_media().get(0).getFile()));
                            }
                        });

                        holder_Image.sdv_img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mActivity, FullImageActivity.class).putExtra(CommonKeys.IMAGE_ARAARY, arrayListImage));

                            }
                        });

                        holder_Image.sdv_img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mActivity, FullImageActivity.class).putExtra(CommonKeys.IMAGE_ARAARY, arrayListImage).putExtra(CommonKeys.IMG_POSITION, 1));

                            }
                        });

                        holder_Image.sdv_img3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mActivity, FullImageActivity.class).putExtra(CommonKeys.IMAGE_ARAARY, arrayListImage).putExtra(CommonKeys.IMG_POSITION, 2));

                            }
                        });

                        holder_Image.sdv_img4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mActivity, FullImageActivity.class).putExtra(CommonKeys.IMAGE_ARAARY, arrayListImage).putExtra(CommonKeys.IMG_POSITION, 3));

                            }
                        });
                        if (Validation.isRequiredField(user_name)) {
                            holder_Image.tv_usre_name.setText(user_name + " /");
                        }
                        if (Validation.isRequiredField(user_profile)) {
                            holder_Image.user_pic.setImageURI(user_profile);
                            holder_Image.user_pic_comment.setImageURI(user_profile);

                        }

                        if (Validation.isRequiredField(caption_title)) {
                            holder_Image.tv_status.setText(caption_title);
                        }
                       /* if(Validation.isRequiredField(postListData.get(position).getUrl())){
                            holder_Image.iv_post_image.setImageURI(postListData.get(position).getUrl());
                        }*/
                        holder_Image.iv_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment findoggsFragment = SocialCommentFragment.newInstance(postListData.get(position).getComments(), postListData.get(position).get_id(), CommonKeys.MEDIAY_TYPE_IMAGE, position);
                                if (findoggsFragment != null) {
                                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.open_from_bottom, R.anim.close_from_top);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.add(R.id.fragment_container, findoggsFragment);
                                    fragmentTransaction.commit();
                                }
                            }
                        });
                        holder_Image.edt_comment.setHint("Reply to " + user_name + ":");
                        holder_Image.edt_comment.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

                        holder_Image.tv_post.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String comment_str = holder_Image.edt_comment.getText().toString().trim();
                                if (Validation.isRequiredField(comment_str)) {
                                    callpostCommentApi(position, videoList, comment_str);
                                    holder_Image.ll_comment.setVisibility(View.GONE);
                                    mActivity.hideKeyboard();
                                    holder_Image.edt_comment.setText("");

                                } else {
                                    holder_Image.edt_comment.setError("Please Enter post");
                                }
                            }
                        });
                        holder_Image.iv_like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (postListData.get(position).getLikes().size() == 0) {
                                    callPostLoveApi(CommonKeys.UNLIKE, position, postListData);
                                } else {
                                    String action_like = "";
                                    List<Likes> likesList = postListData.get(position).getLikes();
                                    if (likesList.size() == 0) {
                                        action_like = CommonKeys.UNLIKE;
                                    } else {
                                        for (int i = 0; i < likesList.size(); i++) {
                                            if (user_id.equalsIgnoreCase(likesList.get(i).getMember_id())) {
                                                action_like = likesList.get(i).getFeedbacktype();
                                                break;
                                            } else {
                                                action_like = CommonKeys.UNLIKE;
                                            }
                                        }
                                    }
                                    callPostLoveApi(action_like, position, postListData);
                                }
                            }
                        });

                       /* holder_Image.iv_post_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(mActivity, FullImageActivity.class).putExtra(CommonKeys.COMMING_USER_OWN,false).putExtra(CommonKeys.IMAGE_URL_PROFILE,postListData.get(position).getUrl()));
                            }
                        });*/


                        holder_Image.img_follow_unfollow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {

                                    if (holder_Image.llEditDelete.getVisibility() == View.VISIBLE) {
                                        holder_Image.llEditDelete.setVisibility(View.GONE);
                                        holder_Image.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                    } else {
                                        holder_Image.llEditDelete.setVisibility(View.VISIBLE);
                                        holder_Image.img_follow_unfollow.setImageResource(R.drawable.up_arrow);

                                    }
                                } else {

                                    if (holder_Image.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                        holder_Image.llFollowUnfollow.setVisibility(View.GONE);
                                        holder_Image.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                    } else {
                                        holder_Image.img_follow_unfollow.setImageResource(R.drawable.up_arrow);

                                        holder_Image.llFollowUnfollow.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        });

                        holder_Image.tv_hidepost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openHidePostConfirmDialog(postListData, user_id, position);
                                if (holder_Image.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_Image.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_Image.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        holder_Image.tv_unfollowuser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openUnfollowConfirmDialog(postListData, user_id, position);
                                if (holder_Image.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_Image.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_Image.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        holder_Image.tv_reportpost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openReportUserDialoge(postListData, position);
                                if (holder_Image.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_Image.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_Image.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        holder_Image.tv_editpost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance(String.valueOf(postListData.get(position).getSocial_media().get(0).getFile()), "1", postListData.get(position).getCaption_title(), "EDIT", postListData.get(position).get_id(), "", String.valueOf(postListData.get(position).getSocial_media().get(0).get_id()), "image"), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);

                            }
                        });
                        holder_Image.tv_deletepost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callDeletePostApi(postListData, position);
                                if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {

                                    if (holder_Image.llEditDelete.getVisibility() == View.VISIBLE) {
                                        holder_Image.llEditDelete.setVisibility(View.GONE);
                                    } else {
                                        holder_Image.llEditDelete.setVisibility(View.VISIBLE);
                                    }
                                } else {

                                    if (holder_Image.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                        holder_Image.llFollowUnfollow.setVisibility(View.GONE);
                                    } else {
                                        holder_Image.llFollowUnfollow.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                    } else if ((postListData.get(position).getSocial_media().get(0).getMedia_type().equalsIgnoreCase("Video"))) {

                        /*======================================Video Type==================================================*/
                        final ViewHolderVideo holder_video = (ViewHolderVideo) holder;
                        holder_video.ll_comment.setVisibility(View.GONE);
                        holder_video.edt_comment.setHint("Reply to " + user_name + ":");
                        holder_video.edt_comment.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

                        holder_video.tv_like_total.setText("" + linkeCount);
                        holder_video.tv_comment_total.setText("" + commentCount);
                        holder_video.tv_time.setText("" + created_time);
                        holder_video.tv_status.setText("" + caption_title);
                        holder_video.tv_tag_line.setText("" + department);


                        //fontset
                        holder_video.tv_usre_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_video.tv_tag_line.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_video.tv_time.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_video.tv_status.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                        holder_video.tv_comment_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_video.tv_like_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_video.tv_post.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));


                        // need to set font
                        holder_video.tv_hidepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_video.tv_unfollowuser.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_video.tv_reportpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_video.tv_editpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_video.tv_deletepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));

                        holder_video.llFollowUnfollow.setVisibility(View.GONE);
                        holder_video.llEditDelete.setVisibility(View.GONE);

                            /*if(action_like.equalsIgnoreCase(CommonKeys.LIKE)){
                                holder_video.iv_like.setColorFilter(getContext().getResources().getColor(R.color.black));
                            }else {
                                holder_video.iv_like.setColorFilter(getContext().getResources().getColor(R.color.transparent));
                            }*/

                         /*=====video player=====================*/
                        videoListCommonFunction(position, holder_video);

                        if (Validation.isRequiredField(user_name)) {
                            holder_video.tv_usre_name.setText(user_name + " /");
                        }
                        if (Validation.isRequiredField(user_profile)) {
                            holder_video.user_pic.setImageURI(user_profile);
                            holder_video.user_pic_comment.setImageURI(user_profile);

                        }
                        if (Validation.isRequiredField(caption_title)) {
                            holder_video.tv_status.setText(caption_title);
                        }
                        if (Validation.isRequiredField(postListData.get(position).getSocial_media().get(0).getFile())) {
                            // holder_video.video_view.setVideoURI(Uri.parse(postListData.get(position).getUrl()));
                        }
                       /* if(Validation.isRequiredField(postListData.get(position).getThumnail())){
                            holder_video.iv_preview.setImageURI(Uri.parse(postListData.get(position).getThumnail()));
                        }*/
                       /* holder_video.tv_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(Validation.isRequiredField(postListData.get(position).getUrl())){
                                    holder_video.video_view.setVideoURI(Uri.parse(postListData.get(position).getUrl()));
                                }
                            }
                        });*/
                        holder_video.iv_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment findoggsFragment = SocialCommentFragment.newInstance(postListData.get(position).getComments(), postListData.get(position).get_id(), CommonKeys.MEDIAY_TYPE_VIDEO, position);
                                if (findoggsFragment != null) {
                                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.open_from_bottom, R.anim.close_from_top);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.add(R.id.fragment_container, findoggsFragment);
                                    fragmentTransaction.commit();
                                }
                            }
                        });
                        holder_video.tv_post.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String comment_str = holder_video.edt_comment.getText().toString().trim();
                                if (Validation.isRequiredField(comment_str)) {
                                    callpostCommentApi(position, videoList, comment_str);
                                    holder_video.ll_comment.setVisibility(View.GONE);
                                    mActivity.hideKeyboard();
                                    holder_video.edt_comment.setText("");
                                } else {
                                    holder_video.edt_comment.setError("Please Enter post");

                                }
                            }
                        });
                        holder_video.iv_like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (postListData.get(position).getLikes().size() == 0) {
                                    callPostLoveApi(CommonKeys.UNLIKE, position, postListData);
                                } else {
                                    String action_like = "";
                                    List<Likes> likesList = postListData.get(position).getLikes();
                                    if (likesList.size() == 0) {
                                        action_like = CommonKeys.UNLIKE;
                                    } else {
                                        for (int i = 0; i < likesList.size(); i++) {
                                            if (user_id.equalsIgnoreCase(likesList.get(i).getMember_id())) {
                                                action_like = likesList.get(i).getFeedbacktype();
                                                break;
                                            } else {
                                                action_like = CommonKeys.UNLIKE;
                                            }
                                        }


                                    }

                                    callPostLoveApi(action_like, position, postListData);

                                }
                            }
                        });


                        holder_video.img_follow_unfollow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {

                                    if (holder_video.llEditDelete.getVisibility() == View.VISIBLE) {
                                        holder_video.llEditDelete.setVisibility(View.GONE);
                                        holder_video.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                    } else {
                                        holder_video.llEditDelete.setVisibility(View.VISIBLE);
                                        holder_video.img_follow_unfollow.setImageResource(R.drawable.up_arrow);

                                    }
                                } else {

                                    if (holder_video.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                        holder_video.llFollowUnfollow.setVisibility(View.GONE);
                                        holder_video.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                    } else {
                                        holder_video.img_follow_unfollow.setImageResource(R.drawable.up_arrow);

                                        holder_video.llFollowUnfollow.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        });


                        holder_video.tv_hidepost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openHidePostConfirmDialog(postListData, user_id, position);
                                if (holder_video.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_video.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_video.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        holder_video.tv_unfollowuser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openUnfollowConfirmDialog(postListData, user_id, position);
                                if (holder_video.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_video.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_video.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                        holder_video.tv_reportpost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openReportUserDialoge(postListData, position);
                                if (holder_video.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_video.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_video.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                        holder_video.tv_editpost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance(String.valueOf(postListData.get(position).getSocial_media().get(0).getFile()), "2", postListData.get(position).getCaption_title(), "EDIT", postListData.get(position).get_id(), String.valueOf(postListData.get(position).getSocial_media().get(0).getThumbnail()), String.valueOf(postListData.get(position).getSocial_media().get(0).get_id()), "video"), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            }
                        });
                        holder_video.tv_deletepost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callDeletePostApi(postListData, position);
                                if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {

                                    if (holder_video.llEditDelete.getVisibility() == View.VISIBLE) {
                                        holder_video.llEditDelete.setVisibility(View.GONE);
                                    } else {
                                        holder_video.llEditDelete.setVisibility(View.VISIBLE);
                                    }
                                } else {

                                    if (holder_video.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                        holder_video.llFollowUnfollow.setVisibility(View.GONE);
                                    } else {
                                        holder_video.llFollowUnfollow.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });


                    } else if ((postListData.get(position).getSocial_media().get(0).getMedia_type().equalsIgnoreCase("Link"))) {

                        /*======================================Link Preview Type==================================================*/
                        final ViewHolderPriviewLink holder_link_preview = (ViewHolderPriviewLink) holder;
                        holder_link_preview.ll_comment.setVisibility(View.GONE);
                        holder_link_preview.edt_comment.setHint("Reply to " + user_name + ":");
                        holder_link_preview.edt_comment.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

                        holder_link_preview.txvSiteName.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                        holder_link_preview.txvTitle.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_link_preview.txvlinkDescription.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

                        holder_link_preview.tv_like_total.setText("" + linkeCount);
                        holder_link_preview.tv_comment_total.setText("" + commentCount);
                        holder_link_preview.tv_time.setText("" + created_time);
                        holder_link_preview.tv_status.setText("" + caption_title);
                        holder_link_preview.tv_tag_line.setText("" + department);

                        holder_link_preview.tv_usre_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_link_preview.tv_tag_line.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_link_preview.tv_time.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_link_preview.tv_status.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
                        holder_link_preview.tv_comment_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_link_preview.tv_like_total.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
                        holder_link_preview.tv_post.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));

                        // need to set font
                        holder_link_preview.tv_hidepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_link_preview.tv_unfollowuser.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_link_preview.tv_reportpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_link_preview.tv_editpost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
                        holder_link_preview.tv_deletepost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));

                           /* if(action_like.equalsIgnoreCase(CommonKeys.LIKE)){
                                holder_link_preview.iv_like.setColorFilter(getContext().getResources().getColor(R.color.black));
                            }else {
                                holder_link_preview.iv_like.setColorFilter(getContext().getResources().getColor(R.color.transparent));
                            }*/

                        holder_link_preview.llFollowUnfollow.setVisibility(View.GONE);
                        holder_link_preview.llEditDelete.setVisibility(View.GONE);

                        if (Validation.isRequiredField(user_name)) {
                            holder_link_preview.tv_usre_name.setText(user_name + " /");
                        }
                        if (Validation.isRequiredField(user_profile)) {
                            holder_link_preview.user_pic.setImageURI(user_profile);
                            holder_link_preview.user_pic_comment.setImageURI(user_profile);

                        }
                        if (Validation.isRequiredField(caption_title)) {
                            holder_link_preview.tv_status.setText(caption_title);
                        }

                        Log.e("test", "link=>>" + postListData.get(position).getSocial_media().get(0).getFile());

                     /*   holder_link_preview.mPreview.destroyDrawingCache();
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder_link_preview.mPreview.setData(postListData.get(position).getSocial_media().get(0).getFile());
                                    }
                                });
                                return null;
                            }

                        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/

//                        holder_link_preview.mPreview.setClickable(true);
//                        holder_link_preview.mPreview.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                String uriUrl = postListData.get(position).getSocial_media().get(0).getFile();
//                              //  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
//                               // startActivity(launchBrowser);
//                                mActivity.pushFragmentDontIgnoreCurrent(WebviewFragment.getInstance(uriUrl),mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
//                                Intent launchBrowser = new Intent(mActivity,WebViewAtivity.class);
//                                launchBrowser.putExtra(CommonKeys.YOU_TUB_URL,uriUrl);
//                                startActivity(launchBrowser);
//                                //  mActivity.startActivity(new Intent(mActivity, FullVideoActivity.class).putExtra(CommonKeys.VIDEO_URL,postListData.get(position).getSocial_media().get(0).getFile()));
//                            }
//                        });
                        holder_link_preview.txvSiteName.setText(postListData.get(position).getSocial_media().get(0).getLink_host());
                        holder_link_preview.txvTitle.setText(postListData.get(position).getSocial_media().get(0).getLink_title());
                        holder_link_preview.txvlinkDescription.setText(postListData.get(position).getSocial_media().get(0).getLink_description());
                        holder_link_preview.linkImage.setImageURI(postListData.get(position).getSocial_media().get(0).getLink_image());

                        holder_link_preview.linkImage.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 String uriUrl = postListData.get(position).getSocial_media().get(0).getFile();
                                                                                 //  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                                                                 // startActivity(launchBrowser);
                                                                                 mActivity.pushFragmentDontIgnoreCurrent(WebviewFragment.getInstance(uriUrl), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                                                             }
                                                                         }
                        );

                        holder_link_preview.txvTitle.setOnClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 String uriUrl = postListData.get(position).getSocial_media().get(0).getFile();
                                                                                 //  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                                                                 // startActivity(launchBrowser);
                                                                                 mActivity.pushFragmentDontIgnoreCurrent(WebviewFragment.getInstance(uriUrl), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                                                             }
                                                                         }
                        );

                        holder_link_preview.txvlinkDescription.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                String uriUrl = postListData.get(position).getSocial_media().get(0).getFile();
                                                                                //  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                                                                // startActivity(launchBrowser);
                                                                                mActivity.pushFragmentDontIgnoreCurrent(WebviewFragment.getInstance(uriUrl), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                                                            }
                                                                        }
                        );

                        holder_link_preview.txvSiteName.setOnClickListener(new View.OnClickListener() {
                                                                                      @Override
                                                                                      public void onClick(View v) {
                                                                                          String uriUrl = postListData.get(position).getSocial_media().get(0).getFile();
                                                                                          //  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                                                                          // startActivity(launchBrowser);
                                                                                          mActivity.pushFragmentDontIgnoreCurrent(WebviewFragment.getInstance(uriUrl), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                                                                                      }
                                                                                  }
                        );

                        
                       /* holder_link_preview.mPreview.setListener(new Preview.PreviewListener() {
                            @Override
                            public void onDataReady(final Preview preview) {

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder_link_preview.cardViewPreview.setVisibility(View.VISIBLE);
                                        holder_link_preview.mPreview.setMessage(preview.getLink());
                                    }
                                });

                            }
                        });*/
                        holder_link_preview.iv_comment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Fragment findoggsFragment = SocialCommentFragment.newInstance(postListData.get(position).getComments(), postListData.get(position).get_id(), CommonKeys.MEDIAY_TYPE_LINK, position);
                                if (findoggsFragment != null) {
                                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.open_from_bottom, R.anim.close_from_top);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.add(R.id.fragment_container, findoggsFragment);
                                    fragmentTransaction.commit();
                                }

                            }
                        });
                        holder_link_preview.tv_post.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String comment_str = holder_link_preview.edt_comment.getText().toString().trim();
                                if (Validation.isRequiredField(comment_str)) {
                                    callpostCommentApi(position, videoList, comment_str);
                                    holder_link_preview.ll_comment.setVisibility(View.GONE);
                                    mActivity.hideKeyboard();
                                    holder_link_preview.edt_comment.setText("");
                                } else {
                                    holder_link_preview.edt_comment.setError("Please Enter post");
                                }
                            }
                        });
                        holder_link_preview.iv_like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (postListData.get(position).getLikes().size() == 0) {
                                    callPostLoveApi(CommonKeys.UNLIKE, position, postListData);
                                } else {
                                    String action_like = "";
                                    List<Likes> likesList = postListData.get(position).getLikes();
                                    if (likesList.size() == 0) {
                                        action_like = CommonKeys.UNLIKE;
                                    } else {
                                        for (int i = 0; i < likesList.size(); i++) {
                                            if (user_id.equalsIgnoreCase(likesList.get(i).getMember_id())) {
                                                action_like = likesList.get(i).getFeedbacktype();
                                                break;
                                            } else {
                                                action_like = CommonKeys.UNLIKE;
                                            }
                                        }


                                    }

                                    callPostLoveApi(action_like, position, postListData);

                                }
                            }
                        });

                        holder_link_preview.img_follow_unfollow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {

                                    if (holder_link_preview.llEditDelete.getVisibility() == View.VISIBLE) {
                                        holder_link_preview.llEditDelete.setVisibility(View.GONE);
                                        holder_link_preview.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                    } else {
                                        holder_link_preview.llEditDelete.setVisibility(View.VISIBLE);
                                        holder_link_preview.img_follow_unfollow.setImageResource(R.drawable.up_arrow);

                                    }
                                } else {
                                    if (holder_link_preview.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                        holder_link_preview.llFollowUnfollow.setVisibility(View.GONE);
                                        holder_link_preview.img_follow_unfollow.setImageResource(R.drawable.down_arrow);

                                    } else {
                                        holder_link_preview.img_follow_unfollow.setImageResource(R.drawable.up_arrow);

                                        holder_link_preview.llFollowUnfollow.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        });

                        holder_link_preview.tv_hidepost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openHidePostConfirmDialog(postListData, user_id, position);
                                if (holder_link_preview.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_link_preview.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_link_preview.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        holder_link_preview.tv_unfollowuser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openUnfollowConfirmDialog(postListData, user_id, position);
                                if (holder_link_preview.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_link_preview.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_link_preview.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }

                            }
                        });
                        holder_link_preview.tv_reportpost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openReportUserDialoge(postListData, position);
                                if (holder_link_preview.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                    holder_link_preview.llFollowUnfollow.setVisibility(View.GONE);
                                } else {
                                    holder_link_preview.llFollowUnfollow.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        holder_link_preview.tv_editpost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.pushFragmentDontIgnoreCurrent(UpLoadPostFragment.getInstance(String.valueOf(postListData.get(position).getSocial_media().get(0).getFile()), "4", postListData.get(position).getCaption_title(), "EDIT", postListData.get(position).get_id(), String.valueOf(postListData.get(position).getSocial_media().get(0).getThumbnail()), String.valueOf(postListData.get(position).getSocial_media().get(0).get_id()), ""), mActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
                            }
                        });
                        holder_link_preview.tv_deletepost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callDeletePostApi(postListData, position);
                                if (postListData.get(position).getMember_id().equalsIgnoreCase(user_id)) {
                                    if (holder_link_preview.llEditDelete.getVisibility() == View.VISIBLE) {
                                        holder_link_preview.llEditDelete.setVisibility(View.GONE);
                                    } else {
                                        holder_link_preview.llEditDelete.setVisibility(View.VISIBLE);
                                    }
                                } else {

                                    if (holder_link_preview.llFollowUnfollow.getVisibility() == View.VISIBLE) {
                                        holder_link_preview.llFollowUnfollow.setVisibility(View.GONE);
                                    } else {
                                        holder_link_preview.llFollowUnfollow.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                        });


                    } else {

                        /*======================================Load More ==================================================*/
                        final ViewHolderLoadling holderStatus = (ViewHolderLoadling) holder;
                        holderStatus.progressBar.setVisibility(View.VISIBLE);

                    }
                }

            }


        }

        @Override
        public int getItemCount() {
            return postListData.size();
        }

        public void updateData(List<SocialTimeline> videoList) {
            this.postListData = videoList;
            notifyDataSetChanged();
        }

        public void addList(List<SocialTimeline> videoList) {
            this.postListData = videoList;
        }

        public class ViewHolderImage extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_usre_name)
            TextView tv_usre_name;
            @BindView(R.id.tv_tag_line)
            TextView tv_tag_line;
            @BindView(R.id.tv_status)
            TextView tv_status;
            @BindView(R.id.tv_time)
            TextView tv_time;
            @BindView(R.id.user_pic)
            SimpleDraweeView user_pic;
            @BindView(R.id.tv_like_total)
            TextView tv_like_total;
            @BindView(R.id.tv_comment_total)
            TextView tv_comment_total;
            @BindView(R.id.im_share)
            ImageView im_share;
            /*   @BindView(R.id.iv_post_image)
               SimpleDraweeView iv_post_image;*/
            @BindView(R.id.ll_comment)
            LinearLayout ll_comment;
            @BindView(R.id.iv_comment)
            ImageView iv_comment;
            @BindView(R.id.iv_like)
            ImageView iv_like;
            @BindView(R.id.edt_comment)
            EditText edt_comment;
            @BindView(R.id.tv_post)
            TextView tv_post;
            @BindView(R.id.user_pic_comment)
            SimpleDraweeView user_pic_comment;

            @BindView(R.id.img1)
            SimpleDraweeView sdv_img1;

            @BindView(R.id.img2)
            SimpleDraweeView sdv_img2;

            @BindView(R.id.img3)
            SimpleDraweeView sdv_img3;

            @BindView(R.id.img4)
            SimpleDraweeView sdv_img4;

            @BindView(R.id.tv_more_images) // +1 images TextView --> only for all five images
                    TextView tv_more_images;

            @BindView(R.id.fl_single_image)
            FrameLayout fl_single_image;

            @BindView(R.id.sdv_single_image)
            SimpleDraweeView sdv_single_image;

            @BindView(R.id.tv_extra_images) // +4 images TextView --> for less than 5 images
                    TextView tv_extra_images;
            @BindView(R.id.percent_layout)
            PercentRelativeLayout percent_layout;
            @BindView(R.id.img_follow_unfollow)
            ImageView img_follow_unfollow;
            @BindView(R.id.llFollowUnfollow)
            LinearLayout llFollowUnfollow;
            @BindView(R.id.tv_hidepost)
            TextView tv_hidepost;
            @BindView(R.id.tv_unfollowuser)
            TextView tv_unfollowuser;
            @BindView(R.id.tv_reportpost)
            TextView tv_reportpost;
            @BindView(R.id.llEditDelete)
            LinearLayout llEditDelete;
            @BindView(R.id.tv_editpost)
            TextView tv_editpost;
            @BindView(R.id.tv_deletepost)
            TextView tv_deletepost;

            /* @BindView(R.id.tv_text)
             TextView tv_text;*/
            public ViewHolderImage(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public class ViewHolderStatus extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_usre_name)
            TextView tv_usre_name;
            @BindView(R.id.tv_tag_line)
            TextView tv_tag_line;
            @BindView(R.id.tv_status)
            TextView tv_status;
            @BindView(R.id.tv_time)
            TextView tv_time;
            @BindView(R.id.user_pic)
            SimpleDraweeView user_pic;
            @BindView(R.id.tv_like_total)
            TextView tv_like_total;
            @BindView(R.id.tv_comment_total)
            TextView tv_comment_total;
            @BindView(R.id.im_share)
            ImageView im_share;
            @BindView(R.id.ll_comment)
            LinearLayout ll_comment;
            @BindView(R.id.iv_comment)
            ImageView iv_comment;
            @BindView(R.id.edt_comment)
            EditText edt_comment;
            @BindView(R.id.tv_post)
            TextView tv_post;
            @BindView(R.id.iv_like)
            ImageView iv_like;
            @BindView(R.id.user_pic_comment)
            SimpleDraweeView user_pic_comment;
            @BindView(R.id.img_follow_unfollow)
            ImageView img_follow_unfollow;
            @BindView(R.id.tv_hidepost)
            TextView tv_hidepost;
            @BindView(R.id.tv_unfollowuser)
            TextView tv_unfollowuser;
            @BindView(R.id.tv_reportpost)
            TextView tv_reportpost;
            @BindView(R.id.llFollowUnfollow)
            LinearLayout llFollowUnfollow;
            @BindView(R.id.llEditDelete)
            LinearLayout llEditDelete;
            @BindView(R.id.tv_editpost)
            TextView tv_editpost;
            @BindView(R.id.tv_deletepost)
            TextView tv_deletepost;

            public ViewHolderStatus(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }

        public class ViewHolderPriviewLink extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_usre_name)
            TextView tv_usre_name;
            @BindView(R.id.tv_tag_line)
            TextView tv_tag_line;
            @BindView(R.id.tv_status)
            TextView tv_status;
            @BindView(R.id.tv_time)
            TextView tv_time;
            @BindView(R.id.user_pic)
            SimpleDraweeView user_pic;
            @BindView(R.id.user_pic_comment)
            SimpleDraweeView user_pic_comment;
            @BindView(R.id.tv_like_total)
            TextView tv_like_total;
            @BindView(R.id.tv_comment_total)
            TextView tv_comment_total;
            @BindView(R.id.im_share)
            ImageView im_share;
            @BindView(R.id.preview)
            Preview mPreview;
            @BindView(R.id.cardViewPreview)
            RelativeLayout cardViewPreview;
            @BindView(R.id.ll_comment)
            LinearLayout ll_comment;
            @BindView(R.id.iv_comment)
            ImageView iv_comment;
            @BindView(R.id.edt_comment)
            EditText edt_comment;
            @BindView(R.id.tv_post)
            TextView tv_post;
            @BindView(R.id.iv_like)
            ImageView iv_like;
            @BindView(R.id.img_follow_unfollow)
            ImageView img_follow_unfollow;
            @BindView(R.id.tv_hidepost)
            TextView tv_hidepost;
            @BindView(R.id.tv_unfollowuser)
            TextView tv_unfollowuser;
            @BindView(R.id.tv_reportpost)
            TextView tv_reportpost;
            @BindView(R.id.llFollowUnfollow)
            LinearLayout llFollowUnfollow;
            @BindView(R.id.llEditDelete)
            LinearLayout llEditDelete;
            @BindView(R.id.tv_editpost)
            TextView tv_editpost;
            @BindView(R.id.tv_deletepost)
            TextView tv_deletepost;
            @BindView(R.id.linkImage)
            SimpleDraweeView linkImage;
            @BindView(R.id.txvSiteName)
            TextView txvSiteName;
            @BindView(R.id.txvTitle)
            TextView txvTitle;
            @BindView(R.id.txvlinkDescription)
            TextView txvlinkDescription;
            @BindView(R.id.RLMainTop)
            RelativeLayout RLMainTop;

            public ViewHolderPriviewLink(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }

        public class ViewHolderVideo extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_usre_name)
            TextView tv_usre_name;
            @BindView(R.id.tv_tag_line)
            TextView tv_tag_line;
            @BindView(R.id.tv_status)
            TextView tv_status;
            @BindView(R.id.tv_time)
            TextView tv_time;
            @BindView(R.id.user_pic)
            SimpleDraweeView user_pic;
            @BindView(R.id.iv_preview)
            SimpleDraweeView iv_preview;
            @BindView(R.id.video_view)
            VideoView video_view;
            @BindView(R.id.bottom_seekbar)
            SeekBar bottom_seekbar;
            @BindView(R.id.bottom_time_current)
            TextView bottom_time_current;
            @BindView(R.id.bottom_end_time)
            TextView bottom_end_time;
            @BindView(R.id.tv_play_pause)
            TextView tv_play_pause;
            @BindView(R.id.progress_center)
            ProgressBar progress_center_video;

            @BindView(R.id.tv_like_total)
            TextView tv_like_total;
            @BindView(R.id.tv_comment_total)
            TextView tv_comment_total;
            @BindView(R.id.im_share)
            ImageView im_share;
            @BindView(R.id.ll_comment)
            LinearLayout ll_comment;
            @BindView(R.id.iv_comment)
            ImageView iv_comment;
            @BindView(R.id.edt_comment)
            EditText edt_comment;
            @BindView(R.id.tv_post)
            TextView tv_post;
            @BindView(R.id.rl_vv)
            RelativeLayout rl_vv;
            @BindView(R.id.layout_bottom)
            LinearLayout layout_bottom;
            @BindView(R.id.user_pic_comment)
            SimpleDraweeView user_pic_comment;
            @BindView(R.id.tv_click)
            TextView tv_click;
            @BindView(R.id.iv_like)
            ImageView iv_like;
            @BindView(R.id.img_follow_unfollow)
            ImageView img_follow_unfollow;
            @BindView(R.id.llFollowUnfollow)
            LinearLayout llFollowUnfollow;
            @BindView(R.id.tv_hidepost)
            TextView tv_hidepost;
            @BindView(R.id.tv_unfollowuser)
            TextView tv_unfollowuser;
            @BindView(R.id.tv_reportpost)
            TextView tv_reportpost;
            @BindView(R.id.llEditDelete)
            LinearLayout llEditDelete;
            @BindView(R.id.tv_editpost)
            TextView tv_editpost;
            @BindView(R.id.tv_deletepost)
            TextView tv_deletepost;

            public ViewHolderVideo(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }

        public class ViewHolderLoadling extends RecyclerView.ViewHolder {

            @BindView(R.id.progressBar)
            ProgressBar progressBar;

            public ViewHolderLoadling(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }

    }

    private void videoListCommonFunction(final int position, final PostAdaptor.ViewHolderVideo viewHolder) {
        if (videoList != null && !videoList.isEmpty()) {
            videoList.get(position);
            viewHolder.tv_click.setSelected(false);
            // viewHolder.sdv_image.setVisibility(View.GONE);

            final Uri uri = Uri.parse(videoList.get(position).getSocial_media().get(0).getFile());
            //thumnailiamge
            viewHolder.iv_preview.setImageURI(videoList.get(position).getSocial_media().get(0).getThumbnail());

            viewHolder.rl_vv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickPosition == position) {
                        if (isPlayingStatus.equalsIgnoreCase(CommonKeys.PREPARED) || isPlayingStatus.equalsIgnoreCase(CommonKeys.RESTARTED)) {

                            viewHolder.layout_bottom.setVisibility(View.VISIBLE);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewHolder.layout_bottom.setVisibility(View.GONE);

                                }
                            }, 2000);
                        }
                    }
                }
            });
            isPlayingStatus = CommonKeys.NOTSTARTED;

          /*  viewHolder.tv_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("test","click onclick button");
                }
            });*/
            viewHolder.tv_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!viewHolder.tv_click.isSelected()) {

                        // calling video view api.

                        viewHolder.video_view.setVisibility(View.VISIBLE);
                        //thumnailiamge
                        BitmapDrawable bitmap = getPreviewBitmap(videoList.get(position).getSocial_media().get(0).getThumbnail());
                        if (bitmap != null) {
                            viewHolder.video_view.setBackground(bitmap);
                        }
                        viewHolder.video_view.setVideoURI(uri);

                        if (previousPreview != null && tv_click != null && !isPlayingStatus.equalsIgnoreCase(CommonKeys.PAUSED)) {


                            if (isPlayingStatus.equalsIgnoreCase(CommonKeys.COMPLETED)) {
                                if (clickPosition == position) {
                                    previousPreview.setVisibility(View.GONE);
                                    previousView.setVisibility(View.VISIBLE);
                                } else {
                                    previousPreview.setImageURI(previewUrl);
                                    previousPreview.setVisibility(View.VISIBLE);
                                    tv_click.setVisibility(View.VISIBLE);
                                    previousView.stopPlayback();
                                    previousView.setVisibility(View.GONE);
                                    ll_btm.setVisibility(View.GONE);
                                }
                            }

                            if (isPlayingStatus.equalsIgnoreCase(CommonKeys.EXIT)) {

                                previousView.setVisibility(View.VISIBLE);
                                previousPreview.setVisibility(View.GONE);
                            } else {

                                previousPreview.setImageURI(previewUrl);
                                previousPreview.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                tv_click.setVisibility(View.VISIBLE);
                                previousView.stopPlayback();
                                previousView.setVisibility(View.GONE);
                                ll_btm.setVisibility(View.GONE);

                            }

                            if (!isPlayingStatus.equalsIgnoreCase(CommonKeys.EXIT)) {
                                isPlayingStatus = CommonKeys.NOTSTARTED;
                            }
                            tv_click.setSelected(false);
                            progressBar.setVisibility(View.GONE);

                            viewHolder.video_view.setVideoURI(uri);


                        } else if (isPlayingStatus.equalsIgnoreCase(CommonKeys.PAUSED)) {
                            if (clickPosition != position) {
                                isPlayingStatus = CommonKeys.NOTSTARTED;
                                previousPreview.setImageURI(previewUrl);
                                previousPreview.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                tv_click.setVisibility(View.VISIBLE);
                                previousView.stopPlayback();
                                previousView.setVisibility(View.GONE);
                                ll_btm.setVisibility(View.GONE);
                                tv_click.setSelected(false);
                                viewHolder.video_view.setVideoURI(uri);
                            }

                            progressBar.setVisibility(View.GONE);
                        }

                        previousView = viewHolder.video_view;
                        previousPreview = viewHolder.iv_preview;
                        tv_click = viewHolder.tv_click;
                        ll_btm = viewHolder.layout_bottom;

                        url = videoList.get(position).getSocial_media().get(0).getFile();
                        //thumnailiamge
                        previewUrl = videoList.get(position).getSocial_media().get(0).getThumbnail();
                        // name = videoList.get(position).getCaption();
                        progressBar = viewHolder.progress_center_video;

                        clickPosition = position;
                        viewHolder.bottom_seekbar.setMax(100);
                        viewHolder.bottom_seekbar.setProgress(0);
                        viewHolder.tv_click.setVisibility(View.GONE);

                        if (isPlayingStatus.equalsIgnoreCase(CommonKeys.PAUSED)) {
                            viewHolder.progress_center_video.setVisibility(View.VISIBLE);
                            viewHolder.video_view.seekTo(stopPosition);
                            viewHolder.bottom_seekbar.setProgress(Utilities.getProgressPercentage(stopPosition, viewHolder.video_view.getDuration()));
                            viewHolder.video_view.start();
                            isPlayingStatus = CommonKeys.RESTARTED;
                            isPlaying = true;
                            tv_click.setVisibility(View.VISIBLE);
                            viewHolder.progress_center_video.setVisibility(View.GONE);
                        } else if (isPlayingStatus.equalsIgnoreCase(CommonKeys.NOTSTARTED)) {
                            viewHolder.progress_center_video.setVisibility(View.VISIBLE);
                            viewHolder.video_view.start();
                            isPlayingStatus = CommonKeys.STARTED;
                            isPlaying = true;
                            seekBar = viewHolder.bottom_seekbar;
                            tv_current_time = viewHolder.bottom_time_current;
                            seekBarHandler.postDelayed(seekBarProgressRunnable, 100);
                        } else if (isPlayingStatus.equalsIgnoreCase(CommonKeys.EXIT)) {
                            viewHolder.progress_center_video.setVisibility(View.VISIBLE);
                            previousView.seekTo(stopPosition);
                            viewHolder.bottom_seekbar.setProgress(Utilities.getProgressPercentage(stopPosition, viewHolder.video_view.getDuration()));
                            viewHolder.video_view.start();
                            isPlayingStatus = CommonKeys.RESTARTED;
                            isPlaying = true;
                            tv_click.setVisibility(View.VISIBLE);
                            viewHolder.progress_center_video.setVisibility(View.GONE);

                        }

                        viewHolder.video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                viewHolder.iv_preview.setVisibility(View.GONE);
                                isPlayingStatus = CommonKeys.PREPARED;
                                viewHolder.video_view.setBackgroundColor(Color.TRANSPARENT);
                                viewHolder.progress_center_video.setVisibility(View.GONE);
                                viewHolder.tv_click.setVisibility(View.VISIBLE);

                                viewHolder.bottom_end_time.setText(getTime(viewHolder.video_view.getDuration()));
                            }

                        });

                        viewHolder.bottom_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                int totalDuration = viewHolder.video_view.getDuration();
                                int currentPosition = Utilities.progressToTimer(seekBar.getProgress(), totalDuration);
                                viewHolder.video_view.seekTo(currentPosition);
                                seekBarHandler.postDelayed(seekBarProgressRunnable, 100);
                            }
                        });
                        viewHolder.video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                Log.e("test", "Not play song ");
                                return false;
                            }
                        });

                        viewHolder.video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                isPlayingStatus = CommonKeys.COMPLETED;
                                viewHolder.video_view.setVisibility(View.GONE);
                                viewHolder.layout_bottom.setVisibility(View.GONE);
                                viewHolder.iv_preview.setImageURI(previewUrl);
                                viewHolder.iv_preview.setVisibility(View.VISIBLE);
                                //  viewHolder.tv_name.setVisibility(View.VISIBLE);
                                viewHolder.video_view.stopPlayback();
                                tv_click.setSelected(false);
                                resetStatics();
                            }
                        });
                        viewHolder.tv_click.setSelected(true);
                    } else {
                        viewHolder.video_view.pause();
                        stopPosition = viewHolder.video_view.getCurrentPosition();
                        isPlayingStatus = CommonKeys.PAUSED;
                        viewHolder.tv_click.setSelected(false);
                    }
                }
            });
        }
    }

    public void resetStatics() {
        previousPreview = new SimpleDraweeView(mActivity);
        previousView = new VideoView(mActivity);
        tv_click = new TextView(mActivity);
        tv_current_time = new TextView(mActivity);
        seekBar = new SeekBar(mActivity);
        tv_name = new TextView(mActivity);
        ll_btm = new LinearLayout(mActivity.getBaseContext());
        progressBar = new ProgressBar(mActivity.getBaseContext());
    }


    public static BitmapDrawable getPreviewBitmap(String url) {
        BitmapDrawable bitmapDrawable = null;
        try {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(url, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            Bitmap resize_bitmap = Bitmap.createScaledBitmap(thumb, Common_Methods.getDeviceWidth(mActivity), 250, false);
            bitmapDrawable = new BitmapDrawable(resize_bitmap);
            if (thumb != null) {
                thumb.recycle();
            }
            if (resize_bitmap != null) {
                resize_bitmap.recycle();
            }
        } catch (Exception e) {
            Log.e("test", "===>>>>>>MEMORY ALLOCATION ");
        }
        return bitmapDrawable;
    }

    public String getTime(long millis) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(millis));
        return time;
    }

    private void loadImagesFromArrayList(RecyclerView.ViewHolder vh3) {

        final PostAdaptor.ViewHolderImage vh = (PostAdaptor.ViewHolderImage) vh3;
        if (arrayListImage.size() == 1) {
            vh.percent_layout.setVisibility(View.GONE);
            vh.fl_single_image.setVisibility(View.VISIBLE);
            vh.tv_more_images.setVisibility(View.GONE);
            ImageUtils.loadImage(mContext, arrayListImage.get(0), vh.sdv_single_image);
            //arrayListImage.clear();
        } else if (arrayListImage.size() == 2) {

            vh.percent_layout.setVisibility(View.VISIBLE);
            vh.fl_single_image.setVisibility(View.GONE);
            vh.tv_more_images.setVisibility(View.GONE);

            PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) vh.sdv_img1.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
            info.widthPercent = 0.50f;
            info.heightPercent = 1.0f;
            vh.sdv_img1.requestLayout();

            PercentRelativeLayout.LayoutParams params1 = (PercentRelativeLayout.LayoutParams) vh.sdv_img2.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info1 = params1.getPercentLayoutInfo();
            info1.widthPercent = 0.50f;
            info1.heightPercent = 1.0f;
            vh.sdv_img2.requestLayout();

            ImageUtils.loadImage(mContext, arrayListImage.get(0), vh.sdv_img1);
            ImageUtils.loadImage(mContext, arrayListImage.get(1), vh.sdv_img2);
        } else if (arrayListImage.size() == 3) {

            vh.percent_layout.setVisibility(View.VISIBLE);
            vh.fl_single_image.setVisibility(View.GONE);
            vh.tv_more_images.setVisibility(View.GONE);

            PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) vh.sdv_img1.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
            info.widthPercent = 1.0f;
            info.heightPercent = 0.50f;
            vh.sdv_img1.requestLayout();

            PercentRelativeLayout.LayoutParams params1 = (PercentRelativeLayout.LayoutParams) vh.sdv_img2.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info1 = params1.getPercentLayoutInfo();
            info1.widthPercent = 0.50f;
            info1.heightPercent = 0.50f;
            vh.sdv_img2.requestLayout();

            PercentRelativeLayout.LayoutParams params2 = (PercentRelativeLayout.LayoutParams) vh.sdv_img3.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info2 = params2.getPercentLayoutInfo();
            info2.widthPercent = 0.50f;
            info2.heightPercent = 0.50f;
            vh.sdv_img3.requestLayout();

            ImageUtils.loadImage(mContext, arrayListImage.get(0), vh.sdv_img1);
            ImageUtils.loadImage(mContext, arrayListImage.get(1), vh.sdv_img2);
            ImageUtils.loadImage(mContext, arrayListImage.get(2), vh.sdv_img3);
        } else if (arrayListImage.size() == 4) {

            vh.percent_layout.setVisibility(View.VISIBLE);
            vh.fl_single_image.setVisibility(View.GONE);
            vh.tv_more_images.setVisibility(View.GONE);

            PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) vh.sdv_img1.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
            info.widthPercent = 0.50f;
            info.heightPercent = 0.50f;
            vh.sdv_img1.requestLayout();

            PercentRelativeLayout.LayoutParams params1 = (PercentRelativeLayout.LayoutParams) vh.sdv_img2.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info1 = params1.getPercentLayoutInfo();
            info1.widthPercent = 0.50f;
            info1.heightPercent = 0.50f;
            vh.sdv_img2.requestLayout();

            PercentRelativeLayout.LayoutParams params2 = (PercentRelativeLayout.LayoutParams) vh.sdv_img3.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info2 = params2.getPercentLayoutInfo();
            info2.widthPercent = 0.50f;
            info2.heightPercent = 0.50f;
            vh.sdv_img3.requestLayout();

            PercentRelativeLayout.LayoutParams params3 = (PercentRelativeLayout.LayoutParams) vh.sdv_img4.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info3 = params3.getPercentLayoutInfo();
            info3.widthPercent = 0.50f;
            info3.heightPercent = 0.50f;
            vh.sdv_img4.requestLayout();

            ImageUtils.loadImage(mContext, arrayListImage.get(0), vh.sdv_img1);
            ImageUtils.loadImage(mContext, arrayListImage.get(1), vh.sdv_img2);
            ImageUtils.loadImage(mContext, arrayListImage.get(2), vh.sdv_img3);
            ImageUtils.loadImage(mContext, arrayListImage.get(3), vh.sdv_img4);

        } else if (arrayListImage.size() >= 5) {

            vh.percent_layout.setVisibility(View.VISIBLE);
            vh.fl_single_image.setVisibility(View.GONE);

            PercentRelativeLayout.LayoutParams params = (PercentRelativeLayout.LayoutParams) vh.sdv_img1.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info = params.getPercentLayoutInfo();
            info.widthPercent = 0.45f;
            info.heightPercent = 0.50f;
            vh.sdv_img1.requestLayout();

            PercentRelativeLayout.LayoutParams params1 = (PercentRelativeLayout.LayoutParams) vh.sdv_img2.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info1 = params1.getPercentLayoutInfo();
            info1.widthPercent = 0.55f;
            info1.heightPercent = 0.50f;
            vh.sdv_img2.requestLayout();

            PercentRelativeLayout.LayoutParams params2 = (PercentRelativeLayout.LayoutParams) vh.sdv_img3.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info2 = params2.getPercentLayoutInfo();
            info2.widthPercent = 0.55f;
            info2.heightPercent = 0.50f;
            vh.sdv_img3.requestLayout();

            PercentRelativeLayout.LayoutParams params3 = (PercentRelativeLayout.LayoutParams) vh.sdv_img4.getLayoutParams();
            PercentLayoutHelper.PercentLayoutInfo info3 = params3.getPercentLayoutInfo();
            info3.widthPercent = 0.45f;
            info3.heightPercent = 0.50f;
            vh.sdv_img4.requestLayout();

            vh.tv_more_images.setVisibility(View.VISIBLE);
            int total_img = arrayListImage.size() - 4;
            vh.tv_more_images.setText("+" + total_img);

            ImageUtils.loadImage(mContext, arrayListImage.get(0), vh.sdv_img1);
            ImageUtils.loadImage(mContext, arrayListImage.get(1), vh.sdv_img2);
            ImageUtils.loadImage(mContext, arrayListImage.get(2), vh.sdv_img3);
            ImageUtils.loadImage(mContext, arrayListImage.get(3), vh.sdv_img4);

//                vh.tv_more_images.setVisibility(View.VISIBLE);
//                vh.tv_more_images.setText("+1");

            // arrayListImage.clear();
        }

    }

    private File createImageFile() throws IOException {
        mCurrentPhotoPath = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
        File direct = new File(Environment.getExternalStorageDirectory() + "/Aktivo");
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

    // private long mLastClickTime = 0;
    public void callPostLoveApi(final String action, final int pos, final List<SocialTimeline> list) {
        {
            /*try {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            String status = "";
            if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                status = CommonKeys.UNLIKE;
            } else {
                status = CommonKeys.LIKE;
            }
            //  Methods.isProgressShow(mActivity);
            List<Likes> likesList = list.get(pos).getLikes();
            if (likesList.size() == 0) {
                likesList = new ArrayList<>();
                likesList.add(new Likes());
                likesList.get(0).setFeedbacktype(status);
                likesList.get(0).setMember_id(user_id);
                Log.e("test", "finalStatus=>" + status);
            } else {
                boolean is_user_like_post = false;
                for (int i = 0; i < likesList.size(); i++) {
                    Log.e("test", "linke" + likesList.get(i).getMember_id());
                    if (user_id.equalsIgnoreCase(likesList.get(i).getMember_id())) {
                        likesList.get(i).setFeedbacktype(status);
                        is_user_like_post = true;
                        Log.e("test", "linke" + likesList.get(i).getMember_id());

                        break;
                    } else {

                    }

                }
                if (!is_user_like_post) {
                    Likes likes = new Likes();
                    likes.setMember_id(user_id);
                    likes.setFeedbacktype(status);
                    likesList.add(likes);

                }

            }
            list.get(pos).setLikes(likesList);

            list.get(pos).getSocial_media();

            RecyclerView.ViewHolder viewHolder = recyclview_post.findViewHolderForAdapterPosition(pos);

            if (list.get(pos).getSocial_media().size() == 0) {
                PostAdaptor.ViewHolderStatus viewHolderStatus = (PostAdaptor.ViewHolderStatus) viewHolder;
                if (viewHolderStatus != null) {
                    if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                        int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                        totla_like = totla_like - 1;
                        list.get(pos).setLikes_count(String.valueOf(totla_like));
                        viewHolderStatus.tv_like_total.setText("" + totla_like);
                    } else {
                        int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                        totla_like = totla_like + 1;
                        list.get(pos).setLikes_count(String.valueOf(totla_like));
                        viewHolderStatus.tv_like_total.setText("" + totla_like);
                    }
                }

            } else {

                String mediay_time = list.get(pos).getSocial_media().get(0).getMedia_type();

                switch (mediay_time) {

                    case CommonKeys.MEDIAY_TYPE_IMAGE:
                        PostAdaptor.ViewHolderImage viewHolderImage = (PostAdaptor.ViewHolderImage) viewHolder;
                        if (viewHolderImage != null) {
                            if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                                int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                totla_like = totla_like - 1;
                                viewHolderImage.tv_like_total.setText("" + totla_like);
                                list.get(pos).setLikes_count(String.valueOf(totla_like));
                            } else {
                                int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                totla_like = totla_like + 1;
                                viewHolderImage.tv_like_total.setText("" + totla_like);
                                list.get(pos).setLikes_count(String.valueOf(totla_like));

                            }
                        }
                        break;
                    case CommonKeys.MEDIAY_TYPE_VIDEO:
                        PostAdaptor.ViewHolderVideo viewHolderVideo = (PostAdaptor.ViewHolderVideo) viewHolder;

                        if (viewHolderVideo != null) {
                            if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                                int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                totla_like = totla_like - 1;
                                list.get(pos).setLikes_count(String.valueOf(totla_like));

                                viewHolderVideo.tv_like_total.setText("" + totla_like);
                            } else {
                                int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                totla_like = totla_like + 1;
                                list.get(pos).setLikes_count(String.valueOf(totla_like));
                                viewHolderVideo.tv_like_total.setText("" + totla_like);
                            }
                        }
                        break;
                    case CommonKeys.MEDIAY_TYPE_LINK:
                        PostAdaptor.ViewHolderPriviewLink viewHolderPriviewLink = (PostAdaptor.ViewHolderPriviewLink) viewHolder;
                        if (viewHolderPriviewLink != null) {
                            if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                                int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                totla_like = totla_like - 1;
                                list.get(pos).setLikes_count(String.valueOf(totla_like));
                                viewHolderPriviewLink.tv_like_total.setText("" + totla_like);
                            } else {
                                int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                totla_like = totla_like + 1;
                                list.get(pos).setLikes_count(String.valueOf(totla_like));
                                viewHolderPriviewLink.tv_like_total.setText("" + totla_like);
                            }
                        }
                        break;
                    default:
                        break;
                }

            }
            postAdaptor.addList(list);

            WebApiClient.getInstance(mActivity).getWebApi().callPostLikeUnLikeApi(user_id, list.get(pos).get_id(), status, "").enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) { /*{
                                List<Likes> likesList = list.get(pos).getLikes();
                                if (likesList.size() == 0) {
                                    likesList = new ArrayList<>();
                                    likesList.add(new Likes());
                                    likesList.get(0).setFeedbacktype(finalStatus);
                                    likesList.get(0).setMember_id(user_id);
                                    Log.e("test", "finalStatus=>" + finalStatus);
                                } else {
                                    for (int i = 0; i < likesList.size(); i++) {
                                        Log.e("test", "linke" + likesList.get(i).getMember_id());
                                        if (user_id.equalsIgnoreCase(likesList.get(i).getMember_id())) {
                                            likesList.get(i).setFeedbacktype(finalStatus);
                                            Log.e("test", "finalStatus=>" + finalStatus);
                                            Log.e("test", "likesList.get(i).get_id()=>" + likesList.get(i).get_id() + "user_id=>" + user_id);
                                        }
                                    }

                                }
                                list.get(pos).setLikes(likesList);
                                mActivity.showCroutonsMessage(mActivity, response.body().getMessage());

                                list.get(pos).getSocial_media();

                                RecyclerView.ViewHolder viewHolder = recyclview_post.findViewHolderForAdapterPosition(pos);

                                if (list.get(pos).getSocial_media().size() == 0) {
                                    PostAdaptor.ViewHolderStatus viewHolderStatus = (PostAdaptor.ViewHolderStatus) viewHolder;
                                    if (viewHolderStatus != null) {
                                        if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                                            int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                            totla_like = totla_like - 1;
                                            list.get(pos).setLikes_count(String.valueOf(totla_like));
                                            viewHolderStatus.tv_like_total.setText("" + totla_like);
                                        } else {
                                            int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                            totla_like = totla_like + 1;
                                            list.get(pos).setLikes_count(String.valueOf(totla_like));
                                            viewHolderStatus.tv_like_total.setText("" + totla_like);
                                        }
                                    }

                                } else {

                                    String mediay_time = list.get(pos).getSocial_media().get(0).getMedia_type();

                                    switch (mediay_time) {

                                        case CommonKeys.MEDIAY_TYPE_IMAGE:
                                            PostAdaptor.ViewHolderImage viewHolderImage = (PostAdaptor.ViewHolderImage) viewHolder;
                                            if (viewHolderImage != null) {
                                                if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                                                    int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                                    totla_like = totla_like - 1;
                                                    viewHolderImage.tv_like_total.setText("" + totla_like);
                                                    list.get(pos).setLikes_count(String.valueOf(totla_like));
                                                } else {
                                                    int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                                    totla_like = totla_like + 1;
                                                    viewHolderImage.tv_like_total.setText("" + totla_like);
                                                    list.get(pos).setLikes_count(String.valueOf(totla_like));

                                                }
                                            }
                                            break;
                                        case CommonKeys.MEDIAY_TYPE_VIDEO:
                                            PostAdaptor.ViewHolderVideo viewHolderVideo = (PostAdaptor.ViewHolderVideo) viewHolder;

                                            if (viewHolderVideo != null) {
                                                if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                                                    int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                                    totla_like = totla_like - 1;
                                                    list.get(pos).setLikes_count(String.valueOf(totla_like));

                                                    viewHolderVideo.tv_like_total.setText("" + totla_like);
                                                } else {
                                                    int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                                    totla_like = totla_like + 1;
                                                    list.get(pos).setLikes_count(String.valueOf(totla_like));
                                                    viewHolderVideo.tv_like_total.setText("" + totla_like);
                                                }
                                            }
                                            break;
                                        case CommonKeys.MEDIAY_TYPE_LINK:
                                            PostAdaptor.ViewHolderPriviewLink viewHolderPriviewLink = (PostAdaptor.ViewHolderPriviewLink) viewHolder;
                                            if (viewHolderPriviewLink != null) {
                                                if (action.equalsIgnoreCase(CommonKeys.LIKE)) {
                                                    int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                                    totla_like = totla_like - 1;
                                                    list.get(pos).setLikes_count(String.valueOf(totla_like));
                                                    viewHolderPriviewLink.tv_like_total.setText("" + totla_like);
                                                } else {
                                                    int totla_like = Integer.parseInt(list.get(pos).getLikes_count());
                                                    totla_like = totla_like + 1;
                                                    list.get(pos).setLikes_count(String.valueOf(totla_like));
                                                    viewHolderPriviewLink.tv_like_total.setText("" + totla_like);
                                                }
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }*/ /*else {
                               // mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                            }*/
                            }
                            // postAdaptor.addList(list);
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                    common_methods.setExceptionMessage(throwable, mActivity);
                }
            });

        }

    }

    // call API for comment

    public void callpostCommentApi(final int pos, final List<SocialTimeline> list, final String comment) {
        /*WebApiClient.getInstance(mActivity).getWebApi().callPostCommentApi(user_id, list.get(pos).get_id(), "Comment", comment).enqueue(new Callback<CommentResponseMain>() {
            @Override
            public void onResponse(Call<CommentResponseMain> call, Response<CommentResponseMain> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                           // Log.e("count", response.body().getData().get(0).getComments_count());

                            RecyclerView.ViewHolder viewHolder = recyclview_post.findViewHolderForAdapterPosition(pos);

                            if (list.get(pos).getSocial_media().size() == 0) {
                                PostAdaptor.ViewHolderStatus viewHolderStatus = (PostAdaptor.ViewHolderStatus) viewHolder;


                                if (viewHolderStatus != null) {
                                    viewHolderStatus.tv_comment_total.setText(response.body().getData().get(0).getComments_count());
                                }
                            } else {

                                String mediay_time = list.get(pos).getSocial_media().get(0).getMedia_type();
                                switch (mediay_time) {

                                    case CommonKeys.MEDIAY_TYPE_IMAGE:
                                        PostAdaptor.ViewHolderImage viewHolderImage = (PostAdaptor.ViewHolderImage) viewHolder;
                                        if (viewHolderImage != null) {

                                            viewHolderImage.tv_comment_total.setText(response.body().getData().get(0).getComments_count());

                                        }
                                        break;
                                    case CommonKeys.MEDIAY_TYPE_VIDEO:
                                        PostAdaptor.ViewHolderVideo viewHolderVideo = (PostAdaptor.ViewHolderVideo) viewHolder;

                                        if (viewHolderVideo != null) {


                                            viewHolderVideo.tv_comment_total.setText(response.body().getData().get(0).getComments_count());

                                        }
                                        break;
                                    case CommonKeys.MEDIAY_TYPE_LINK:
                                        PostAdaptor.ViewHolderPriviewLink viewHolderPriviewLink = (PostAdaptor.ViewHolderPriviewLink) viewHolder;
                                        if (viewHolderPriviewLink != null) {

                                            viewHolderPriviewLink.tv_comment_total.setText(response.body().getData().get(0).getComments_count());

                                        }
                                        break;
                                    default:
                                        break;
                                }

                            }
                            list.get(pos).setComments_count(response.body().getData().get(0).getComments_count());
                            postAdaptor.addList(list);
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<CommentResponseMain> call, Throwable throwable) {
                common_methods.setExceptionMessage(throwable, mActivity);
            }
        });*/
    }


    public void callDeletePostApi(final List<SocialTimeline> list, final int position) {
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            methods.isProgressShow(mActivity);
            WebApiClient.getInstance(mActivity).getWebApi().DeletePost(list.get(position).get_id()).enqueue(new Callback<BaseCommanRespons>() {
                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                list.remove(position);
                                postAdaptor.updateData(list);
                            }
                        }
                    }
                    methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                    common_methods.setExceptionMessage(throwable, mActivity);
                    methods.isProgressHide();
                }
            });
        }
    }

    // Hide Post

    public void callHidePostApi(final List<SocialTimeline> list, final String user_id, final int position) {

        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            methods.isProgressShow(mActivity);
            WebApiClient.getInstance(mActivity).getWebApi().HidePost(user_id, list.get(position).get_id(), "Hide").enqueue(new Callback<BaseCommanRespons>() {

                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                    Log.e("get social id", list.get(position).get_id());
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                list.remove(position);
                                postAdaptor.updateData(list);
                            }
                        }
                    }
                    methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                    common_methods.setExceptionMessage(throwable, mActivity);
                    methods.isProgressHide();

                }
            });
        }
    }

    // Unfollow User
    public void callUnfollowuserApi(final List<SocialTimeline> list, final String user_id, final int position) {
        if (ConnectionUtil.isInternetAvailable(mActivity)) {
            methods.isProgressShow(mActivity);
            WebApiClient.getInstance(mActivity).getWebApi().UnfollowUser(user_id, list.get(position).getMember_id(), "Unfollow").enqueue(new Callback<BaseCommanRespons>() {

                @Override
                public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                    if (response != null) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                                callSocialTimeLineApi(1, true, false);

                            }
                        }
                    }
                    methods.isProgressHide();
                }

                @Override
                public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                    common_methods.setExceptionMessage(throwable, mActivity);
                    methods.isProgressHide();

                }
            });
        }
    }

    public void openReportUserDialoge(final List<SocialTimeline> list, final int position) {
        dialogBuilder = new Dialog(getActivity());
        dialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBuilder.setContentView(R.layout.dilaoge_report_user);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView recyclview_report_user = (RecyclerView) dialogBuilder.findViewById(R.id.recyclview_report_user);
        final LinearLayout llReason = (LinearLayout) dialogBuilder.findViewById(R.id.llReasonReport);
        final LinearLayout llActiontaken = (LinearLayout) dialogBuilder.findViewById(R.id.llActionTaken);
        TextView tv_actiontaken = (TextView) dialogBuilder.findViewById(R.id.tv_action);
        TextView tv_actionlike = (TextView) dialogBuilder.findViewById(R.id.tv_actionlike);
        TextView tv_hideost = (TextView) dialogBuilder.findViewById(R.id.tv_hidethispost);
        TextView tv_Unfollow = (TextView) dialogBuilder.findViewById(R.id.tv_unfollow);
        TextView tv_personalfeedback = (TextView) dialogBuilder.findViewById(R.id.tv_personalfeedback);
        TextView tv_persoalnote = (TextView) dialogBuilder.findViewById(R.id.tv_personalnote);
        final EditText edtcomment = (EditText) dialogBuilder.findViewById(R.id.edtpersonalcomment);
        TextView tv_thanksnote = (TextView) dialogBuilder.findViewById(R.id.tv_thanksnote);
        TextView btnActionback = (TextView) dialogBuilder.findViewById(R.id.btn_actionback);
        TextView btnActionReport = (TextView) dialogBuilder.findViewById(R.id.btn_actionreport);
        final ImageView imgHidepost = (ImageView) dialogBuilder.findViewById(R.id.imghidepost);
        final ImageView imgunfollowpost = (ImageView) dialogBuilder.findViewById(R.id.imgunfollowpost);
        TextView btncancel = (TextView) dialogBuilder.findViewById(R.id.btnCancle);
        TextView btnnext = (TextView) dialogBuilder.findViewById(R.id.btnNext);
        TextView tvreson = (TextView) dialogBuilder.findViewById(R.id.tv_reasonforthis);
        TextView tvthisreport = (TextView) dialogBuilder.findViewById(R.id.tv_whythispost);

        // need to set font

        tvreson.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tvthisreport.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_actiontaken.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_actionlike.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_hideost.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_Unfollow.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

        tv_personalfeedback.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_persoalnote.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        edtcomment.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_thanksnote.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        btnActionback.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        btncancel.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        btnnext.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        btnActionReport.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));


        listreport = new ArrayList<>();
        listreportstatus = new ArrayList<>();
        listreport.add("Offensive/Foul language");
        listreport.add("Sexually explicit image/video");
        listreport.add("Violent content");
        listreport.add("Racially provoked content");
        listreport.add("Spam");
        listreport.add("Others");

        listreportstatus.add("true");
        listreportstatus.add("false");
        listreportstatus.add("false");
        listreportstatus.add("false");
        listreportstatus.add("false");
        listreportstatus.add("false");


        reportAdaptor = new ReportUserAdaptor(listreport, listreportstatus);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        recyclview_report_user.setLayoutManager(mLayoutManager);
        recyclview_report_user.setItemAnimator(new DefaultItemAnimator());
        recyclview_report_user.setAdapter(reportAdaptor);
        //recyclview_hobby.setNestedScrollingEnabled(false);

        dialogBuilder.show();

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llReason.setVisibility(View.GONE);
                llActiontaken.setVisibility(View.VISIBLE);
            }
        });

        btnActionback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llReason.setVisibility(View.VISIBLE);
                llActiontaken.setVisibility(View.GONE);
            }
        });

        imgHidepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgunfollowpost.setImageResource(R.drawable.radio_white);
                imgHidepost.setImageResource(R.drawable.radio_black);
                selectedsubcategory = "Hide this post";
            }
        });
        imgunfollowpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgunfollowpost.setImageResource(R.drawable.radio_black);
                imgHidepost.setImageResource(R.drawable.radio_white);
                selectedsubcategory = "Unfollow this user";
            }
        });
        btnActionReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtil.isInternetAvailable(mActivity)) {
                    Reportuser(list, user_id, position, selectedcategory, selectedsubcategory, edtcomment.getText().toString());
                }
            }
        });

    }

    // for report user
    public class ReportUserAdaptor extends RecyclerView.Adapter<ReportUserAdaptor.MyViewHolder> {

        private ArrayList<String> listreport;
        private ArrayList<String> listreportstatus;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img)
            ImageView img;
            @BindView(R.id.tv_name)
            TextView tv_name;

            // need to set font


            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);

            }
        }

        public ReportUserAdaptor(ArrayList<String> listreport, ArrayList<String> listreportstatus) {

            this.listreport = listreport;
            this.listreportstatus = listreportstatus;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_report_user, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int pos) {
            //  holder.tv_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
            final int position = pos;

            // need to set font
            holder.tv_name.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));

            if (listreport.get(position) != null) {
                holder.tv_name.setText(listreport.get(position));

                if (listreportstatus.get(position).equalsIgnoreCase("true")) {
                    holder.img.setImageResource(R.drawable.radio_black);
                    selectedcategory = listreport.get(position);
                    Log.e("get selected category", selectedcategory);
                } else {
                    holder.img.setImageResource(R.drawable.radio_white);

                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listreportstatus.size(); i++) {
                        if (i == position) {
                            listreportstatus.set(i, "true");
                        } else {
                            listreportstatus.set(i, "false");

                        }
                    }
                    notifyDataSetChanged();
                }

            });


        }


        @Override
        public int getItemCount() {
            return listreport.size();
        }
    }


    // Report User
    public void Reportuser(final List<SocialTimeline> list, final String user_id, final int position, final String category, final String subcategory, final String commnet) {
        methods.isProgressShow(mActivity);
        dialogBuilder.dismiss();
        WebApiClient.getInstance(mActivity).getWebApi().ReportUser(user_id, list.get(position).getMember_id(), category, subcategory, commnet, "Report").enqueue(new Callback<BaseCommanRespons>() {

            @Override
            public void onResponse(Call<BaseCommanRespons> call, Response<BaseCommanRespons> response) {
                Log.e("get all value", user_id + "::" + list.get(position).getMember_id() + "::" + category + "::" + subcategory + "::" + commnet);
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            callSocialTimeLineApi(1, true, false);
                        }
                    }
                }
                methods.isProgressHide();
            }

            @Override
            public void onFailure(Call<BaseCommanRespons> call, Throwable throwable) {
                common_methods.setExceptionMessage(throwable, mActivity);
                methods.isProgressHide();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("test", "on Destory call==>>");
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(broadcastReceiver_commentCountupdate);

    }

    private void openUnfollowConfirmDialog(final List<SocialTimeline> postListData, final String user_id, final int position) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comman_confirm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_yes = (TextView) dialog.findViewById(R.id.yes);
        TextView tv_cancle = (TextView) dialog.findViewById(R.id.cancle);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        tv_header.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_yes.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_cancle.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_header.setText("Are you sure you want to unfollow this user?");
      /*  camera_btn.setText(""+languageTable.getFROM_CAMERA());
        galary.setText(""+languageTable.getFROM_GALLERY());
        tv_header.setText(""+languageTable.getSELECT_OPTION());*/
//        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUnfollowuserApi(postListData, user_id, position);
                dialog.dismiss();
            }
        });

    }

    private void openHidePostConfirmDialog(final List<SocialTimeline> postListData, final String user_id, final int position) {
        final Dialog dialog = new Dialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comman_confirm);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

        TextView tv_yes = (TextView) dialog.findViewById(R.id.yes);
        TextView tv_cancle = (TextView) dialog.findViewById(R.id.cancle);
        TextView tv_header = (TextView) dialog.findViewById(R.id.tv_header);
        tv_header.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_yes.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_cancle.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_header.setText("Are you sure you want to hide this post?");
      /*  camera_btn.setText(""+languageTable.getFROM_CAMERA());
        galary.setText(""+languageTable.getFROM_GALLERY());
        tv_header.setText(""+languageTable.getSELECT_OPTION());*/
//        ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                callHidePostApi(postListData, user_id, position);
            }
        });

    }

    boolean is_visible_fragment = true;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        is_visible_fragment = false;
        if (socialTimeLineReponseCall != null) {
            socialTimeLineReponseCall.cancel();
        }
        //  unbinder.unbind();
    }

}
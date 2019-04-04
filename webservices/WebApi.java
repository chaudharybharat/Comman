package com.aktivo.webservices;


import com.aktivo.response.BaseCommanRespons;
import com.aktivo.response.CommentResponse;
import com.aktivo.response.CommentResponseMain;
import com.aktivo.response.CompeteReponse;
import com.aktivo.response.ConnectedTypeResponse;
import com.aktivo.response.DymmyDate;
import com.aktivo.response.ExrciseResponse;
import com.aktivo.response.MarketUrlResponse;
import com.aktivo.response.MyStatsResponse;
import com.aktivo.response.PostCMSReponse;
import com.aktivo.response.ProfileResponse;
import com.aktivo.response.SinginResponse;
import com.aktivo.response.SocialTimeLineReponse;
import com.aktivo.response.SplashTutorailResponse;
import com.aktivo.response.TodayYouHaveResponse;
import com.aktivo.response.ValenceVoiceRecoderReponse;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebApi {
    // public static final String BASE_URL = "http://aktivolabs.coderspreview.com:1338/";

    // public static final String BASE_URL = "http://ec2-13-229-132-152.ap-southeast-1.compute.amazonaws.com:1337/";

    public static final String BASE_URL = "http://13.250.55.187:1340/";

    public static final String EMIAL = "email";
    public static final String OTP = "otp";
    public static final String MEMBER_ID = "member_id";

    public static final String TEXT = "text";
    public static final String PLATFORM_TYPE = "platform_type";
    public static final String IMAGE_0 = "image_0";
    public static final String IMAGE_1 = "image_1";
    public static final String VIDEO_0 = "video_0";
    public static final String LINK = "link";
    public static final String PAGE = "page";
    public static final String SOCIAL_ID = "social_id";
    public static final String FEEDBACK_TYPE = "feedback_type";
    public static final String COMMENT = "comment";
    public static final String AUDIO_0 = "audio_0";
    public static final String WEEK = "week";
    public static final String YEAR = "year";
    public static final String TYPE = "type";
    public static final String JSONINFO = "jsonInfo";
    public static final String PLATFORM_CONNECTED_DATETIME = "platform_connected_datetime";
    public static final String SOCIAL_MEDIA_ID = "social_media_id";
    public static final String FIRSTNAME = "firstname";
    public static final String LASTNAME = "lastname";
    public static final String SEX = "sex";
    public static final String PHOTO = "photo";
    public static final String PHONE = "phone";
    public static final String DATE_OF_BIRTH = "date_of_birth";
    public static final String HEIGHT = "height";
    public static final String HEIGHT_UNIT = "height_unit";
    public static final String WEIGHT = "weight";
    public static final String WEIGHT_UNIT = "weight_unit";
    public static final String SMOKES = "smokes";
    public static final String DRINKS = "drinks";
    public static final String BADTIME = "badtime";
    public static final String WAKEUP = "wakeup";
    public static final String AREA_OF_INTEREST = "area_of_interest";

    public static final String ACTION_TAKEN_MEMBER_ID = "action_taken_member_id";
    public static final String SOCIAL_TIMELINE_ID = "social_timeline_id";
    public static final String CATEGORY = "report_category";
    public static final String SUBCATEGORY = "report_subcategory";
    public static final String REPORTMESSAGE = "report_message";

    public static final String DEVICE_ID = "device_id";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_TYPE = "device_type";
    public static final String DEVICE_TOKEN = "device_token";

    @POST(BASE_URL + "/v2/5185415ba171ea3a00704eed")
    Call<DymmyDate> callLoginApi();

    @POST(BASE_URL + "postFlashScreen")
    Call<SplashTutorailResponse> callSplashTutorialApi();


    @POST(BASE_URL + "postCMS")
    Call<PostCMSReponse> callPostCMSApi();

    @FormUrlEncoded
    @POST(BASE_URL + "postSignIn")
    Call<BaseCommanRespons> callSignInApi(@Field(EMIAL) String tag);

    @FormUrlEncoded
    @POST(BASE_URL + "postVerifyOTP")
    Call<SinginResponse> callSignInWihtOtpApi(@Field(EMIAL) String tag,
                                              @Field(OTP) String otp_value);

    @FormUrlEncoded
    @POST(BASE_URL + "postCompete")
    Call<CompeteReponse> callCompeteApi(@Field(MEMBER_ID) String member_id);

    @FormUrlEncoded
    @POST(BASE_URL + "postListSocial")
    Call<SocialTimeLineReponse> callSocialTimeLneApi(@Field(MEMBER_ID) String member_id,
                                                     @Field(PAGE) String page);

    @FormUrlEncoded
    @POST(BASE_URL + "postSocialFeedback ")
    Call<BaseCommanRespons> callPostLikeUnLikeApi(@Field(MEMBER_ID) String member_id,
                                                  @Field(SOCIAL_ID) String social_id,
                                                  @Field(FEEDBACK_TYPE) String facebook_type,
                                                  @Field(COMMENT) String commnet);


    /*  @Multipart
      @POST(BASE_URL)
      Call<BaseCommanRespons> callPostUploadImage(@Query(TEXT) String name,
                                                            @Query(IMAGE_0) String image);
  */
    @Multipart
    @POST(BASE_URL + "postSocial")
    Call<BaseCommanRespons> callPostUploadImageANDVideo(@Part(MEMBER_ID) RequestBody membar_id,
                                                        @Part(TEXT) RequestBody text,
                                                        @Part MultipartBody.Part file);


    @Multipart
    @POST(BASE_URL + "postRecordValenceScore")
    Call<ValenceVoiceRecoderReponse> callValenceRecoderApi(@Part(MEMBER_ID) RequestBody membar_id,
                                                           @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(BASE_URL + "postSocial")
    Call<BaseCommanRespons> callPostUploadStatus(@Field(MEMBER_ID) String membar_id,
                                                 @Field(TEXT) String text);

    @FormUrlEncoded
    @POST(BASE_URL + "postExercise")
    Call<ExrciseResponse> callExecirseApi(@Field(MEMBER_ID) String membar_id,
                                          @Field(YEAR) int year,
                                          @Field(WEEK) int week);

    @FormUrlEncoded
    @POST(BASE_URL + "postSocial")
    Call<BaseCommanRespons> callPostUploadLink(@Field(MEMBER_ID) String membar_id,
                                               @Field(TEXT) String text,
                                               @Field(LINK) String link);

    @FormUrlEncoded
    @POST(BASE_URL + "postAktivoScoreTodayYouHave")
    Call<TodayYouHaveResponse> callTodayYouHave(@Field(MEMBER_ID) String membar_id);

    // for post comment by Hitendra Zala
    @FormUrlEncoded
    @POST(BASE_URL + "postSocialFeedback")
    Call<CommentResponseMain> callPostCommentApi(@Field(MEMBER_ID) String member_id,
                                                 @Field(SOCIAL_ID) String social_id,
                                                 @Field(FEEDBACK_TYPE) String facebook_type,
                                                 @Field(COMMENT) String commnet);

    // for save google fit info
    @FormUrlEncoded
    @POST(BASE_URL + "postSaveValidicAHKGFData")
    Call<BaseCommanRespons> callSaveGoogleFitData(@Field(MEMBER_ID) String member_id,
                                                  @Field(TYPE) String type,
                                                  @Field(JSONINFO) String jsonInfo,
                                                  @Field(PLATFORM_CONNECTED_DATETIME) String device_connected);

    // for MarketURL
    @FormUrlEncoded
    @POST(BASE_URL + "postCreateValidicUser")
    Call<MarketUrlResponse> CreateValidicUser(@Field(MEMBER_ID) String member_id);

    // for callGenerateAktivoScoreApi
    @FormUrlEncoded
    @POST(BASE_URL + "postGenerateAktivoScore")
    Call<BaseCommanRespons> GenerateAktivoScore(@Field(MEMBER_ID) String member_id);

    // for PostAktivoMyState
    @FormUrlEncoded
    @POST(BASE_URL + "postAktivoMyStats")
    Call<MyStatsResponse> postAktivoMyStats(@Field(MEMBER_ID) String member_id,
                                            @Field(WEEK) int week,
                                            @Field(YEAR) int year,
                                            @Field(TYPE) String type);

    // for update profile
    @Multipart
    @POST(BASE_URL + "postUpdateProfile")
    Call<BaseCommanRespons> callUpdateProfileWithImage(@Part(MEMBER_ID) RequestBody membar_id,
                                                       @Part(FIRSTNAME) RequestBody firstname,
                                                       @Part(LASTNAME) RequestBody lastname,
                                                       @Part(SEX) RequestBody sex,
                                                       @Part(PHONE) RequestBody phone,
                                                       @Part(DATE_OF_BIRTH) RequestBody date_of_birth,
                                                       @Part(HEIGHT) RequestBody height,
                                                       @Part(WEIGHT) RequestBody weight,
                                                       @Part(WEIGHT_UNIT) RequestBody weight_unit,
                                                       @Part(HEIGHT_UNIT) RequestBody height_unit,
                                                       @Part(SMOKES) RequestBody smokes,
                                                       @Part(DRINKS) RequestBody drinks,
                                                       @Part(BADTIME) RequestBody badtime,
                                                       @Part(WAKEUP) RequestBody wakeup,
                                                       @Part(AREA_OF_INTEREST) RequestBody area_of_interest,
                                                       @Part MultipartBody.Part file);

    // for update profile data
    @FormUrlEncoded
    @POST(BASE_URL + "postUpdateProfile")
    Call<BaseCommanRespons> callUpdateProfile(@Field(MEMBER_ID) String membar_id,
                                              @Field(FIRSTNAME) String firstname,
                                              @Field(LASTNAME) String lastname,
                                              @Field(SEX) String sex,
                                              @Field(PHONE) String phone,
                                              @Field(DATE_OF_BIRTH) String date_of_birth,
                                              @Field(HEIGHT) String height,
                                              @Field(WEIGHT) String weight,
                                              @Field(WEIGHT_UNIT) String weight_unit,
                                              @Field(HEIGHT_UNIT) String height_unit,
                                              @Field(SMOKES) String smokes,
                                              @Field(DRINKS) String drinks,
                                              @Field(BADTIME) String badtime,
                                              @Field(WAKEUP) String wakeup,
                                              @Field(AREA_OF_INTEREST) String area_of_interest);

    // for Display Profile
    @FormUrlEncoded
    @POST(BASE_URL + "postDisplayProfile")
    Call<ProfileResponse> DisplayProfile(@Field(MEMBER_ID) String member_id);

    // for Delete Social Post
    @FormUrlEncoded
    @POST(BASE_URL + "postDeletePost")
    Call<BaseCommanRespons> DeletePost(@Field(SOCIAL_ID) String social_id);

    // for Hide Social Post
    @FormUrlEncoded
    @POST(BASE_URL + "postSocialTakeAction")
    Call<BaseCommanRespons> HidePost(@Field(ACTION_TAKEN_MEMBER_ID) String action_taken_member_id, @Field(SOCIAL_TIMELINE_ID) String social_timeline_id, @Field(TYPE) String type);

    // for unfollow user
    @FormUrlEncoded
    @POST(BASE_URL + "postSocialTakeAction")
    Call<BaseCommanRespons> UnfollowUser(@Field(ACTION_TAKEN_MEMBER_ID) String action_taken_member_id, @Field(MEMBER_ID) String member_id, @Field(TYPE) String type);

    // for report user
    @FormUrlEncoded
    @POST(BASE_URL + "postSocialTakeAction")
    Call<BaseCommanRespons> ReportUser(@Field(ACTION_TAKEN_MEMBER_ID) String action_taken_member_id, @Field(MEMBER_ID) String member_id, @Field(CATEGORY) String report_category,
                                       @Field(SUBCATEGORY) String report_subcategory, @Field(REPORTMESSAGE) String report_message, @Field(TYPE) String type);
// Edit image and video

    @Multipart
    @POST(BASE_URL + "postEditPost")
    Call<BaseCommanRespons> callEditImageVideoApi(@Part(SOCIAL_ID) RequestBody social_id,
                                                  @Part(TEXT) RequestBody text,
                                                  @Part MultipartBody.Part file);

//    @FormUrlEncoded
//    @POST(BASE_URL+"postEditPost")
//    Call<BaseCommanRespons> callEditPostUploadLink(@Field(MEMBER_ID) String membar_id,
//                                               @Field(TEXT) String text,
//                                               @Field(LINK) String link);

    @FormUrlEncoded
    @POST(BASE_URL + "postEditPost")
    Call<BaseCommanRespons> callPostEditStatus(@Field(SOCIAL_ID) String social_id,
                                               @Field(TEXT) String text);

    // Edit image and video when image is not updated

    @FormUrlEncoded
    @POST(BASE_URL + "postEditPost")
    Call<BaseCommanRespons> callwithoutEditImageVideoApi(@Field(SOCIAL_ID) String social_id,
                                                         @Field(TEXT) String text,
                                                         @Field(IMAGE_0) String image_0,
                                                         @Field(SOCIAL_MEDIA_ID) String social_media_id);

    @FormUrlEncoded
    @POST(BASE_URL + "postSavePlatformConnectivity")
    Call<ConnectedTypeResponse> callUserChangeConnectionType(@Field(MEMBER_ID) String member_id,
                                                             @Field(PLATFORM_TYPE) String platform_data_type,
                                                             @Field(PLATFORM_CONNECTED_DATETIME) String platform_connecte_time);

    ///Device Registeration
    @FormUrlEncoded
    @POST(BASE_URL + "postRegisterDevice")
    Call<BaseCommanRespons> callDeviceRegister(@Field(MEMBER_ID) String member_id,
                                               @Field(DEVICE_ID) String device_id,
                                               @Field(DEVICE_NAME) String device_name,
                                               @Field(DEVICE_TYPE) String device_type,
                                               @Field(DEVICE_TOKEN) String device_token);


}

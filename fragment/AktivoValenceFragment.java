package com.aktivo.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.aktivo.response.UserDetailTable;
import com.aktivo.response.ValenceVoiceRecoderReponse;
import com.aktivo.webservices.WebApi;
import com.aktivo.webservices.WebApiClient;
import com.commonmodule.mi.utils.Validation;

import java.io.File;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.AudioRecorderActivity;
import cafe.adriel.androidaudiorecorder.Util;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AktivoValenceFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.ll_analysing)
    LinearLayout ll_analysing;
    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.iv_circle)
    ImageView iv_circle;
    @BindView(R.id.ll_start)
    RelativeLayout ll_start;
    @BindView(R.id.ll_left_sec)
    LinearLayout ll_left_sec;
    @BindView(R.id.tv_left_min)
    TextView tv_left_min;
    @BindView(R.id.tv_result)
    TextView tv_result;
    @BindView(R.id.ll_result)
    LinearLayout ll_result;
    @BindView(R.id.tv_first)
    TextView tv_first;
    @BindView(R.id.tv_third)
    TextView tv_third;
    @BindView(R.id.tv_gotScore)
    TextView tv_gotScore;
    @BindView(R.id.tv_Score)
    TextView tv_Score;
    @BindView(R.id.tv_yourVoice)
    TextView tv_yourVoice;
    @BindView(R.id.tv_YourResult)
    TextView tv_YourResult;
    @BindView(R.id.llValenceText)
    LinearLayout llValenceText;
    @BindView(R.id.llDisplayScore)
    LinearLayout llDisplayScore;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_aktivo)
    TextView tv_aktivo;
    @BindView(R.id.tv_moodForHighestPoint)
    TextView tv_moodForHighestPoint;
    @BindView(R.id.tv_Deeper_analysis)
    TextView tv_Deeper_analysis;
    @BindView(R.id.tv_back)
    TextView tv_back;

    private boolean isRecording;
    Animation rotation;
    private Recorder recorder;
    private MediaPlayer player;
    String top_title = "";
    String bottom_title = "";
    String user_id = "";
    private int recorderSecondsElapsed;
    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final int REQUEST_INTERNET = 200;
    String recTitle;
    private Timer timer;
    private static final String AUDIO_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/aktivo_recorded_audio.wav";
    private String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
    CountDownTimer CountDownTimer;

    //    private String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
//    private AudioSource source = AudioSource.MIC;
//    private AudioChannel channel = AudioChannel.MONO;
//    private AudioSampleRate sampleRate = AudioSampleRate.HZ_8000;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.aktivo_valence_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHeader();
        top_title = "Find out how ‘positive’ or ‘negative’ you feel on just about anything that’s on your mind.<br><b style='font-weight:bold:font-size:50px'>Take about 15 seconds to record your thoughts!</b><br> You can also simply read the set sentences for a more general analysis";
        bottom_title = "Got something on your mind? For the most accurate analysis, relax and speak freely.Press the microphone button to start recording.";
        // top_title="<font color='black'>Please relax and take about 15 seconds to read the following sentence.<b style='font-weight:bold:font-size:50px'> Press the microphone button when you're ready!</b></font>";
        setFont();
        initComponet();
        mActivity.tagmanager("Positivity Screen", "positivity_view");

    }

    private void setFont() {
        tv_first.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_first.setText(Html.fromHtml(top_title));
        tv_third.setText(Html.fromHtml(bottom_title));
        tv_third.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Light));
        tv_start.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Medium));
        tv_left_min.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_gotScore.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_Score.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_yourVoice.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_Deeper_analysis.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        tv_YourResult.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_moodForHighestPoint.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_back.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
    }

    private void initComponet() {

        final UserDetailTable userDetail = UserDetailTable.getUserDetail();
        if (userDetail != null) {
            if (Validation.isRequiredField(userDetail.get_id())) {
                user_id = userDetail.get_id();
            }
        }

        rotation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);

    }

    private void setHeader() {
        mActivity.seletect_tab(CommonKeys.CLEAR_TAB);
        mActivity.setToolbarTopVisibility(false);
        mActivity.setToolbarBottomVisibility(true);
        tv_aktivo.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_Bold));
        tv_title.setTypeface(common_methods.getFont(mActivity, CommonKeys.Montserrat_ExtraLight));
        mActivity.enableDrawer();

    }

    @OnClick({R.id.tv_start, R.id.ll_start, R.id.tv_back, R.id.iv_menu, R.id.iv_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mActivity.onBackPressed();
                break;
            case R.id.ll_start:
            case R.id.tv_start:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_INTERNET);

                } else {
                    //  recordAudio(v);
                    Log.e("get start stop", tv_start.getText().toString());
                    if (tv_start.getText().toString().equalsIgnoreCase("STOP RECORDING")) {
                        tv_start.setText("START RECORDING");
                        tv_left_min.setText("");
                        stopRecording();
                        mActivity.tagmanager("Start Recording button", "positivity_start_rec_click");
                    } else {
                        ll_left_sec.setVisibility(View.VISIBLE);
                        iv_circle.startAnimation(rotation);
                        tv_start.setText("STOP RECORDING");
                        toggleRecording(null);
                        mActivity.tagmanager("Stop Recording button", "positivity_stop_rec_click");

                    }

                }
                //ll_start.setVisibility(View.GONE);

                break;
            case R.id.iv_menu:
                mActivity.openDrawer();
                break;
            case R.id.tv_back:
                mActivity.tagmanager("Return to Home button", "positivity_back_button_click");

                mActivity.onBackPressed();
              /*  mActivity.clearBackStack();
                mActivity.clearBackStackFragmets();
                mActivity.onselectTabPostion(4);
                mActivity.pushFragmentDontIgnoreCurrent(AktivoMyStatsFragment.getInstance(CommonKeys.POSITIVITY), mActivity.FRAGMENT_JUST_REPLACE);*/
                break;
            default:
                break;
        }
    }

    public void toggleRecording(View v) {
        stopPlaying();
        isRecording = false;
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    pauseRecording();
                } else {
                    resumeRecording();
                }
            }
        });
    }

    private void stopPlaying() {
        //   statusView.setText("");
        //   statusView.setVisibility(View.INVISIBLE);

        //   visualizerView.release();
//        if (visualizerHandler != null) {
//            visualizerHandler.stop();
//        }

        if (player != null) {
            try {
                player.stop();
                player.reset();
            } catch (Exception e) {
            }
        }

        stopTimer();
    }

    private void pauseRecording() {
        isRecording = false;
//        if (!isFinishing()) {
//            saveMenuItem.setVisible(true);
//        }
        //  statusView.setText(R.string.aar_paused);
        //    statusView.setVisibility(View.VISIBLE);
//        restartView.setVisibility(View.VISIBLE);
//        playView.setVisibility(View.VISIBLE);
//        recordView.setImageResource(cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_rec);
//        playView.setImageResource(cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_play);

        //  visualizerView.release();
//        if (visualizerHandler != null) {
//            visualizerHandler.stop();
//        }

        if (recorder != null) {
            recorder.pauseRecording();
        }

        stopTimer();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void resumeRecording() {
        isRecording = true;
//        saveMenuItem.setVisible(false);
        //   statusView.setText(R.string.aar_recording);
        //   statusView.setVisibility(View.VISIBLE);
//        restartView.setVisibility(View.INVISIBLE);
//        playView.setVisibility(View.INVISIBLE);
//        recordView.setImageResource(cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_pause);
//        playView.setImageResource(cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_play);

        //      visualizerHandler = new VisualizerHandler();
        //   visualizerView.linkTo(visualizerHandler);

        if (recorder == null) {
            //  timerView.setText("00:00:00");

            CountDownTimer = new CountDownTimer(16000, 1000) {

                public void onTick(long millisUntilFinished) {
                    Log.e("test", "millisUntilFinished=>>>" + millisUntilFinished / 1000);
                    tv_left_min.setText("" + millisUntilFinished / 1000 + " " + "sec left");
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    stopRecording();
                    Log.e("test", "Recodring Finish=>>>");
                    callRecodeApi();

                    //setResult(RESULT_OK);
                    //finish();
                }

            }.start();

            recorder = OmRecorder.wav(
                    new PullTransport.Default(Util.getMic(AudioSource.MIC, AudioChannel.MONO, AudioSampleRate.HZ_8000)),
                    new File(AUDIO_FILE_PATH));
        }
        recorder.resumeRecording();
        //  startTimer();
    }


    // Record Audio File
    public void recordAudio(View v) {

        AndroidAudioRecorder.with(AktivoValenceFragment.this)
                // Required
                .setFilePath(AUDIO_FILE_PATH)
                .setColor(ContextCompat.getColor(getActivity(), R.color.blue_light))
                .setRequestCode(REQUEST_RECORD_AUDIO)
                // Optional
                //.setSource(MediaRecorder.AudioSource.MIC)
                .setChannel(AudioChannel.MONO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)
                .setTitle(recTitle)
                // Start recording
                .recordFromFragment();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == mActivity.RESULT_OK) {
                Toast.makeText(getActivity(), "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
                Log.e("get path", AUDIO_FILE_PATH);
                callRecodeApi();
                //   UploadRecordingFile();
            } else if (resultCode == mActivity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Audio was not recorded", Toast.LENGTH_SHORT).show();
                mActivity.tagmanager("Recording Error popup", "positivity_rec_error");
            }
        }
    }

    public void callRecodeApi() {
        RequestBody user_idBody = RequestBody.create(MediaType.parse("text/plain"), user_id);

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/aktivo_recorded_audio.wav");
        if (!file.exists()) {
            Log.e("test", "file not found");
            return;
        }
        final MultipartBody.Part body;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData(WebApi.AUDIO_0, file.getAbsolutePath(), requestFile);

        } else {
            Log.e("test", "ERROR  ==>FILE NOT FOUND ");
            return;
        }
        if (body == null) {
            Log.e("test", "ERROR  ==> FILE BODY NULL");
            return;
            //if body found null it returns it's
        }
        ll_left_sec.setVisibility(View.INVISIBLE);
        ll_analysing.setVisibility(View.VISIBLE);
        ll_start.setVisibility(View.GONE);
        WebApiClient.getInstance(mActivity).getWebApi().callValenceRecoderApi(user_idBody, body).enqueue(new Callback<ValenceVoiceRecoderReponse>() {
            @Override
            public void onResponse(Call<ValenceVoiceRecoderReponse> call, Response<ValenceVoiceRecoderReponse> response) {
                if (response != null) {
                    if (response.body() != null) {
                        if (response.body().getStatus().equalsIgnoreCase(CommonKeys.SUCCESS)) {
                            // mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                            llDisplayScore.setVisibility(View.VISIBLE);

                            ll_result.setVisibility(View.VISIBLE);
                            String voice_result = response.body().getData().getMood();

                            String moodForHighestPoint = response.body().getData().getMoodForHighestPoint();
                            tv_moodForHighestPoint.setText("" + moodForHighestPoint);
                            tv_YourResult.setText(voice_result);
                            tv_Score.setText(response.body().getData().getValence_score());
                            llValenceText.setVisibility(View.GONE);
                            ll_start.setVisibility(View.GONE);
                            mActivity.tagmanager("Positivity Score Successful Display", "positivity_score_display_success");

                        } else {
                            ll_start.setVisibility(View.VISIBLE);
                            common_methods.setCutemDialogMessage(mActivity, response.body().getMessage());
                            // mActivity.showCroutonsMessage(mActivity, response.body().getMessage());
                            // Toast.makeText(mActivity, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            //  mActivity.tagmanager("Recording Error popup","positivity_rec_error");

                            mActivity.tagmanager("Positivity Score Error Display", "positivity_score_display_error");


                        }
                    }
                }
                ll_analysing.setVisibility(View.GONE);
                ll_left_sec.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ValenceVoiceRecoderReponse> call, Throwable t) {
                common_methods.setExceptionMessage(t, mActivity);
                ll_analysing.setVisibility(View.INVISIBLE);
                ll_result.setVisibility(View.VISIBLE);

            }
        });
    }

    public void stopRecording() {
        //  visualizerView.release();
//        if (visualizerHandler != null) {
//            visualizerHandler.stop();
//        }

        recorderSecondsElapsed = 0;
        if (recorder != null) {
            recorder.stopRecording();
            recorder = null;
        }

        stopTimer();
        CountDownTimer.cancel();
        tv_start.setText("START RECORDING");
        ll_left_sec.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHeader();
        }
    }
}

package com.aktivo.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aktivo.R;
import com.aktivo.Utils.CommonKeys;
import com.commonmodule.mi.utils.Validation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.delight.android.webview.AdvancedWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebviewFragment extends BaseFragment implements AdvancedWebView.Listener,View.OnClickListener{
   
    @BindView(R.id.webview)
    AdvancedWebView mWebView;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    @BindView(R.id.tv_close)
    TextView tv_close;
    public static final String LINK="link";
    public static WebviewFragment getInstance(String link){
        WebviewFragment webviewFragment=new WebviewFragment();
        Bundle bundle=new Bundle();
        bundle.putString(LINK,link);
        webviewFragment.setArguments(bundle);
        return webviewFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activty_webview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setHeader();
        tv_close.setTypeface(common_methods.getFont(mActivity,CommonKeys.Montserrat_Light));
        initComponet();
    }

    private void setHeader() {
        mActivity.setToolbarBottomVisibility(false);
          mActivity.setBackgroudnImage("http://www.publicdomainpictures.net/pictures/30000/velka/plain-white-background.jpg");
    }

    private void initComponet() {

        mWebView.setListener(getActivity(), this);
        mWebView.setGeolocationEnabled(false);
        mWebView.setMixedContentAllowed(true);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);

        Bundle bundle = getArguments();
        if(bundle!=null){
            if(bundle.containsKey(LINK)){
                String url=bundle.getString(LINK);
                if(Validation.isRequiredField(LINK)){
                    mWebView.addHttpHeader("X-Requested-With", "");
                    mWebView.loadUrl(url);
                }
            }
        }

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //Toast.makeText(mActivity, title, Toast.LENGTH_SHORT).show();
            }

        });


}

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }



    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String url) {
        progressbar.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Toast.makeText(mActivity, "onPageError(errorCode = "+errorCode+",  description = "+description+",  failingUrl = "+failingUrl+")", Toast.LENGTH_SHORT).show();
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        Toast.makeText(mActivity, "onDownloadRequested(url = "+url+",  suggestedFilename = "+suggestedFilename+",  mimeType = "+mimeType+",  contentLength = "+contentLength+",  contentDisposition = "+contentDisposition+",  userAgent = "+userAgent+")", Toast.LENGTH_LONG).show();
        progressbar.setVisibility(View.GONE);

		/*if (AdvancedWebView.handleDownload(this, url, suggestedFilename)) {
			// download successfully handled
		}
		else {
			// download couldn't be handled because user has disabled download manager app on the device
		}*/
    }

    @Override
    public void onExternalPageRequest(String url) {
        Toast.makeText(mActivity, "onExternalPageRequest(url = "+url+")", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_close)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_close:
                mActivity.onBackPressed();
                break;

            default:
                break;
        }
    }
}

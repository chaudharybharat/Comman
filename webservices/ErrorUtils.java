package com.aktivo.webservices;




import com.aktivo.MainActivity;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import retrofit2.Converter;
import retrofit2.Response;


public class ErrorUtils {

    public static MainActivity mActivity = null;

    public ErrorUtils(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    public static APIError parseError(Response<?> response) {
        Converter<okhttp3.ResponseBody, APIError> converter = WebApiClient.getInstance(mActivity).retrofit_new
                .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
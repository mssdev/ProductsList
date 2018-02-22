package com.walmartlabs.android.productlist.api;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class ApiError implements Parcelable {
    public static final int EMPTY_ERROR_CODE = -1;


    @Expose
    private Integer httpStatusCode;
    @Expose
    private String message;

    public ApiError() {

    }

    /***
     *
     * @return this is the http status code
     */
    public Integer getHttpStatusCode() {
        if (httpStatusCode == null) {
            httpStatusCode = Integer.valueOf(EMPTY_ERROR_CODE);
        }
        return httpStatusCode;
    }

    public String getmessage() {
        if (message == null) {
            message = "";
        }
        return message;
    }


    /***
     * parcelable
     */

    public static final Creator<ApiError> CREATOR = new Creator<ApiError>() {
        @Override
        public ApiError createFromParcel(final Parcel in) {
            return new ApiError(in);
        }

        @Override
        public ApiError[] newArray(final int size) {
            return new ApiError[size];
        }
    };

    public ApiError(Parcel in) {
        readFromParcel(in);
    }

    public ApiError(Integer httpStatusCode, String message, Boolean useServerMessage) {
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    private void readFromParcel(Parcel in) {
        httpStatusCode = (Integer) in.readValue(Integer.class.getClassLoader());
        message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(httpStatusCode);
        dest.writeString(message);
    }


}

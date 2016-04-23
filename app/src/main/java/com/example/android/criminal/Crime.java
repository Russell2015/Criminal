package com.example.android.criminal;

import android.text.format.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/14.
 */
public class Crime {
    private UUID mUUID;
    private String mTitle;
    private boolean mSolved;
    private Date mDate;
    private String mSuspectName;
    private String mSuspectId;
    private String mSuspectPhoneNumber;

    public Crime() {
        mUUID = UUID.randomUUID();
        mDate = new Date();
    }


    public Crime(UUID uuid){
        mUUID = uuid;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Boolean getSolved() {
        return mSolved;
    }

    public void setSolved(Boolean solved) {
        mSolved = solved;
    }

    public String getSuspectName() {
        return mSuspectName;
    }

    public void setSuspectName(String suspectName) {
        mSuspectName = suspectName;
    }

    public String getSuspectId() {
        return mSuspectId;
    }

    public void setSuspectId(String suspectId) {
        mSuspectId = suspectId;
    }

    public String getSuspectPhoneNumber() {
        return mSuspectPhoneNumber;
    }

    public void setSuspectPhoneNumber(String suspectPhoneNumber) {
        mSuspectPhoneNumber = suspectPhoneNumber;
    }

    public String getPhotoFileName(){
        return "IMG_" + getUUID() + ".jpg";
    }
}

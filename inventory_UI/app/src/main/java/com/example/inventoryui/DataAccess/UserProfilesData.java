package com.example.inventoryui.DataAccess;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.inventoryui.Models.UserProfile.UserProfile;

public class UserProfilesData extends BaseData<UserProfile, com.example.inventoryui.Models.UserProfile.IndexVM> {

    public UserProfilesData(@NonNull Application application) {
        super(application);
    }

    @Override
    protected Class getIndexVMClass() {
        return com.example.inventoryui.Models.UserProfile.IndexVM.class;
    }

    @Override
    protected Class EClass() {
        return UserProfile.class;
    }

    @Override
    String addToUrl() {
        return "/userprofiles";
    }
}

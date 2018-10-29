package com.example.a44323.studykotlin.model;

import com.example.a44323.studykotlin.bean.UserInfo;

import org.jetbrains.annotations.NotNull;

public class UserModel implements IUserModel{

    UserInfo userInfo;

    public UserModel(){

        userInfo = new UserInfo();
    }

    @Override
    public void setUserName(@NotNull String userName) {

        userInfo.setUserName(userName);
    }

    @Override
    public void setPassWord(@NotNull String passWord) {

        userInfo.setPassWord(passWord);
    }

    @NotNull
    @Override
    public UserInfo getUserInfo(@NotNull String userName) {

        return userInfo;
    }
}

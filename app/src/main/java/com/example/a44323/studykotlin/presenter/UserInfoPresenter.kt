package com.example.a44323.studykotlin.presenter

import com.example.a44323.studykotlin.model.IUserModel
import com.example.a44323.studykotlin.model.UserModel
import com.example.a44323.studykotlin.ui.activity.IUserView

class UserInfoPresenter(private val mUserView: IUserView) {

    private val mUserModel: IUserModel

    init {
        mUserModel = UserModel()
    }

    fun saveUser(userName: String, passWord: String) {

        mUserModel.setUserName(userName)
        mUserModel.setPassWord(passWord + "md5")

    }

    fun loadUserInfo(userName: String) {

        val userInfo = mUserModel.getUserInfo(userName)
        mUserView.setUserName(userInfo.userName!!)
        mUserView.serPassword(userInfo.passWord!!)

    }


}

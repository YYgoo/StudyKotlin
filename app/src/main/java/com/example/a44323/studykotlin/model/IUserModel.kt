package com.example.a44323.studykotlin.model

import com.example.a44323.studykotlin.bean.UserInfo

interface IUserModel{

    fun setUserName(userName:String)
    fun setPassWord(passWord:String)
    fun getUserInfo(userName: String):UserInfo
}
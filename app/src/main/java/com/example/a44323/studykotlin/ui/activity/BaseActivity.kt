package com.example.a44323.studykotlin.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import kotlin.reflect.KClass

open class BaseActivity :AppCompatActivity(){

    open var context:Activity? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        context = this
    }

    fun toNextActivity(context: Context, openClass:Class<Any>,bundle: Bundle?){

        var intent:Intent = Intent(context,openClass)
        if (bundle != null){
            intent.putExtras(bundle)
        }
        context!!.startActivity(intent)

    }
    fun toNextActivityResult(openClass:Class<Any>,requestCode:Int,bundle: Bundle?){

        var intent:Intent = Intent(context,openClass)
        if (bundle != null){
            intent.putExtras(bundle)
        }
        context!!.startActivityForResult(intent,requestCode)
    }
    fun setResultOk(bundle: Bundle){

        var intent:Intent = Intent()
        if (bundle != null) intent.putExtras(bundle)
        setResult(Activity.RESULT_OK,intent)
        finish()

    }
}
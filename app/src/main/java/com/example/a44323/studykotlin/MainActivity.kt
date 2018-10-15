package com.example.a44323.studykotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_id.setText("hello kotlin");

        but_id.setOnClickListener{
            but_id.text="按钮已经被点击了"
                toast("你点击了按钮！")
        }

        but_long_id.setOnLongClickListener { longToast("长按了按钮");true }
    }
}

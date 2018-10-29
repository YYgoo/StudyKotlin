package com.example.a44323.studykotlin.ui.activity

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.v7.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.a44323.studykotlin.Adapter.TestRecyclerAdapter
import com.example.a44323.studykotlin.R
import com.example.a44323.studykotlin.aidl.IBackService
import com.example.a44323.studykotlin.presenter.UserInfoPresenter
import com.example.a44323.studykotlin.service.BackService
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.util.*

class MainActivity : BaseActivity(), IUserView {

    var strA:String = "123"
    var i:Int = 0
    var imgUrl:String = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539708332669&di=fa56462fd4b39b7263dc1d905c132897&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2Fcc11728b4710b912892dbedac9fdfc0392452221.jpg"
    var gifUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539708601676&di=9a693642967b3baa00f475cbc569676b&imgtype=0&src=http%3A%2F%2Fwx4.sinaimg.cn%2Fmw690%2F006NPaNHgy1fms39y9oe3g30b408cn1d.gif"

    var mServiceIntent:Intent? = null

    var iBackService: IBackService? = null

    var myRecyclerView:RecyclerView? = null
    var mAdapter:TestRecyclerAdapter? = null

    var list:MutableList<String>? = null

    companion object {
        private val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        val mUserPresenter:UserInfoPresenter = UserInfoPresenter(context)
        tv_username.setText("hello kotlin")
        but_id.setOnClickListener{
//            changeBtnText(i++)
//            toast(strA.test())
            mUserPresenter.saveUser("fuckyou","sleep")
            Glide.with(context).load(gifUrl).asGif().placeholder(R.drawable.ic_launcher_background).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(image_baidu)
            toNextActivity(context,MPATestActivity::class.java as Class<Any>,null)
        }
        move_image.setOnClickListener {

            try {
                if (iBackService == null) {
                    toast("没有连接，可能是服务器已断开")
                } else {
                   var isSend:Boolean = iBackService!!.sendMessage("isConnect")
                    if(isSend)
                        toast("success")
                    else
                        toast("fail")

                }
            } catch (e: RemoteException ) {
                e.printStackTrace()
            }
        }

        but_long_id.setOnLongClickListener {
            mUserPresenter.loadUserInfo("fuckyou");true }
        mServiceIntent = Intent(this, BackService::class.java)

        list = mutableListOf<String>()
        for (i in 1..10){
            list!!.add("第"+i+"项")
        }
        mAdapter = TestRecyclerAdapter(list!!)

        var mLinearLayoutManager:LinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL)
        rv_move_delete.setLayoutManager(mLinearLayoutManager)
        rv_move_delete.adapter = mAdapter
        rv_move_delete.itemAnimator = DefaultItemAnimator()
        rv_move_delete.setOnclickListener { position -> removeItem(position) }

//        mAdapter!!.setItemClickListener(object : TestRecyclerAdapter.onItemClickListener {
//
//            override fun onItemClick(v: View, position: Int) {
//
//                tv_username.text = list.get(position)
//            }
//        })
//        mAdapter!!.setItemClickNew { position, v -> tv_username.text =  list.get(position)}

    }
    fun removeItem(position:Int){

        list!!.removeAt(position)
        mAdapter!!.removeItem(position)

    }
    override fun getId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserName(): String {
        return tv_username.text.toString()
    }

    override fun getPassWord(): String {
        return tv_password.text.toString()
    }

    override fun setUserName(userName: String) {
        tv_username.text = userName
    }

    override fun serPassword(passWord: String) {
        tv_password.text = passWord
    }

    fun changeBtnText(number:Int){

        if (number % 2 == 0){
            but_id.text="按钮已经被点击了"+number+"次了"
        }
    }

    fun String.test():String{
        strA = "测试成功 哇哈哈哈 睡觉"
        return strA;
    }

    fun testList(): ArrayList<Int> {
        val list = arrayListOf(1,2,3)
        for (i in list)
        {
            toast(i.toString())
        }
        return list
    }

    private val conn:ServiceConnection = object:ServiceConnection{

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

            iBackService = IBackService.Stub.asInterface(service)

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            iBackService = null

        }

    }

    override fun onStart() {
        super.onStart()
        bindService(mServiceIntent,conn, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver();

    }

    override fun onPause() {
        // 注销广播 最好在onPause上注销
        unregisterReceiver(mReceiver);
        // 注销服务
        unbindService(conn);
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun  registerReceiver(){

        val intentFilter = IntentFilter()
        intentFilter.addAction(BackService.HEART_BEAT_ACTION)
        intentFilter.addAction(BackService.MESSAGE_ACTION)
        registerReceiver(mReceiver, intentFilter)
    }

    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            // 消息广播
            if (action == BackService.MESSAGE_ACTION) {
                val stringExtra = intent.getStringExtra("message")
                toast(stringExtra)
            } else if (action == BackService.HEART_BEAT_ACTION) {// 心跳广播
                toast("正常心跳")
            }
        }
    }

}

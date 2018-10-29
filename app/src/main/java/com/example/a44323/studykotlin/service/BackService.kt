package com.example.a44323.studykotlin.service

import java.lang.ref.WeakReference
import java.net.Socket
import java.net.UnknownHostException
import java.util.Arrays

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.RemoteException
import android.util.Log

import com.example.a44323.studykotlin.aidl.IBackService
import java.io.*


class BackService : Service() {

    private var sendTime = 0L

    /** 弱引用 在引用对象的同时允许对垃圾对象进行回收   */
    private var mSocket: WeakReference<Socket>? = null

    private var mReadThread: ReadThread? = null

    private var input: BufferedReader? = null
    private var output: PrintWriter? = null
    private var content:String? = null

    val bm = "utf-8" //全局定义，以适应系统其他部分

    private val iBackService = object : IBackService.Stub() {
        @Throws(RemoteException::class)
        override fun sendMessage(message: String): Boolean {
            return sendMsg(message)
        }
    }

    // 发送心跳包
    private val mHandler = Handler()
    private val heartBeatRunnable = object : Runnable {
        override fun run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                val isSuccess = sendMsg("")// 就发送一个\r\n过去, 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    mHandler.removeCallbacks(this)
                    mReadThread!!.release()
                    releaseLastSocket(mSocket)
                    InitSocketThread().start()
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE)
        }
    }

    override fun onBind(arg0: Intent): IBinder? {
        return iBackService
    }

    override fun onCreate() {
        super.onCreate()
        InitSocketThread().start()
    }

    fun sendMsg(msg: String): Boolean {
        if (null == mSocket || null == mSocket!!.get()) {
            return false
        }
        val soc = mSocket!!.get()
        try {
            if (!soc!!.isClosed() && !soc!!.isOutputShutdown()) {


//                val os = soc!!.getOutputStream()
                val message = msg + "客户端 哈哈！"
//                os.write(message.toByteArray())
//                os.flush()

                output = PrintWriter(BufferedWriter(OutputStreamWriter(
                        soc.getOutputStream(), bm)), true)
                output!!.println(message)
                sendTime = System.currentTimeMillis()// 每次发送成功数据，就改一下最后成功发送的时间，节省心跳间隔时间
                Log.i(TAG, "发送成功的时间：$sendTime")
            } else {
                return false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    // 初始化socket
    @Throws(UnknownHostException::class, IOException::class)
    private fun initSocket() {
        val socket = Socket(HOST, PORT)
        mSocket = WeakReference(socket)
        mReadThread = ReadThread(socket)
        mReadThread!!.start()
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE)// 初始化成功后，就准备发送心跳包
    }

    // 释放socket
    private fun releaseLastSocket(mSocket: WeakReference<Socket>?) {
        var mSocket = mSocket
        try {
            if (null != mSocket) {
                var sk: Socket? = mSocket.get()
                if (null !=sk )
                {
                    if (!sk!!.isClosed) {
                    sk.close()
                }
                }

                sk = null
                mSocket = null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    internal inner class InitSocketThread : Thread() {
        override fun run() {
            super.run()
            try {
                initSocket()
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    inner class ReadThread(socket: Socket) : Thread() {
        private val mWeakSocket: WeakReference<Socket>
        private var isStart = true

        init {
            mWeakSocket = WeakReference(socket)
        }

        fun release() {
            isStart = false
            releaseLastSocket(mWeakSocket)
        }

        @SuppressLint("NewApi")
        override fun run() {
            super.run()
            val socket = mWeakSocket.get()
            if (null != socket) {
                try {
                    val inputStream = socket.getInputStream()
                    val buffer = ByteArray(1024 * 4)
//                    var length = inputStream.read(buffer)
                    input = BufferedReader(InputStreamReader(socket.getInputStream()))

                    content = input!!.readLine()
                    while (!socket.isClosed && !socket.isInputShutdown
                            && isStart && content!!.length > 0) {
//                        length = inputStream.read(buffer)
                        if (content!!.length > 0) {
//                            val message = String(Arrays.copyOf(buffer,
//                                    length)).trim { it <= ' ' }
                            Log.i(TAG, "收到服务器发送来的消息：$content")
                            // 收到服务器过来的消息，就通过Broadcast发送出去
                            if (content == "ok") {// 处理心跳回复
                                val intent = Intent(HEART_BEAT_ACTION)
                                sendBroadcast(intent)
                            } else {
                                // 其他消息回复
                                val intent = Intent(MESSAGE_ACTION)
                                intent.putExtra("message", content)
                                sendBroadcast(intent)
                            }
                        }
                        content = ""
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    companion object {
        private val TAG = "BackService"
        /** 心跳检测时间   */
        private val HEART_BEAT_RATE = (3 * 1000).toLong()
        /** 主机IP地址   */
        private val HOST = "10.0.2.2"
        /** 端口号   */
        val PORT = 1234
        /** 消息广播   */
        val MESSAGE_ACTION = "org.feng.message_ACTION"
        /** 心跳广播   */
        val HEART_BEAT_ACTION = "org.feng.heart_beat_ACTION"
    }
}

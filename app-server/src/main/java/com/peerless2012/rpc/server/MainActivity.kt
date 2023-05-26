package com.peerless2012.rpc.server

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.peerless2012.rpc.server.service.IRPCService
import com.peerless2012.rpc.server.service.RPCService
import com.peerless2012.rpc.server.service.RPCServiceBinder

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/26 14:44
 * @Version V1.0
 * @Description:
 */
class MainActivity : AppCompatActivity(), ServiceConnection {

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var infoText: TextView

    private lateinit var bindOrUnbindBtn: Button

    private var binded: Boolean = false

    private var service: IRPCService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        infoText = findViewById(R.id.grpc_info_text)
        bindOrUnbindBtn = findViewById(R.id.rpc_bind_or_unbind_service)
        startService(Intent(this, RPCService::class.java))
        bindOrUnbindService(bindOrUnbindBtn)
    }

    override fun onDestroy() {
        if (binded) {
            unbindService(this)
        }
        super.onDestroy()
    }

    fun bindOrUnbindService(view: View) {
        if (binded) {
            unbindService(this)
            onBindStatusUpdate(false)
        } else {
            val serviceIntent = Intent(this, RPCService::class.java)
            bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
            bindOrUnbindBtn.isEnabled = false
        }
    }

    private fun onInfoChanged(info: String) {
        handler.post {
            infoText.text = info
        }
    }

    override fun onServiceConnected(name: ComponentName, binder: IBinder?) {
        service = (binder as RPCServiceBinder).getService()
        service!!.setInfoListener(this::onInfoChanged)
        bindOrUnbindBtn.isEnabled = true
        onBindStatusUpdate(true)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        service = null
        bindOrUnbindBtn.isEnabled = true
        onBindStatusUpdate(false)
    }

    private fun onBindStatusUpdate(binded: Boolean) {
        this.binded = binded
        if (binded) {
            bindOrUnbindBtn.text = "Unbind RPC Service"
        } else {
            bindOrUnbindBtn.text = "Bind RPC Service"
        }
    }

}
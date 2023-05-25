package com.peerless2012.rpc

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/24 16:02
 * @Version V1.0
 * @Description
 */
class ServerService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
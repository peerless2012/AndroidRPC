package com.peerless2012.rpc.server.service

import android.os.Binder

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/26 16:22
 * @Version V1.0
 * @Description
 */
class RPCServiceBinder(private val service: IRPCService)
    : Binder()
    , IRPCServiceBinder {

    override fun getService(): IRPCService {
        return service
    }

}
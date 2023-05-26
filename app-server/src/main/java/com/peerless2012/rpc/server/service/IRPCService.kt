package com.peerless2012.rpc.server.service

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/26 16:23
 * @Version V1.0
 * @Description
 */
interface IRPCService {

    fun getInfo(): String

    fun setInfoListener(listener: ((String) -> Unit)?)

}
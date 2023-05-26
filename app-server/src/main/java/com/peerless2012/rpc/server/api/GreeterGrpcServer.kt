package com.peerless2012.rpc.server.api

import com.peerless2012.rpc.api.GreeterGrpc
import com.peerless2012.rpc.api.HelloReply
import com.peerless2012.rpc.api.HelloRequest
import com.peerless2012.rpc.server.service.IRPCService
import io.grpc.stub.StreamObserver
import java.lang.StringBuilder

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/26 15:32
 * @Version V1.0
 * @Description
 */
class GreeterGrpcServer: GreeterGrpc.GreeterImplBase(), IRPCService {

    companion object {

        private const val TAG_REQUEST = "\n--------------------  request --------------------\n"

        private const val TAG_RESPONSE = "\n--------------------  response --------------------\n"

        private const val TAG_END = "\n\n"

    }

    private var info: String = ""

    private var func: ((String)->Unit)? = null

    override fun sayHello(request: HelloRequest, responseObserver: StreamObserver<HelloReply>) {
        onRequest(request.name)

        val helloReply = HelloReply
            .newBuilder()
            .setMessage("Hello ${request.name}, Welcome to the RPC world!")
            .build()
        responseObserver.onNext(helloReply)
        responseObserver.onCompleted()

        onResponse(helloReply.message)
    }

    override fun getInfo(): String {
        return info
    }

    override fun setInfoListener(listener: ((String) -> Unit)?) {
        func = listener
        func?.also {
            it(info)
        }
    }

    private fun onRequest(request: String) {
        val infoBuilder = StringBuilder(info)
        infoBuilder.append(TAG_REQUEST)
            .append(request)
        info = infoBuilder.toString()
        func?.also {
            it(info)
        }
    }

    private fun onResponse(response: String) {
        val infoBuilder = StringBuilder(info)
        infoBuilder.append(TAG_RESPONSE)
            .append(response)
            .append(TAG_END)
        info = infoBuilder.toString()
        func?.also {
            it(info)
        }
    }
}
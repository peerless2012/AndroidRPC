package com.peerless2012.rpc.client

import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.peerless2012.rpc.api.GreeterGrpc
import com.peerless2012.rpc.api.HelloReply
import com.peerless2012.rpc.api.HelloRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/24 16:02
 * @Version V1.0
 * @Description:
 */
class MainActivity : AppCompatActivity() {

    private lateinit var sendButton: Button
    private lateinit var hostEdit: EditText
    private lateinit var portEdit: EditText
    private lateinit var messageEdit: EditText
    private lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sendButton = this.findViewById(R.id.send_button)
        hostEdit = findViewById(R.id.host_edit_text)
        portEdit = findViewById(R.id.port_edit_text)
        messageEdit = findViewById(R.id.message_edit_text)
        resultText = findViewById(R.id.grpc_response_text)
        resultText.movementMethod = ScrollingMovementMethod()
    }

    fun sendMessage(view: View) {
        MainScope().launch {
            val response: String
            withContext(Dispatchers.IO) {
                response = sayHello()
            }
            resultText.text = response
        }
    }

    private suspend fun sayHello(): String {
        val host: String = hostEdit.text.toString()
        val message: String = messageEdit.text.toString()
        val portStr: String = portEdit.text.toString()
        val port = if (TextUtils.isEmpty(portStr)) 0 else Integer.valueOf(portStr)
        try {
            val channel: ManagedChannel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build()
            val stub: GreeterGrpc.GreeterBlockingStub = GreeterGrpc.newBlockingStub(channel)
            val request: HelloRequest = HelloRequest.newBuilder().setName(message).build()
            val reply: HelloReply = stub.sayHello(request)
            return reply.message
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            pw.flush()
            return String.format("Failed... : %n%s", sw)
        }
    }

}
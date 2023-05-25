package com.peerless2012.rpc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.peerless2012.rpc.entity.HelloRequestOuterClass.HelloRequest
import java.io.File
import java.io.FileOutputStream

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/24 16:02
 * @Version V1.0
 * @Description:
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val helloRequest = HelloRequest.newBuilder().setHello("Hello").build()
        val fo = FileOutputStream(File(cacheDir, "Hello.proto"))
    }

}
package com.peerless2012.rpc.server.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.peerless2012.rpc.server.MainActivity
import com.peerless2012.rpc.server.R
import com.peerless2012.rpc.server.api.GreeterGrpcServer
import io.grpc.InsecureServerCredentials
import io.grpc.Server
import io.grpc.okhttp.OkHttpServerBuilder
import java.util.concurrent.Executors

/**
 * @Author peerless2012
 * @Email peerless2012@126.com
 * @DateTime 2023/5/26 15:54
 * @Version V1.0
 * @Description
 */
class RPCService: Service(), IRPCService {

    companion object {

        private const val TAG = "RPCService"

        /**
         * R-P-C-S
         */
        private const val NOTIFICATION_ID = 0x52504353

        private const val CHANNEL_ID = "RPCService"

        private const val CHANNEL_NAME = "RPCService"

    }

    private lateinit var mServer: Server

    private val mRPCServer = GreeterGrpcServer()

    private lateinit var mNotification: Notification

    private lateinit var mInfoIntent: PendingIntent

    override fun onCreate() {
        super.onCreate()

        createPendingIntent()
        createNotificationChannel()
        createNotification()
        startForeground(NOTIFICATION_ID, mNotification)

        mServer = OkHttpServerBuilder
            .forPort(6800, InsecureServerCredentials.create())
            .addService(mRPCServer)
            .executor(Executors.newCachedThreadPool())
            .build()
        mServer.start()
    }

    private fun createPendingIntent() {
        val infoIntent = Intent(this, MainActivity::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        mInfoIntent =
            PendingIntent.getActivity(this
                , 1
                , infoIntent
                , flags)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mediaChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            mediaChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            mediaChannel.setSound(Uri.EMPTY, audioAttributes)
            mediaChannel.enableLights(false)
            mediaChannel.enableVibration(false)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(mediaChannel)
        }
    }

    private fun createNotification() {
        val appName = resources.getString(applicationInfo.labelRes)
        val ticker = resources.getString(R.string.rpc_service_foreground_service_ticker, appName)
        val notifyBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.rpc_service_foreground_service)
            .setContentTitle(resources.getString(R.string.rpc_service_foreground_service_title))
            .setContentText(resources.getString(R.string.rpc_service_foreground_service_content))
        notifyBuilder.setShowWhen(false)
        notifyBuilder.setAutoCancel(false)
        notifyBuilder.setOngoing(true)
        notifyBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifyBuilder.priority = NotificationCompat.PRIORITY_LOW
        } else {
            notifyBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }
        notifyBuilder.setTicker(ticker)
        notifyBuilder.setContentIntent(mInfoIntent)
        mNotification = notifyBuilder.build()
    }

    override fun onBind(intent: Intent?): IBinder {
        return RPCServiceBinder(this)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mRPCServer.setInfoListener(null)
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        mServer.shutdownNow()
        super.onDestroy()
    }

    override fun getInfo(): String {
        return mRPCServer.getInfo()
    }

    override fun setInfoListener(listener: ((String) -> Unit)?) {
        mRPCServer.setInfoListener(listener)
    }

}
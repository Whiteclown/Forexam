package com.bobrovskii.forexam

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bobrovskii.core.NOTIFICATION_ANSWER_INTENT_ACTION
import com.bobrovskii.core.NOTIFICATION_MESSAGE_INTENT_ACTION
import com.bobrovskii.exam.data.dto.MessageNotification
import com.bobrovskii.exam.domain.usecase.GetAccountByIdUseCase
import com.bobrovskii.exam.domain.usecase.SaveFirebaseTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService(), CoroutineScope {

	override val coroutineContext: CoroutineContext = Dispatchers.Default

	@Inject
	lateinit var saveFirebaseTokenUseCase: SaveFirebaseTokenUseCase

	@Inject
	lateinit var getAccountByIdUseCase: GetAccountByIdUseCase

	private lateinit var notificationManager: NotificationManager

	private companion object {

		const val TOKEN_EVENT = "com.google.firebase.messaging.NEW_TOKEN"
		const val NEW_TOKEN = "token"

		const val NOTIFICATION_ID = "google.message_id"
		const val NOTIFICATION_TITLE = "gcm.notification.title"
		const val NOTIFICATION_BODY = "gcm.notification.body"
	}

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		launch {
			saveFirebaseTokenUseCase(token)
		}
		Log.d("myTag", token)
	}

	override fun onCreate() {
		super.onCreate()
		notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		initChannel("23", "MyChannel")
	}

	private fun send(
		id: Long,
		title: String,
		text: String,
		channelId: String
	) {
		val notificationBuilder = NotificationCompat.Builder(this, channelId)
			.setSmallIcon(R.drawable.ic_launcher_background)
			.setContentTitle(title)
			.setStyle(NotificationCompat.BigTextStyle().bigText(text))
			.setContentText(text)
			.setAutoCancel(true)
			.setCategory(NotificationCompat.CATEGORY_MESSAGE)
			.setDefaults(NotificationCompat.DEFAULT_ALL)
			.setPriority(NotificationCompat.PRIORITY_HIGH)

		notificationManager.notify(id.toInt(), notificationBuilder.build())
	}

	override fun handleIntent(intent: Intent) {
		if (intent.isTokenEvent()) {
			val token = requireNotNull(intent.getStringExtra(NEW_TOKEN))
			Log.d("TEST_TECH", token)
			onNewToken(token)
			return
		}

		var messageTitle = intent.extras?.getString(NOTIFICATION_TITLE) ?: return
		var messageBody = intent.extras?.getString(NOTIFICATION_BODY) ?: return
		Log.d("myTag", messageBody)

		val notifId = messageBody.hashCode().toLong()

		val moshi = Moshi.Builder().build()
		when (messageTitle) {
			"NEW_MESSAGE"    -> {
				val adapter = moshi.adapter(MessageNotification::class.java)
				val messageNotification = adapter.fromJson(messageBody)
				launch {
					messageNotification?.let { notification ->
						val senderAcc = getAccountByIdUseCase(notification.accountId)
						messageTitle = "${senderAcc.surname} ${senderAcc.name}"
						messageBody = notification.text
						sendBroadcast(Intent(NOTIFICATION_MESSAGE_INTENT_ACTION).putExtra("accountId", notification.accountId))
						send(notifId, messageTitle, messageBody, "23")
					}
				}
			}

			"ANSWER_CHANGED" -> {
				sendBroadcast(Intent(NOTIFICATION_ANSWER_INTENT_ACTION))
			}
		}
	}

	private fun initChannel(channelId: String, channelName: String) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
			notificationManager.createNotificationChannel(channel)
		}
	}

	private fun Intent.isTokenEvent(): Boolean =
		TOKEN_EVENT == action
}
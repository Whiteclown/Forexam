package com.bobrovskii.core

import android.content.IntentFilter

const val NOTIFICATION_ANSWER_INTENT_ACTION = "com.bobrovskii.nstu.forexam.custom.intent.action.UPDATE_ANSWER"

val NOTIFICATION_ANSWER_FILTER = IntentFilter(NOTIFICATION_ANSWER_INTENT_ACTION)

const val NOTIFICATION_MESSAGE_INTENT_ACTION = "com.bobrovskii.nstu.forexam.custom.intent.action.UPDATE_MESSAGE"

val NOTIFICATION_MESSAGE_FILTER = IntentFilter(NOTIFICATION_MESSAGE_INTENT_ACTION)

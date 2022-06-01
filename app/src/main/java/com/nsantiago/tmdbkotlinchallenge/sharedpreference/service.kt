package com.nsantiago.tmdbkotlinchallenge.sharedpreference

import android.content.Context

private const val PREFERENCE_NAME = "com.nsantiago.tmdbkotlinchallenge.sharedpreferences"
private const val GUEST_SESSION_ID = "GUEST_SESSION_ID"
class SharedPreferenceService(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun getGuestSessionId(): String? {
        return sharedPreferences.getString(GUEST_SESSION_ID, null)
    }
    fun putGuestSessionId(sessionId: String) {
        editor.putString(GUEST_SESSION_ID, sessionId)
        editor.commit()
    }
    fun removeGuestSessionId() {
        editor.remove(GUEST_SESSION_ID)
    }
}
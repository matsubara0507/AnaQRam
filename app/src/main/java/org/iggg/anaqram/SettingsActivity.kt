package org.iggg.anaqram

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(android.R.id.content, SettingsPreferenceFragment())
        fragmentTransaction.commit()
    }
}

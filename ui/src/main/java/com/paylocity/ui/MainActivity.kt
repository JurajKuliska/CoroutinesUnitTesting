package com.paylocity.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paylocity.ui.apod.ApodFragment
import com.paylocity.unittest.R

class MainActivity : AppCompatActivity() {

	@SuppressLint("CommitTransaction")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)
		if (savedInstanceState == null) {
			supportFragmentManager.beginTransaction()
				.replace(R.id.container, ApodFragment.newInstance())
				.commitNow()
		}
	}
}
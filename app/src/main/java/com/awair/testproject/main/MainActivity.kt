package com.awair.testproject.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import com.awair.testproject.R
import com.awair.testproject.event.list.ui.EventListFragment
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EventListFragment.newInstance())
                .commitNow()
        }
    }
}
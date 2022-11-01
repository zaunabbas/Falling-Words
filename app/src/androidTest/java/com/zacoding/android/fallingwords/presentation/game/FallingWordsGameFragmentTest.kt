package com.zacoding.android.fallingwords.presentation.game

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class FallingWordsGameFragmentTest{

    @Test
    fun testLaunchFallingWordGameFragment() {
        val scenario = launchFragmentInContainer<FallingWordsGameFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

}
package com.galou.watchmyback

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.galou.watchmyback.mainActivity.MainActivityViewModel
import com.galou.watchmyback.testHelpers.FakeAuthResult
import com.galou.watchmyback.testHelpers.LiveDataTestUtil
import com.galou.watchmyback.testHelpers.UserRepositoryMocked
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by galou on 2019-10-24
 */

class MainActivityViewModelTest {

    private lateinit var viewModel: MainActivityViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){
        viewModel = MainActivityViewModel(UserRepositoryMocked())

    }

    @Test
    fun checkUserNotConnected_openSignInActivityEmitted(){
        val firebaseUser = null
        viewModel.checkIfUserIsConnected(firebaseUser)
        val value: Event<Unit> = LiveDataTestUtil.getValue(viewModel.openSignInActivityEvent)
        assertThat(value.getContentIfNotHandled()).isNotNull()

    }

    @Test
    fun checkUserIsConnected_isWelcomeBack(){
        val firebaseUser = FakeAuthResult.user
        viewModel.checkIfUserIsConnected(firebaseUser)
        assertNull(LiveDataTestUtil.getValue(viewModel.openSignInActivityEvent))
        //val userValue = LiveDataTestUtil.getValue(viewModel.userLD)
        //assertEquals(userValue.email, firebaseUser.email)
        //assertEquals(userValue.username, firebaseUser.displayName)
        //assertEquals(userValue.pictureUrl, firebaseUser.photoUrl)
        //assertSnackBarMessage(viewModel.snackbarMessage, R.string.welcome)
    }

}
package com.gauvain.seigneur.presentation.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseViewModelTest {

    // Run tasks synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    val testingDispatcher = Dispatchers.Unconfined

    @Before
    open fun setup() {
        Dispatchers.setMain(testingDispatcher)
    }

    @After
    open fun tearDown() {
        Dispatchers.resetMain()
    }


    @ExperimentalCoroutinesApi
    fun unconfinifyTestScope() {
        ui = Dispatchers.Unconfined
        io = Dispatchers.Unconfined
        background = Dispatchers.Unconfined
    }
}
package com.example.localweatherapp.extension

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import java.util.*

class ExtensionsKtTest {

    @Test
    fun within1hourAgo_null() {
        val now = Date()
        assertThat(now.within1hourAgo(null), `is`(false))
    }

    @Test
    fun within1hourAgo_equalTime() {
        val from = Date(1611901852 * 1000L)
        assertThat(from.within1hourAgo(1611901852), `is`(true))
    }

    @Test
    fun within1hourAgo_forwardTime() {
        val fromUnixTime = 1611898252
        val from = Date(fromUnixTime * 1000L)
        assertThat(from.within1hourAgo(fromUnixTime + 1), `is`(false))
    }

    @Test
    fun within1hourAgo_1hourAgoTime() {
        val fromUnixTime = 1611898252
        val from = Date(fromUnixTime * 1000L)
        assertThat(from.within1hourAgo(fromUnixTime - 3600), `is`(true))
    }

    @Test
    fun within1hourAgo_moreThan1hourAgo() {
        val fromUnixTime = 1611898252
        val from = Date(fromUnixTime * 1000L)
        assertThat(from.within1hourAgo(fromUnixTime - 3601), `is`(false))
    }
}
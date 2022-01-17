package com.example.localweatherapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.composethemeadapter.MdcTheme


class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onClickForLicenses: () -> Unit = {
            startActivity(Intent(this, OssLicensesMenuActivity::class.java))
        }

        val onClickForWeatherApi: () -> Unit = {
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse("https://openweathermap.org/api"))
        }

        setContent {
            MdcTheme() {
                HelpContent(onClickForLicenses, onClickForWeatherApi)
            }
        }
    }
}

@Composable
private fun HelpContent(onClick0: () -> Unit = {}, onClick1: () -> Unit = {}) {
    Column {
        Text(
            text = stringResource(id = R.string.oss_license_title),
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .clickable(
                    enabled = true
                ) { onClick0() },
        )
        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(start = 10.dp)
        )
        Text(
            text = "Weather API - OpenWeatherMap",
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .clickable(
                    enabled = true
                ) { onClick1() },
        )
    }
}

@Preview
@Composable
fun Preview() {
    HelpContent() {}
}
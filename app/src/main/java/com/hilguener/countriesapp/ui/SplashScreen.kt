package com.hilguener.countriesapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hilguener.countriesapp.R
import com.hilguener.countriesapp.ui.theme.CountriesAppTheme

@SuppressLint("CustomSplashScreen")
class SplashScreen() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CountriesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    SplashScreenContent()
                }
            }
        }

        Thread {
            Thread.sleep(2000)
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }.start()
    }
}

@Composable
fun SplashScreenContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center, content = {
        Surface {
            Image(
                painter = painterResource(id = R.drawable.countries),
                contentDescription = "Countries",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit,
            )
        }
    })
}

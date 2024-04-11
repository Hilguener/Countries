package com.hilguener.countriesapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.hilguener.countriesapp.R
import com.hilguener.countriesapp.model.Country
import com.hilguener.countriesapp.model.Currency
import com.hilguener.countriesapp.ui.theme.CountriesAppTheme
import com.hilguener.countriesapp.viewmodel.CountryViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val countryViewModel: CountryViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CountriesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    HomeScreen(countryViewModel = countryViewModel, this)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    countryViewModel: CountryViewModel,
    context: ComponentActivity,
) {
    val countriesState by countryViewModel.countries.observeAsState(initial = emptyList())
    val isLoading = countryViewModel.isLoading.value

    Scaffold(topBar = { CountryAppBar() }) {
        val bottomSheetState = rememberModalBottomSheetState()
        val selectedCountry = remember { mutableStateOf<Country?>(null) }
        var isSheetOpen by rememberSaveable {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()

        val isInternetConnectionVisible = !isNetworkAvailable(context) && !isSheetOpen

        LazyColumn(
            contentPadding = it,
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            content = {
                items(countriesState) { country ->
                    CountryItem(
                        country = country,
                        modifier = Modifier
                            .clickable {
                                isSheetOpen = true
                                selectedCountry.value = country
                                scope.launch { bottomSheetState.show() }
                            }
                            .padding(5.dp),
                    )
                }

                if (isInternetConnectionVisible) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            NoInternetConnectionMessage(onRefreshClick = {
                                countryViewModel.fetchCountries()
                            })
                        }
                    }
                }

                if (isLoading == true) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            LinearProgressIndicator(color = Color.Yellow)
                        }
                    }
                }
            },
        )

        if (isSheetOpen) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isSheetOpen = false },
            ) {
                selectedCountry.value?.let { country ->
                    CountryBottomSheet(
                        country = country,
                    )
                }
            }
        }
    }
}

@Composable
fun NoInternetConnectionMessage(onRefreshClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.no_internet_connection),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRefreshClick,
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    text = stringResource(R.string.refresh),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

@Composable
fun CountryItem(
    country: Country,
    modifier: Modifier,
) {
    ElevatedCard(modifier = modifier.padding(8.dp)) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .sizeIn(72.dp),
        ) {
            Box(modifier = modifier.size(80.dp)) {
                CountryIcon(countryFlag = country.flags.png)
            }
            Spacer(modifier = modifier.width(8.dp))
            CountryInfo(
                country.name.official,
                country.region,
            )
        }
    }
}

@Composable
fun CountryAlertDialog() {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        AlertDialog(onDismissRequest = {
            showDialog.value = false
        }, title = {
            Text(text = "Sort by")
        }, text = {
            Text(text = "Este é um exemplo de AlertDialog em Compose.")
        }, confirmButton = {
            Button(onClick = {
                showDialog.value = false
            }) {
                Text(text = "OK")
            }
        })
    }
    IconButton(onClick = {
        showDialog.value = true
    }) {
        Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.morevert))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryAppBar() {
    TopAppBar(title = {
        Text(
            text = stringResource(R.string.countries),
            style = MaterialTheme.typography.displayMedium,
            fontSize = 25.sp,
        )
    }, actions = {
        IconButton(onClick = { }) {
            Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
        }
        CountryAlertDialog()
    })
}

@Composable
fun CountryCurrency(currency: Currency?) {
    currency?.let { cur ->
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        ) {
            Text(
                text = "Currency: ${cur.name} (${cur.symbol})",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Composable
fun CountryBottomSheet(country: Country?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        CountryIcon(
            countryFlag = country?.flags?.png ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(start = 12.dp, end = 12.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(
                R.string.name,
                country?.name?.official ?: stringResource(R.string.unknown),
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = stringResource(
                    R.string.region,
                    country?.region ?: stringResource(R.string.unknown),
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(
                    R.string.fifa,
                    country?.fifa ?: stringResource(R.string.unknown),
                ),
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(
                R.string.subregion,
                country?.subregion ?: stringResource(R.string.unknown),
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = stringResource(
                    R.string.population,
                    NumberFormat.getNumberInstance(Locale.getDefault())
                        .format(country?.population ?: 0),
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(
                    R.string.area_m,
                    country?.area ?: stringResource(R.string.unknown),
                ),
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(
                R.string.capital,
                country?.capital?.firstOrNull() ?: stringResource(R.string.unknown),
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        )
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = stringResource(R.string.coat_of_arms),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 18.dp),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(16.dp))
            CountryIcon(
                countryFlag = country?.coatOfArms?.png ?: "",
                modifier = Modifier.size(60.dp),
            )
        }
        Text(
            text = stringResource(
                R.string.status,
                country?.status ?: stringResource(R.string.unknown),
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        val bordersText =
            country?.borders?.joinToString(", ") ?: stringResource(R.string.no_borders_available)
        Text(
            text = stringResource(R.string.borders, bordersText),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(
                R.string.demonyms,
                country?.demonyms?.eng?.m ?: stringResource(R.string.unknown),
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        )
        CountryCurrency(currency = country?.currencies?.values?.firstOrNull())
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CountryIcon(
    countryFlag: String,
    modifier: Modifier = Modifier,
) {
    GlideImage(
        model = countryFlag,
        modifier = modifier
            .padding(8.dp)
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.None,
        contentDescription = null,
    )
}

@Composable
fun CountryInfo(
    countryName: String,
    region: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(top = 16.dp)) {
        Text(text = countryName, style = MaterialTheme.typography.displaySmall)
        Text(
            text = stringResource(id = R.string.region, region),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
fun CountryPreview() {
    CountriesAppTheme(darkTheme = false) {
//        HomeScreen(viewModel = HomeScreenViewModel())
    }
}

@Preview
@Composable
fun CountryDarkPreview() {
    CountriesAppTheme(darkTheme = true) {
//        HomeScreen(viewModel = HomeScreenViewModel())
    }
}

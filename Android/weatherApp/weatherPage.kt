package com.Android.weatherApp
// Necessary Imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.adarshelectronics.api.NetworkResponse
import com.example.adarshelectronics.api.WeatherModel

@Composable
   // Weather Page Function
   fun WeatherPage (viewModel: WeatherViewModel) {
      val weatherData = viewModel.weatherData.observeAsState()

      // KeyBoardController
      val keyboardController = LocalSoftwareKeyboardController.current

      var city by remember{
         mutableStateOf("")
      }

      // Setting up the Search Bar and Search Button
      Column (
         modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
         horizontalAlignment = Alignment.CenterHorizontally
      ){
         Row (
            modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
         ){
            OutlinedTextField(
               modifier = Modifier.weight(1f),
               value = city,
               onValueChange =
               {
                  city = it
               },
               label = {
                  Text(text = "Search for any Location") }
            )
            IconButton(onClick = {
               viewModel.getData(city)
               keyboardController?.hide()
            }) {
               Icon(imageVector = Icons.Default.Search,
                  contentDescription = "Search Button")

            }

         }

         // Result Scenarios
         when(val result = weatherData.value){
            is NetworkResponse.Error ->
               Text(text = result.message)
            NetworkResponse.Loading ->
               CircularProgressIndicator()
            is NetworkResponse.Success ->
               WeatherDetails(data = result.data)
            null ->
               Text(text = "")
         }
      }
   }

@Composable
   // Weather Details Function : Integrating And Displaying Data
fun WeatherDetails(data: WeatherModel){
   // Weather Details Column
   Column (
      modifier = Modifier
         .fillMaxWidth()
         .padding(vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally ){

      // Location Row
      Row (
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.Start,
         verticalAlignment = Alignment.Bottom
      ){
         Icon(imageVector = Icons.Default.LocationOn,
            contentDescription = "Location Icon",
            modifier = Modifier.size(40.dp)
         )
         Text(text = data.location.name, fontSize = 30.sp)

         Spacer(modifier = Modifier.width(9.dp))
         Text(text = data.location.country, fontSize = 20.sp, color = Color.Gray)
         Spacer(modifier = Modifier.width(9.dp))
         Text(text = data.location.localtime, fontSize = 17.sp, color = Color.Gray)
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Weather Icon
      AsyncImage(
         model = "https:${data.current.condition.icon}"
         , contentDescription = "Weather Icon",
         modifier = Modifier.size(150.dp))
      Text(
         text = data.current.condition.text,
         fontSize = 20.sp,
         color = Color.Gray,
         textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(11.dp))

      // Displaying the Temperature
      Text(
         text ="${data.current.temp_c}°C",
         fontSize = 56.sp,
         color = Color.Black,
         fontWeight = FontWeight.Bold,
         textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Card For Other Details
      Card {
         Column (
            modifier = Modifier.fillMaxWidth()
         ) {
            // Row for Humidity and Wind Speed
            Row (
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceEvenly
            ){
               WeatherKeyVal("Humidity", "${data.current.humidity}%")
               WeatherKeyVal("Wind Speed", "${data.current.wind_kph}km/h")
            }
            // Row for UV Index and Precipitation
            Row (
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceEvenly
            ){
               WeatherKeyVal("UV Index", "${data.current.uv}~")
               WeatherKeyVal("Precipitation", "${data.current.precip_mm}mm")
            }
            // Row for Feels Like and Visibility
            Row (
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceEvenly
            ){
               WeatherKeyVal("Feels Like", "${data.current.feelslike_c}°C")
               WeatherKeyVal("Visibility", "${data.current.vis_km}km")
            }
         }
      }
   }
}

@Composable
   // Weather Key Value Function For Other Weather Details Present At The Bottom
 fun WeatherKeyVal(key: String, value: String) {
    Column (
       modifier = Modifier.padding(16.dp),
       horizontalAlignment = Alignment.CenterHorizontally
    ) {
       Text(text = key , fontWeight = FontWeight.SemiBold , color = Color.Gray)
       Text(text = value , fontSize = 24.sp , fontWeight = FontWeight.Bold)
    }
 }



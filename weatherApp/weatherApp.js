const cityInput = document.querySelector('.cityInput');
const searchbtn = document.querySelector('.searchbtn');

const notFoundSection = document.querySelector('.not-found');
const searchCitySection = document.querySelector('.search-city');
const weatherInfoSection = document.querySelector('.weatherInfo');

const locationText = document.querySelector('.location-text');
const currDate = document.querySelector('.curr-date');
const weatherSummaryImage = document.querySelector('.weather-summary-image');
const tempText = document.querySelector('.temp-text');
const conditionTxt = document.querySelector('.condition-txt');
const humidityValue = document.querySelector('.humidity-value');
const windValue = document.querySelector('.wind-value');

const forecastWeatherItems = document.querySelector('.forecast-items-container');

const apiKey = "77fcbaba531e49b4ebe7caa861de0b7e";

searchbtn.addEventListener("click",() => {
    if(cityInput.value.trim() != ''){
        updateWeatherInfo(cityInput.value);
        cityInput.value =  '';
        cityInput.blur();
    }
});

cityInput.addEventListener('keydown',(event) => {
    if(event.key == 'Enter' && cityInput.value.trim() != ''){
        updateWeatherInfo(cityInput.value);
        cityInput.value =  '';
        cityInput.blur();
    }
});

async function getFetchedData(endPoint , city){
    const apiUrl = `https://api.openweathermap.org/data/2.5/${endPoint}?q=${city}&appid=${apiKey}&units=metric`;

    const response = await fetch(apiUrl);

    return response.json();
}

function getWeatherImage(id){
    if(id <= 232) return 'assets/thunderstorm.svg';
    if(id <= 321) return 'assets/drizzle.svg';
    if(id <= 531) return 'assets/rain.svg';
    if(id <= 622) return 'assets/snow.svg';
    if(id <= 781) return 'assets/atmosphere.svg';
    if(id <= 800) return 'assets/clear.svg';
    else return 'assets/clouds.svg';

}

function getCurrDate(){
    const currentDate = new Date();
    const options = {
        weekday: 'short',
        day: '2-digit',
        month: 'short'
    }
    return currentDate.toLocaleDateString('en-GB', options);
}

async function updateWeatherInfo(city){
    const weatherData = await getFetchedData('weather' , city);

    if(weatherData.cod != 200){
        showDisplaySection(notFoundSection);
        return
    }

    const {
        name: country,
        main: {temp , humidity},
        weather: [{id , main}],
        wind: {speed}
    } = weatherData ;

    locationText.textContent = country;
    tempText.textContent = Math.round(temp) + ' °C';
    conditionTxt.textContent = main ;
    humidityValue.textContent = humidity + ' %';
    windValue.textContent = speed + ' M/s';
    weatherSummaryImage.src = getWeatherImage(id);
    currDate.textContent = getCurrDate();

    await getforecastData(city);
    showDisplaySection(weatherInfoSection);
}

async function getforecastData(city) {
    const forecastsData = await getFetchedData('forecast', city);
    const timeTaken = '12:00:00';
    const todayDate = new Date().toISOString().split('T')[0];

    forecastWeatherItems.innerHTML = '';
    forecastsData.list.forEach(forecastWeather => {
        if(forecastWeather.dt_txt.includes(timeTaken) && 
            !forecastWeather.dt_txt.includes(todayDate)){
            updateForecastItems(forecastWeather);
        }
    });
}
function updateForecastItems(weatherData){
    const {
        dt_txt: date,
        weather:[{ id }],
        main: { temp }
    } = weatherData ;

    const dateTaken = new Date(date);
    const dateOptions = {
        month: 'short',
        day: '2-digit'
        
    };
    const dateResult = dateTaken.toLocaleDateString('en-US', dateOptions);

    const forecastItems = `
        <div class="forecast-items">
            <h5 class="forecast-item-date regular-text">${dateResult}</h5>
            <img src="${getWeatherImage(id)}" class="forecast-img"></img>
            <h5 class="forecast-item-temprature">${Math.round(temp)} °C</h5>
        </div>
    `;

    forecastWeatherItems.insertAdjacentHTML('beforeend', forecastItems);
}

function showDisplaySection(section){
    [weatherInfoSection, searchCitySection, notFoundSection].forEach(section => section.style.display = 'none');
    section.style.display = 'flex';
}
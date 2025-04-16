const API_BASE_URL = 'http://localhost:8080';


function displayResult(result) {
    const resultsElement = document.getElementById('results');
    resultsElement.textContent = typeof result === 'string' ? result : JSON.stringify(result, null, 2);
}


function handleError(error) {
    console.error('Error:', error);
    displayResult(error.message || 'An error occurred');
}


function showSection(sectionId) {

    document.querySelectorAll('.hidden-section').forEach(section => {
        section.style.display = 'none';
    });


    const section = document.getElementById(sectionId);
    if (section) {
        section.style.display = 'block';
    }


    document.getElementById('results').textContent = '';
}


document.getElementById('create-city-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const cityData = {
        cityName: document.getElementById('city-name').value,
        lat: parseFloat(document.getElementById('lat').value),
        lon: parseFloat(document.getElementById('lon').value),
        temperature: parseFloat(document.getElementById('temperature').value),
        humidity: parseInt(document.getElementById('humidity').value),
        windSpeed: parseFloat(document.getElementById('wind-speed').value),
        userId: parseInt(document.getElementById('user-id').value)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/cities/create?cityName=${cityData.cityName}&lat=${cityData.lat}&lon=${cityData.lon}&temperature=${cityData.temperature}&humidity=${cityData.humidity}&windSpeed=${cityData.windSpeed}&userId=${cityData.userId}`, {
            method: 'POST'
        });

        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('bulk-create-cities-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const citiesJson = document.getElementById('cities-json').value;

    try {
        const cityRequests = JSON.parse(citiesJson);
        const response = await fetch(`${API_BASE_URL}/cities/bulk-create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(cityRequests)
        });

        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('update-city-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const cityData = {
        id: parseInt(document.getElementById('update-city-id').value),
        cityName: document.getElementById('update-city-name').value,
        lat: parseFloat(document.getElementById('update-lat').value),
        lon: parseFloat(document.getElementById('update-lon').value),
        temperature: parseFloat(document.getElementById('update-temperature').value),
        humidity: parseInt(document.getElementById('update-humidity').value),
        windSpeed: parseFloat(document.getElementById('update-wind-speed').value),
        userId: parseInt(document.getElementById('update-user-id').value)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/cities/${cityData.id}?cityName=${cityData.cityName}&lat=${cityData.lat}&lon=${cityData.lon}&temperature=${cityData.temperature}&humidity=${cityData.humidity}&windSpeed=${cityData.windSpeed}&userId=${cityData.userId}`, {
            method: 'PUT'
        });

        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('delete-city-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const cityId = document.getElementById('delete-city-id').value;

    try {
        const response = await fetch(`${API_BASE_URL}/cities/${cityId}`, {
            method: 'DELETE'
        });

        const result = await response.text();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('get-city-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const cityId = document.getElementById('get-city-id').value;

    try {
        const response = await fetch(`${API_BASE_URL}/cities/${cityId}`);
        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

async function getAllCities() {
    try {
        const response = await fetch(`${API_BASE_URL}/cities`);
        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
}

document.getElementById('weather-by-city-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const cityName = document.getElementById('weather-city-name').value;

    try {
        const response = await fetch(`${API_BASE_URL}/cities/weather?city=${cityName}`);
        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('weather-by-coords-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const lat = document.getElementById('weather-lat').value;
    const lon = document.getElementById('weather-lon').value;

    try {
        const response = await fetch(`${API_BASE_URL}/cities/weather/coord?lat=${lat}&lon=${lon}`);
        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('cities-by-username-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const username = document.getElementById('username').value;

    try {
        const response = await fetch(`${API_BASE_URL}/cities/by-username?username=${username}`);
        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

// User Management Functions
document.getElementById('create-user-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userData = {
        id: parseInt(document.getElementById('user-id-create').value),
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        username: document.getElementById('username-create').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/users?id=${userData.id}&email=${userData.email}&password=${userData.password}&username=${userData.username}`, {
            method: 'POST'
        });

        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('update-user-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userData = {
        id: parseInt(document.getElementById('update-user-id1').value),
        email: document.getElementById('update-email').value,
        password: document.getElementById('update-password').value,
        username: document.getElementById('update-username').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/users/${userData.id}?email=${userData.email}&password=${userData.password}&username=${userData.username}`, {
            method: 'PUT'
        });

        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('delete-user-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userId = document.getElementById('delete-user-id').value;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`, {
            method: 'DELETE'
        });

        const result = await response.text();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

document.getElementById('get-user-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userId = document.getElementById('get-user-id').value;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}`);
        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});

async function getAllUsers() {
    try {
        const response = await fetch(`${API_BASE_URL}/users`);
        const result = await response.json();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
}

document.getElementById('add-city-to-user-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    const userId = document.getElementById('add-city-user-id').value;
    const cityId = document.getElementById('add-city-city-id').value;

    try {
        const response = await fetch(`${API_BASE_URL}/users/${userId}/cities/${cityId}`, {
            method: 'POST'
        });

        const result = await response.text();
        displayResult(result);
    } catch (error) {
        handleError(error);
    }
});


async function getRequestCount() {
    try {
        const response = await fetch(`${API_BASE_URL}/request-count`);
        const result = await response.text();
        document.getElementById('request-count-result').textContent = `Total requests: ${result}`;
    } catch (error) {
        handleError(error);
    }
}


document.addEventListener('DOMContentLoaded', function() {

    document.querySelectorAll('.hidden-section').forEach(section => {
        section.style.display = 'none';
    });
});
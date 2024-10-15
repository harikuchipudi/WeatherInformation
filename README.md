Weather Forecast Information Project

Main Objective : 
- This project results in a real time weather forecasting system which would give the weather information based on the Pincode of the location.

execution process:
- The project follows a simple approach of retrieving the weather information for a given loaction throught he following steps:
 1. Pincode is taken as the input to get the location of the area.
 2. Pincode is used to extract the longitude and latitude of the area, with the help of Rapid API.
 3. Extracted longitude and latitude are used to create the object of the pincode elemenet which is an entity, so a new row of pincode information is stored in the databse table.
 4. such that this table acts as the cache memory for the coordinates extraction, easing the system to check if the coordinates for the given pincode are already present in the database.
 5. if the given pincode information is already in the table, then we need hit the API to extract the information again, easing the complexity of the fucntionality.
 6. once the coordinates are extracted, we now have to look for the weather information at those coordinates, which is is done by the help of OpenWeatherAPI.
 7. Again, whenever we are extracting the weather Information, we create an object of the weatherInformation class, which is an enitity meaning a new row is allocated for every unique weather information.
 8. So, whenever we wan to extract the weather information for a particular location, we check for its availability in the database, if its present then the data could be retrived easily and the process can be stopped.
 9. This reduces a lot of unwanted API calls which could be expennsive and extensive for the complexity of the system.

Tech Stack used:
- SpringBoot for the backend funcitonality
- H2 In-memory database for simple database operations
- Rapid API - for extracting the latitude and longitude of the given Pincode location
- WeatherAPI - for extracting the weather forecast information at the given Pincode location by the help of extracted latitude and longitude
- Postman - for testing the API routes and configuring the endpoints

Code Flow working:
1. Take the pincode input.
2. check if the weather information is already available in the database.
3. If its avaialable return it and exit the process.
4. If its not avaialable, then extract the coordinates (latitude & longitude) to extract the weather information from the RAPID API, and also store it in the database.
5. Again, for extracting the coordinates, check if the pincode information is already avaialble in the database.
6. If its avaialble, return the pincode information along with the coordinates.
7. If its not avaialable, hit the RAPID API to extract the coordinates information.
8. now make use of the pincode information and hit the OpenWeatherInformation API and extract the weather information and also store it in the database.
9. deal with the exceptions meanwhile, at cases like the error in connection, parsing and network.

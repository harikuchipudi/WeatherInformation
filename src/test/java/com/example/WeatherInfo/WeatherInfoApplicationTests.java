package com.example.WeatherInfo;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.service.PinCodeService;
import com.example.WeatherInfo.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class WeatherInfoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private PinCodeService pinCodeService;

	@Autowired
	private WeatherService weatherService;

	@Test
	void testPinCodeServiceBeanExists(){
		assertThat(pinCodeService).isNotNull();
	}

	@Test
	void testWeatherServiceBeanExists(){
		assertThat(weatherService).isNotNull();
	}

}

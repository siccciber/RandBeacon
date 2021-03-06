package br.gov.inmetro.beacon.input;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.TimeZone;

@SpringBootApplication
public class BeaconInputApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeaconInputApplication.class, args);
	}

	@EventListener(ContextRefreshedEvent.class)
	public void contextRefreshedEvent() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}


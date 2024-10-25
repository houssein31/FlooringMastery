package dev.houssein.FlooringMastery;

import dev.houssein.FlooringMastery.view.FlooringView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FlooringMasteryApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(FlooringMasteryApplication.class, args);
		FlooringView userIO = context.getBean(FlooringView.class);
		userIO.run();
	}

}

package vehicle;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
	
	@Bean
	CommandLineRunner initDatabase(CarRepository repository) {
		return args -> {
			System.out.println("Preloading " + repository.save(new Car("Audi", "Coupe", "A7")));
			System.out.println("Preloading " + repository.save(new Car("BMW", "Coupe", "4 Series Gran Coupe")));
			System.out.println("Preloading " + repository.save(new Car("Jaguar", "SUV", "I-PACE")));
		};
	}
	

}

package vehicle;

public class CarNotFoundException extends RuntimeException {
	
	
	CarNotFoundException(Long id) {
		super("Could not find the car with ID: " + id);
	}

}

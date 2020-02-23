package vehicle;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {

	private final CarRepository repository;
	
	private final CarResourceAssembler assembler;
	
	CarController(CarRepository repository, CarResourceAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}
	
	// Aggregate root
	
	/*
	
	@GetMapping("/cars")
	List<Car> all() {
		return repository.findAll();
	}
	
	*/
	
	@GetMapping("/cars")
	CollectionModel<EntityModel<Car>> all() {

		  List<EntityModel<Car>> cars = repository.findAll().stream()
				    .map(assembler::toModel)
				    .collect(Collectors.toList());

				  return new CollectionModel<>(cars,
				    linkTo(methodOn(CarController.class).all()).withSelfRel());
	}
	
	@PostMapping("/cars")
	ResponseEntity<?> newCar(@RequestBody Car newCar) throws URISyntaxException {

	  EntityModel<Car> entityModel = assembler.toModel(repository.save(newCar));

	  return ResponseEntity
	    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
	    .body(entityModel);
	}
	// Single item
	
	/*
	@GetMapping("/cars/{id}")
	Car one(@PathVariable Long id) {
		return repository.findById(id).
				orElseThrow(() -> new CarNotFoundException(id));
	}
	*/
	
	@GetMapping("/cars/{id}")
	EntityModel<Car> one (@PathVariable Long id) {

		Car car = repository.findById(id).orElseThrow(() -> new CarNotFoundException(id));
		
		return assembler.toModel(car);
	}
	
	@PutMapping("/cars/{id}")
	Car replaceCar(@RequestBody Car newCar, @PathVariable Long id) {
		
		return repository.findById(id).map(car -> {
					car.setBrand(newCar.getBrand());
					car.setType(newCar.getType());
					car.setModel(newCar.getModel());
					return repository.save(car);
				}).orElseGet(() ->  {
			newCar.setId(id);
			return repository.save(newCar);
		});
	}
	
	@DeleteMapping("/cars/{id}")
	void deleteCar(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
	
	
	
	
}

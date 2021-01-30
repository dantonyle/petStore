package com.example.demo.store;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.util.StringUtils;

@RestController
public class StoreController {

	@Autowired
	ProductEntityCrudRepository productEntityCrudRepository;

	// displaying textboxes to add a NEW PET
	@GetMapping(path = "/addPet", produces = "text/html")
	String showProductForm() {
		String output = "<form action='' method='POST'>";
		output += "<p> Name: <input name='name' type='text' /></p>";
		output += "<p> Species: <input name='species' type='text' /></p>";
		output += "<p> Price: <input name='price' type='text' /></p>";
		output += "<input type='submit' />";
		output += "</form>";
		return output;
	}
	
	// Saving NEW PET to Database --- checks first
	@PostMapping(path = "/addPet")
	String createProduct(@RequestParam String name, @RequestParam String species, @RequestParam String price) {
		String outcome = "<form><body>";
		
		ProductEntity product = new ProductEntity();
		
		// String Check of missing information
		try {

			if (StringUtils.isNullOrEmpty(name) || StringUtils.isEmptyOrWhitespaceOnly(name) ) {
				outcome += "Missing pet name <br>";
			} 
			
			if (StringUtils.isNullOrEmpty(species) || StringUtils.isEmptyOrWhitespaceOnly(species) ) {
				outcome += "Missing species information <br>";
			} 
			
			if (StringUtils.isNullOrEmpty(price) || StringUtils.isEmptyOrWhitespaceOnly(price) ) {
				outcome += "Missing price for pet <br>";
			}

		} catch (Exception e) {

			return "Missing information";
		}
		
		if (!(outcome.equals("<form><body>"))) {
			return outcome + "</body></form>";
		}

		
		// Float conversion check 
		try {

			Float isFloat = Float.valueOf(price);

			if (!(isFloat instanceof Float)) {
				outcome = "Please enter an positive integer for Price";
			} else if (isFloat < 0) {
				outcome = "Price cannot be negative";
			} else {

				product.setName(name);
				product.setSpecies(species);
				product.setPrice(isFloat);
				productEntityCrudRepository.save(product);
				outcome = "New Pet added to PetStore";
			}

		} catch (Exception e) {
			return "Price needs to be assigned with interger values only";
		}

		return outcome;

	}

	// ADDs 2 new pets 
	@GetMapping(path = "/home")
	String home() {

		String[] animNames = { "Silva", "Tilly", "Speedy", "Sparkles", "Spot", "Bubbles", "Grover", "Greg" };
		String[] animSpecies = { "Gecko", "Tortoise", "Rabbit", "Tarantula", "Dragon", "Unicorn", "Phoenix", "Slime" };

		Random rand = new Random();
		int numName = rand.nextInt(8);
		int numAnim = rand.nextInt(8);
		Float priceTag = (rand.nextFloat() * 100) + 10;

		ProductEntity pet1 = new ProductEntity();
		pet1.setName(animNames[numName]);
		pet1.setSpecies(animSpecies[numAnim]);
		pet1.setPrice(priceTag);
		productEntityCrudRepository.save(pet1);

		numName = rand.nextInt(8);
		numAnim = rand.nextInt(8);
		priceTag = (rand.nextFloat() * 100) + 10;

		ProductEntity pet2 = new ProductEntity();
		pet2.setName(animNames[numName]);
		pet2.setSpecies(animSpecies[numAnim]);
		pet2.setPrice(priceTag);
		productEntityCrudRepository.save(pet2);

		Iterable<ProductEntity> pets = productEntityCrudRepository.findAll();

		String myProducts = "<h2>Available Pets</h2>";
		for (ProductEntity p : pets) {
			myProducts = myProducts + "Name:" + p.getName() + " | Species:" + p.getSpecies() + " | Price: $"
					+ p.getPrice() + "</p>";
		}
		return myProducts;
	}

	// Display all Pets
	@GetMapping(path = "/showPets", produces = "text/html")
	String showAllPets() {

		Iterable<ProductEntity> pets = productEntityCrudRepository.findAll();

		String myProducts = "<form action='' method='POST'><h2>Pets Available</h2> <br> <body>";
		for (ProductEntity p : pets) {
			myProducts = myProducts += "<p> Name:" + p.getName() + " | Species:" + p.getSpecies() + " | $"
					+ p.getPrice() + "</p>";
		}
		return myProducts + "</body></form>";
	}

}

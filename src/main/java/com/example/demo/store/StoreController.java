package com.example.demo.store;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class StoreController {

    @Autowired
    ProductEntityCrudRepository productEntityCrudRepository;


    @GetMapping(path = "/createProduct", produces = "text/html")
    String showProductForm() {
    	String output = "<form action='' method='POST'>";
        output += "<p> Name: <input name='name' type='text' /></p>";
        output += "<p> Species: <input name='species' type='text' /></p>";
        output += "<p> Price: <input name='price' type='text' /></p>";
        output += "<input type='submit' />";
        output += "</form>";
        return output;
    }

    @PostMapping(path = "/createProduct")
    void createProduct(@ModelAttribute ProductEntity product) {
    	if (product == null || product.getName() == null) {
            throw new RuntimeException("Please fill all information");
        } else if (product.getPrice() < 0) {
            throw new RuntimeException("Price cannot be negative");
        } else if (product.getSpecies() == null) {
        	throw new RuntimeException("Species needs to be identified");
        } else {
        	productEntityCrudRepository.save(product);
        	System.out.print("New Pet added to PetStore");
        }
    }

    @GetMapping(path = "/home")
    String home() {

    	String[] animNames = { "Silva", "Tilly", "Speedy", "Sparkles", "Spot", "Bubbles", "Grover", "Greg" };
    	String[] animSpecies = { "Gecko", "Tortoise", "Rabbit", "Tarantula", "Dragon", "Unicorn", "Phoenix", "Slime" };
    
    	Random rand = new Random();
    	int numName = rand.nextInt(8);
    	int numAnim = rand.nextInt(8);
    	
    	ProductEntity pet1 = new ProductEntity();
        pet1.setName(animNames[numName]);
        pet1.setSpecies(animSpecies[numAnim]);
        pet1.setPrice(20.98F);
        productEntityCrudRepository.save(pet1);
        
        numName = rand.nextInt(8);
    	numAnim = rand.nextInt(8);
    	
        ProductEntity pet2 = new ProductEntity();
        pet2.setName(animNames[numName]);
        pet2.setSpecies(animSpecies[numAnim]);
        pet2.setPrice(30.98F);
        productEntityCrudRepository.save(pet2);

        Iterable<ProductEntity> pets = productEntityCrudRepository.findAll();

        String myProducts = "<h2>Our products</h2>";
        for (ProductEntity p: pets) {
            myProducts = myProducts + "Name:" + p.getName() + " | Species:" + p.getSpecies() +  " | Price: $" + p.getPrice() + "</p>";
        }
        return myProducts;
    }
    
    @GetMapping(path = "/showPets", produces = "text/html")
    String showAllPets() {
    	
    
    	Iterable<ProductEntity> pets = productEntityCrudRepository.findAll();
    	
    	String myProducts = "<form action='' method='POST'><h2>Pets Available</h2> <br> <body>";
        for (ProductEntity p: pets) {
            myProducts = myProducts += "<p> Name:" + p.getName() + " | Species:" + p.getSpecies() + " | $" + p.getPrice() + "</p>";
        }
        return myProducts + "</body></form>";
    }

}

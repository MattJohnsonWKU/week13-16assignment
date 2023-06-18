package pet.store.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreCustomer.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping("/pet_store")
	@ResponseStatus(HttpStatus.CREATED)
	public PetStoreData insertPetStore(
			@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	@PutMapping("/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId,
			@RequestBody PetStoreData petStoreData) {
			petStoreData.setPetStoreId(petStoreId);
			log.info("Updating pet_Store {}", petStoreData);
			return petStoreService.savePetStore(petStoreData);
	}
	 @PostMapping("/{petStoreId}/employee")
	 @ResponseStatus(HttpStatus.CREATED)
	    public PetStoreEmployee addEmployee(
	            @PathVariable("petStoreId") Long petStoreId,
	            @RequestBody PetStoreEmployee employee) {
	        log.info("Adding employee to pet store with ID: {}", petStoreId);
	        return petStoreService.saveEmployee(petStoreId, employee);
	    }
	
	@GetMapping
	public List <PetStoreData> listAllPetStores(){
		return petStoreService.retrieveAllPetStores();
	}
	
	@GetMapping("/{petStoreId}")
	public PetStoreData getPetStoreById(
			@PathVariable("petStoreId") Long petStoreId) {
		return petStoreService.getPetStoreById(petStoreId);
	}
	
	@DeleteMapping("/{petStoreId}/delete")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
		petStoreService.deletePetStoreById(petStoreId);
		
		Map<String, String> msg = new HashMap<>();
		msg.put("message", "Delete Pet Store");
		return msg;
	}
	
	 @PostMapping("/{petStoreId}/customer")
	 @ResponseStatus(HttpStatus.CREATED)
	    public PetStoreCustomer addCustomer(
	            @PathVariable("petStoreId") Long petStoreId,
	            @RequestBody PetStoreCustomer customer) {
	        log.info("Adding customer to pet store with ID: {}", petStoreId);
	        return petStoreService.saveCustomer(petStoreId, customer);
	    }
	
	

}

package pet.store.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.dao.PetStoreDao;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {
	
	@Autowired
	private PetStoreDao petStoreDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		  PetStore petStore = findOrCreatePetStore(petStoreData.getPetStoreId());
	        copyPetStoreFields(petStore, petStoreData);
	        PetStore savedPetStore = petStoreDao.save(petStore);
	        return new PetStoreData(savedPetStore);
	    }

	    private PetStore findOrCreatePetStore(Long petStoreId) {
	        if (petStoreId != null) {
	            return petStoreDao.findById(petStoreId)
	                .orElseThrow(() -> new NoSuchElementException("Pet store not found with ID: " + petStoreId));
	        } else {
	            return new PetStore();
	        }
	    }

	    private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
	        petStore.setPetStoreName(petStoreData.getPetStoreName());
	        petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
	        petStore.setPetStoreCity(petStoreData.getPetStoreCity());
	        petStore.setPetStoreState(petStoreData.getPetStoreState());
	        petStore.setPetStoreZip(petStoreData.getPetStoreZip());
	        petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	    }
}

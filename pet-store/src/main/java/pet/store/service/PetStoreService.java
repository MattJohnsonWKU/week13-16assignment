package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreCustomer.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
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

    private PetStore findPetStoreById(Long petStoreId) {
  		return petStoreDao.findById(petStoreId)
  				.orElseThrow(() -> new NoSuchElementException(
  						"Pet Store with ID= " + petStoreId + " was not found."));
  	}
    

    @Autowired
    private EmployeeDao employeeDao;

    @Transactional(readOnly = false)
    public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
    	PetStore petStore = findPetStoreById(petStoreId);
    	Employee employee = findOrCreateEmployee(petStoreEmployee.getEmployeeId());
        copyEmployeeFields(employee, petStoreEmployee);
        employee.setPetStore(petStore);
        Employee savedEmployee = employeeDao.save(employee);
        return new PetStoreEmployee(savedEmployee);
    }

  

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
        employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
        employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
        employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
        employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
    }

    private Employee findOrCreateEmployee(Long employeeId) {
        if (employeeId != null) {
            return employeeDao.findById(employeeId)
                    .orElseThrow(() -> new NoSuchElementException("Employee not found with ID: " + employeeId));
        } else {
            return new Employee();
        }
        
    }

    @Autowired
    private CustomerDao customerDao;
    
    @Transactional(readOnly = false)
    public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
    	PetStore petStore = findPetStoreById(petStoreId);
    	Customer customer = findOrCreateCustomer(petStore, petStoreCustomer.getCustomerId());
    	copyCustomerFields(customer, petStoreCustomer, petStore);
   	
    	customer.getPetStores().add(petStore);
    	Customer savedCustomer = customerDao.save(customer);
    	savedCustomer.getPetStores().add(petStore);
    	return new PetStoreCustomer(savedCustomer);
    }

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer, PetStore petStore) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
		petStore.getCustomers().add(customer);
	}

	private Customer findOrCreateCustomer(PetStore petStore, Long customerId) {
		Customer customer;
		if (customerId != null) {
			customer = customerDao.findById(customerId)
					.orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));
			for(PetStore pet : customer.getPetStores()) {
				if (pet.getPetStoreId() == petStore.getPetStoreId()) {
					customer.getPetStores().add(pet);
				}
			}
			
		} else {
			customer = new Customer();
			customer.setPetStores(listOfPetStoresById(petStore.getPetStoreId()));
		}	
		return customer;
	
 
 }

	private Set<PetStore> listOfPetStoresById(Long petStoreId) {
		List<PetStore> petStores = petStoreDao.findAll();
		Set<PetStore> setPetStores = new HashSet<>();
		
		for(PetStore tempStore : petStores) {
			if(tempStore.getPetStoreId() == petStoreId) {
				setPetStores.add(tempStore);
			}
		}
		
		return setPetStores;
	}

	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStoreData> result = new LinkedList<>();
		List<PetStore> petStores = petStoreDao.findAll();
		
		for(PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);
			psd.getCustomers().clear();
			psd.getEmployees().clear();
		
			result.add(psd);
	}
		return result;
}

	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
		
	}

	public PetStoreData getPetStoreById(Long petStoreId) {
		PetStore petStore = petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet Store with ID=" + petStoreId + " was not found."));
		return new PetStoreData(petStore);
	}
}


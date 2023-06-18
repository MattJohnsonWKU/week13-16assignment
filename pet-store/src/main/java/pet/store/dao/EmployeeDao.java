package pet.store.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pet.store.entity.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, Long> {
	
	Optional<Employee> findById(Long employeeId);

}

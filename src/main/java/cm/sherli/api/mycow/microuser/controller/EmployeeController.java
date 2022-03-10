package cm.sherli.api.mycow.microuser.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cm.sherli.api.mycow.microuser.exception.ResourceNotFoundException;
import cm.sherli.api.mycow.microuser.model.Employee;
import cm.sherli.api.mycow.microuser.model.Group;
import cm.sherli.api.mycow.microuser.repository.EmployeeRepository;
import cm.sherli.api.mycow.microuser.repository.GroupRepository;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/mycow")
public class EmployeeController {

	@Autowired
	  private GroupRepository gRepository;

	  @Autowired
	  private EmployeeRepository emplRepository;
	  
	  @PostMapping("/employee")
	  public ResponseEntity<Employee> addEmpl(@RequestBody Employee empl){
		
		  Employee employee = emplRepository.save(empl);
		  return new ResponseEntity<>(employee, HttpStatus.CREATED);
		  
	  }
	  
	  
	  @GetMapping("/employees")
	  public ResponseEntity<List<Employee>> getAllTags() {
	    List<Employee> employees = new ArrayList<Employee>();

	    emplRepository.findAll().forEach(employees::add);

	    if (employees.isEmpty()) {
	      throw new ResourceNotFoundException("No Employee found");
	    }

	    return new ResponseEntity<>(employees, HttpStatus.OK);
	  }
	  
	  @GetMapping("/groups/{groupId}/employees")
	  public Optional<Employee> getAllTagsByTutorialId(@PathVariable(value = "groupId") Long groupId) {
	    if (!gRepository.existsById(groupId)) {
	      throw new ResourceNotFoundException("Not found Tutorial with id = " + groupId);
	    }
  
	    Optional<Employee> employees = emplRepository.findById(groupId);
	    return employees;
	  }

	  @GetMapping("/employees/{id}")
	  public ResponseEntity<Employee> getEmplsById(@PathVariable(value = "id") Long id) {
		  Employee employee = emplRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Not found employee with id = " + id));

	    return new ResponseEntity<>(employee, HttpStatus.OK);
	  }
	  
	  @GetMapping("/employees/{employeeId}/groups")
	  public Optional<Group> getAllGroupByEmplId(@PathVariable(value = "employeeId") Long employeeId) {
	    if (!emplRepository.existsById(employeeId)) {
	      throw new ResourceNotFoundException("No Employee found with Id = " + employeeId);
	    }

	    Optional<Group> groups = gRepository.findById(employeeId);
	    return groups;
	  }

	  @PostMapping("/groups/{groupId}/employees")
	  public ResponseEntity<Employee> addEmpl(@PathVariable(value = "groupId") Long groupId, @RequestBody Employee employeeRequest) {
		  Employee employee = gRepository.findById(groupId).map(group -> {
	      long employeeId = employeeRequest.getId();
	      
	      // tag is existed
	      if (employeeId != 0L) {
	        Employee _employee = emplRepository.findById(employeeId)
	            .orElseThrow(() -> new ResourceNotFoundException("Not found Employee with id = " + employeeId));
	        group.addemployee(_employee);
	        gRepository.save(group);
	        return _employee;
	      }
	      
	      // add and create new Tag
	      group.addemployee(employeeRequest);
	      return emplRepository.save(employeeRequest);
	    }).orElseThrow(() -> new ResourceNotFoundException("Not found Group with id = " + groupId));

	    return new ResponseEntity<>(employee, HttpStatus.CREATED);
	  }

	  @PutMapping("/employees/{id}")
	  public ResponseEntity<Employee> updateEmpl(@PathVariable("id") long id, @RequestBody Employee employeeRequest) {
		  Employee employee = emplRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("employeeId " + id + "not found"));

	    employee.setName(employeeRequest.getName());

	    return new ResponseEntity<>(emplRepository.save(employee), HttpStatus.OK);
	  }
	 
	  @DeleteMapping("/groups/{groupId}/employees/{employeeId}")
	  public ResponseEntity<HttpStatus> deleteEmplFromGroup(@PathVariable(value = "groupId") Long groupId, @PathVariable(value = "employeeId") Long employeeId) {
		  Group group = gRepository.findById(groupId)
	        .orElseThrow(() -> new ResourceNotFoundException("No Group found with id = " + groupId));
	    
		  group.removeemployee(employeeId);
	    gRepository.save(group);
	    
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  }
	  
	  @DeleteMapping("/employees/{id}")
	  public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") long id) {
		  emplRepository.deleteById(id);

	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  }
}

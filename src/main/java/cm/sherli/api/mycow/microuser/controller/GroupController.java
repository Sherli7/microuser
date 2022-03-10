package cm.sherli.api.mycow.microuser.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cm.sherli.api.mycow.microuser.exception.ResourceNotFoundException;
import cm.sherli.api.mycow.microuser.model.Group;
import cm.sherli.api.mycow.microuser.repository.GroupRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/mycow")
public class GroupController {

	  @Autowired
	  GroupRepository gRepository;

	  @GetMapping("/groups")
	  public ResponseEntity<List<Group>> getAllgroups(@RequestParam(required = false) String name) {
	    List<Group> groups = new ArrayList<Group>();

	    if (name == null)
	      gRepository.findAll().forEach(groups::add);
	    else
	      gRepository.findByNameContaining(name).forEach(groups::add);

	    if (groups.isEmpty()) {
	    	 throw new ResourceNotFoundException("No group found");
	    }

	    return new ResponseEntity<>(groups, HttpStatus.OK);
	  }

	  @GetMapping("/groups/{id}")
	  public ResponseEntity<Group> getGroupById(@PathVariable("id") long id) {
	    Group Group = gRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Not found Group with id = " + id));

	    return new ResponseEntity<>(Group, HttpStatus.OK);
	  }

	  @PostMapping("/groups")
	  public ResponseEntity<Group> createGroup(@RequestBody Group Group) {
	    Group _Group = gRepository.save(new Group(Group.getName(), Group.getDescription()));
	    return new ResponseEntity<>(_Group, HttpStatus.CREATED);
	  }

	  @PutMapping("/groups/{id}")
	  public ResponseEntity<Group> updateGroup(@PathVariable("id") long id, @RequestBody Group Group) {
	    Group _Group = gRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Not found Group with id = " + id));

	    _Group.setName(Group.getName());
	    _Group.setDescription(Group.getDescription());
	    
	    return new ResponseEntity<>(gRepository.save(_Group), HttpStatus.OK);
	  }

	  @DeleteMapping("/groups/{id}")
	  public ResponseEntity<HttpStatus> deleteGroup(@PathVariable("id") long id) {
	    gRepository.deleteById(id);
	    
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  }

	  @DeleteMapping("/groups")
	  public ResponseEntity<HttpStatus> deleteAllgroups() {
	    gRepository.deleteAll();
	    
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  }

	 
	}
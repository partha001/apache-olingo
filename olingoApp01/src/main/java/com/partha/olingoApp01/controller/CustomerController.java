package com.partha.olingoApp01.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.partha.olingoApp01.entity.Customer;
import com.partha.olingoApp01.service.CustomerService;

@RestController
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	
	@GetMapping(value="/customers")
	public List<Customer> getAllCustomers(){
		return service.getAllCustomers();
	}
	
	
	
	@GetMapping(value="/customers/{id}")
	public ResponseEntity getCustomerById(@PathVariable Integer id){
		Optional<Customer> foundCustomer = service.findCustomerById(id);
		if(foundCustomer.isPresent())
			return new ResponseEntity<Customer>(foundCustomer.get(), HttpStatus.OK);
		else
			return new ResponseEntity( HttpStatus.NOT_FOUND);
	}
	
	
	@PostMapping(value="/customers")
	public ResponseEntity createCustomer(@RequestBody Customer customer){
		return new ResponseEntity(service.createCustomer(customer),HttpStatus.CREATED);
	}
	
	@PutMapping(value="/customers")
	public ResponseEntity updateCustomer(@RequestBody Customer customer){
		return new ResponseEntity(service.updateCustomer(customer),HttpStatus.OK);
	}
	
	
		

}


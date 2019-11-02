package com.partha.olingoApp01.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.partha.olingoApp01.entity.Customer;
import com.partha.olingoApp01.repository.CustomerRepository;

@Service
public class CustomerService {
	
	
	@Autowired
	private CustomerRepository repository;
	
	
	
	public List<Customer> getAllCustomers(){
		return (List<Customer>) repository.findAll();
	}
	
	
	public Optional<Customer> findCustomerById(int id){
		return repository.findById(id);
	}
	
	public  Customer createCustomer(Customer customer){
		return repository.save(customer);
	}


	public Customer updateCustomer(Customer customer) {
		return repository.save(customer);
	}


	
	
	
}
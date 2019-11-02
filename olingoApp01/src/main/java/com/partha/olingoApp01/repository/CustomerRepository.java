package com.partha.olingoApp01.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.partha.olingoApp01.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer>{
	
	

}


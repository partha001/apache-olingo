package com.partha.olingoApp01.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="customer")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	
//	@OneToMany(fetch=FetchType.LAZY ,cascade=CascadeType.ALL, mappedBy="customer")
//	//@JsonManagedReference
//	private List<Account> accounts;

	
}

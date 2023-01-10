package com.java.models;

import java.time.LocalDateTime;

import com.java.enums.EmissionType;

public class Emission {

	
	 private Long id;
	    private Transfer transfer;
	    private Long agent;
	    private  Long agence;
	    private EmissionType emissionType; //By Agent - By Gab
	    private LocalDateTime emissionDate;
}

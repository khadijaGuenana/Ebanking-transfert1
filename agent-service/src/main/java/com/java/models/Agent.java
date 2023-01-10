package com.java.models;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name="agent")
@Entity
@Data @ToString @AllArgsConstructor @NoArgsConstructor
public class Agent {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private double soldeAgent;
	private String username;
	private String password;
	@ManyToOne
	@JoinColumn(name="ID_AGENCE") 
	private Pointdevente pointdevente;
	 
	
}

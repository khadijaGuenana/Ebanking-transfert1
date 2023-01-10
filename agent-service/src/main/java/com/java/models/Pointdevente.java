package com.java.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class Pointdevente {
	
	@Id@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true,nullable = false)
	private String nom;
	@Column(nullable = false)
	private String ville;
	@Column(nullable = false)
	private String adresse;
	@Column(unique = true,nullable=false)
	private String email;
	@Column(unique = true,nullable=false)
	private String tel;
	
	 @JsonIgnore
	 @OneToMany(mappedBy="pointdevente",fetch=FetchType.LAZY)
	 private List<Agent> listeAgents;
	 
	/* @JsonIgnore
	 @OneToMany(mappedBy = "pointdevente",fetch = FetchType.LAZY) 
	 private List<Compte> listeComptes;
	 */
	
	
}

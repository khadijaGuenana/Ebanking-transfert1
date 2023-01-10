package com.java.services;

import java.util.List;


import com.java.enums.Status;
import com.java.models.Agent;
import com.java.models.Compte;
import com.java.models.Emission;
import com.java.models.Transfer;
import org.springframework.http.ResponseEntity;

public interface AgentService {
	
	 //public Emission emissionTransfert(Emission emission);
	 public ResponseEntity<?> TransfertsByStatus(Status status);
	 public ResponseEntity<?> addTransfertEspece(Transfer transfert,Long id);
	 public ResponseEntity<?> addTransfertEspeceMult(List<Transfer> transfert, Long id);
	 public ResponseEntity<?> addTransfertDebit(Transfer transfert,Long id);
	 public ResponseEntity<?> addTransfertMultDebit(List<Transfer> transfert,Long id);
	 public Compte findCompteByPin(String pin);
	 public ResponseEntity<?> login(Agent agent);
	 public ResponseEntity<?> addMoneyInAccount(double moneyValue,Long clientId);
	 public ResponseEntity<?> removeMoneyFromAccount(double moneyValue,Long clientId);
	 public ResponseEntity<?> clientHasAccount(Long id);
	 public ResponseEntity<?> addAccount(Compte compte);

}

package com.java.services.impl;


import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import com.java.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java.enums.Status;
import com.java.repositories.AgentRepository;
import com.java.repositories.CompteRepository;
import com.java.services.AgentService;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class AgentServiceImpl implements AgentService {

	@Autowired
	private AgentRepository agentRepository;
	@Autowired
	private CompteRepository  compteRepository;
	@Autowired
	private RestTemplate restTemplate;


	@Override
	public ResponseEntity<?> TransfertsByStatus(Status status) {
		List<Transfer> listTransfers = Arrays.asList(restTemplate.getForObject("http://TRANSFER-SERVICE/transferservice/transfer/transfersByStatus/"+status,Transfer[].class));
		if(listTransfers != null)
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(listTransfers);
		else
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Erreur s'est produite !");
	}
	@Override
	public ResponseEntity<?> addTransfertEspece(Transfer transfert, Long id) {
		Agent agent = agentRepository.findAgentById(id);
		Transfer tr = restTemplate.postForObject("http://TRANSFER-SERVICE/transferservice/transfer/cash/"+agent.getSoldeAgent(),transfert,Transfer.class);
		if(tr != null) {
			agent.setSoldeAgent(agent.getSoldeAgent()-tr.getMontant());
			agentRepository.save(agent);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(tr);
		}else{
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Erreur s'est produite !");
		}
	}

	@Override
	public ResponseEntity<?> addTransfertEspeceMult(List<Transfer> listetransferts, Long id) {
		Agent agent=agentRepository.findAgentById(id);
		List<Transfer> listetr= Arrays.asList(restTemplate.postForObject("http://TRANSFER-SERVICE/transferservice/transfer/cash/multiple/"+agent.getSoldeAgent(),listetransferts,Transfer[].class));
		double somme = 0;
		for(Transfer tr:listetr)
			somme += tr.getMontant();
		if(listetr != null) {
			agent.setSoldeAgent(agent.getSoldeAgent()-somme);
			agentRepository.save(agent);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(listetr);
		}else{
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Erreur s'est produite !");
		}
	}

	@Override
	public  ResponseEntity<?> addTransfertDebit(Transfer transfert, Long id) {
		Agent agent=agentRepository.findAgentById(id);
		Transfer tr = restTemplate.postForObject("http://TRANSFER-SERVICE/transferservice/transfer/debit/"+agent.getSoldeAgent(),transfert,Transfer.class);
		if(tr != null)
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(tr);
		else
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Erreur s'est produite !");
	}

	@Override
	public ResponseEntity<?> addTransfertMultDebit(List<Transfer> listetransferts, Long id) {
		Agent agent=agentRepository.findAgentById(id);
		List<Transfer> listetr= Arrays.asList(restTemplate.postForObject("http://TRANSFER-SERVICE/transferservice/transfer/debit/multiple/"+agent.getSoldeAgent(),listetransferts,Transfer[].class));
		if(listetr != null)
			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(listetr);
		else
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body("Erreur s'est produite !");
	}
	@Override
	public Compte findCompteByPin(String pin){
		return compteRepository.findByPin(pin);
	}

	@Override
	public ResponseEntity<?> login(Agent agent) {
		Agent ag = agentRepository.findAgentByUsernameAndPassword(agent.getUsername(),agent.getPassword());
		if(ag != null)
			return ResponseEntity
				.status(HttpStatus.OK)
				.body(ag);
		else
			return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body("les informations invalides ! ");

	}

	@Override
	public ResponseEntity<?> addMoneyInAccount(double moneyValue, Long clientId) {
		Compte compte = compteRepository.findCompteByClient(clientId);
		if(compte != null){
			compte.setSolde(compte.getSolde()+moneyValue);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(compteRepository.save(compte));
		}else{
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("Compte non trouvé !");
		}
	}

	@Override
	public ResponseEntity<?> removeMoneyFromAccount(double moneyValue, Long clientId) {
		Compte compte = compteRepository.findCompteByClient(clientId);
		if(compte != null){
			compte.setSolde(compte.getSolde()-moneyValue);
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(compteRepository.save(compte));
		}else{
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("Compte non trouvé !");
		}
	}

	@Override
	public ResponseEntity<?> clientHasAccount(Long idClient) {
		Compte compte = compteRepository.findCompteByClient(idClient);
		if(compte != null)
			return ResponseEntity
					.status(HttpStatus.OK)
					.body(compte);
		else
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
					.body("Compte non trouvé !");
	}

	@Override
	public ResponseEntity<?> addAccount(Compte compte) {
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(compteRepository.save(compte));
	}


}

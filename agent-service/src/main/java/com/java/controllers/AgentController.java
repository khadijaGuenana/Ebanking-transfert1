package com.java.controllers;

import java.util.List;

import com.java.models.Agent;
import com.java.models.Compte;
import com.java.models.MoneyValue;
import com.java.repositories.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import com.java.enums.Status;
import com.java.models.Transfer;
import com.java.services.AgentService;

@RestController
@RequestMapping("/agent")
@CrossOrigin(origins = "http://localhost:4200")
public class AgentController {
	
	@Autowired
	private AgentService agentService;

	@Autowired
	private AgentRepository agentRepository;

	
	/*@PostMapping("/TransfertEmis")
	   @ResponseStatus(HttpStatus.CREATED)
	    public Emission emissionByAgent(@RequestBody Emission emission){
	       return agentService.emissionTransfert(emission);
	   }*/
	
	@GetMapping("/transfertsByStatus/{status}")
	public ResponseEntity<?> consulterTransfertsBystatus(@PathVariable Status status){
		return agentService.TransfertsByStatus(status);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginAgent(@RequestBody Agent agent){
		return agentService.login(agent);
	}




	@PostMapping("/servirTransfertEspece/{id}")
	   @ResponseStatus(HttpStatus.CREATED)
	    public ResponseEntity<?> servirTransferEspece(@RequestBody Transfer transfert, @PathVariable Long id){
		 return agentService.addTransfertEspece(transfert,id);
	}
	
	@PostMapping("/servirTransfertEspeceMult/{id}")
	   @ResponseStatus(HttpStatus.CREATED)
	    public ResponseEntity<?> servirTransferEspeceMult(@RequestBody List<Transfer> listetransferts, @PathVariable Long id){
		return agentService.addTransfertEspeceMult(listetransferts, id);
	}

	@PostMapping("/servirTransfertDebit/{id}")
	   @ResponseStatus(HttpStatus.CREATED)
	    public ResponseEntity<?> servirTransferDebit(@RequestBody Transfer transfer, @PathVariable Long id){
		return agentService.addTransfertDebit(transfer,id);
	}
	
	@PostMapping("/servirTransfertDebitMult/{id}")
    @ResponseStatus(HttpStatus.CREATED)
		public ResponseEntity<?> servirTransferDebitMult(@RequestBody List<Transfer> listetransferts, @PathVariable Long id){
		return agentService.addTransfertMultDebit(listetransferts,id);
    }
	@GetMapping("/compte/{pin}")
	public Compte findCompteByPin(@PathVariable String pin){
		return agentService.findCompteByPin(pin);
	}
	@GetMapping("/{id}")
	public Agent findAgentById(@PathVariable Long id){
		return agentRepository.findAgentById(id);
	}
	@PostMapping("/")
	public Agent addAgent(@RequestBody Agent agent){
		return agentRepository.save(agent);
	}
	@PostMapping("/compte")
	public ResponseEntity<?> addAccount(@RequestBody Compte compte){
		return agentService.addAccount(compte);
	}
	@GetMapping("/compte/client/{idClient}")
	public ResponseEntity<?> accountOfClient(@PathVariable Long idClient){
		return agentService.clientHasAccount(idClient);
	}
	@PutMapping("/compte/addMoney/")
	public ResponseEntity<?> addMoneyInAccount(@RequestBody MoneyValue moneyValue){
		return agentService.addMoneyInAccount(moneyValue.getMoneyValue(),moneyValue.getIdClient());
	}
	@PutMapping("/compte/removeMoney/")
	public ResponseEntity<?> removeMoneyFromAccount(@RequestBody MoneyValue moneyValue){
		return agentService.removeMoneyFromAccount(moneyValue.getMoneyValue(),moneyValue.getIdClient());
	}
}

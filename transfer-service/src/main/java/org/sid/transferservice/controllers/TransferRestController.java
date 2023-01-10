package org.sid.transferservice.controllers;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import org.sid.transferservice.entities.Client;
import org.sid.transferservice.entities.Transfer;
import org.sid.transferservice.enums.Status;
import org.sid.transferservice.repositories.TransferRepository;
import org.sid.transferservice.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/transferservice/transfer")
public class TransferRestController {
    @Autowired
    private RestTemplate restTemplate;
    TransferService transferService;
    private final TemplateEngine templateEngine;
    public TransferRestController(TransferService transferService, TemplateEngine templateEngine) {
        this.transferService= transferService;
        this.templateEngine = templateEngine;
    }
    @PostMapping(path="/cash/{soldAgent}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> transferByAgent(@RequestBody Transfer transfer,@PathVariable double soldAgent) {
        return transferService.transferEspeceAgent(transfer,soldAgent);
    }
    @PostMapping(path="/cash/multiple/{soldAgent}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> transferMultByAgent(@RequestBody List<Transfer> transfers, @PathVariable double soldAgent){
        return transferService.transferEspeceAgentMult(transfers,soldAgent);
    }
    @PostMapping(path="/debit/{soldCompte}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> transferDebAgent(@RequestBody Transfer transfer,@PathVariable double soldCompte) throws Exception {
        return transferService.transferDebAgent(transfer,soldCompte);
    }
    @PostMapping(path="/debit/multiple/{soldCompte}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> transferDebMultAgent(@RequestBody List<Transfer> transfers,@PathVariable double soldCompte){
        return transferService.transferDebitAgentMult(transfers,soldCompte);
    }
    @GetMapping(path = "/getTransfer/{idTransfer}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getTransferById(@PathVariable Long idTransfer){
        return transferService.getTransferById(idTransfer);
    }

    @GetMapping(path = "/{clientSrc}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> transferByAgent(@PathVariable Long clientSrc){
        return transferService.myTransfers(clientSrc);
    }
    @PutMapping(path = "/block/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transfer blockTransfer(@PathVariable Long id){
        return transferService.blockTransfer(id);
    }
    @PutMapping(path = "/deblock/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transfer deblockTransfer(@PathVariable Long id){
        return transferService.deblockTransfer(id);
    }
    @PutMapping(path = "/extourn/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transfer extournTransfer(@PathVariable Long id){
        return transferService.extournTransfer(id);
    }
    @PutMapping(path = "/restore/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transfer restoreTransfer(@PathVariable Long id){
        return transferService.restoreTransfer(id);
    }
    @GetMapping(path = "/transfersByStatus/{status}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transfer> transfersByStatus(@PathVariable Status status){return transferService.transfersByStatus(status);}
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    ServletContext servletContext;

    @GetMapping(path = "/pdf/{idTransfer}")
    public ResponseEntity<?> getPDF(HttpServletRequest request, HttpServletResponse response,@PathVariable Long idTransfer) throws IOException {
        Transfer transfer = (Transfer) transferService.getTransferById(idTransfer).getBody();
        Client clientSrc = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+transfer.getClientSrc(),Client.class);
        Client clientDst = restTemplate.getForObject("http://CLIENT-SERVICE/client/"+transfer.getClientDst(),Client.class);
        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("transfer",transfer);
        context.setVariable("clientSrc",clientSrc);
        context.setVariable("clientDst",clientDst);
        String orderHtml = templateEngine.process("recutransfer", context);
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        //converterProperties.setBaseUri("http://TRANSFER-SERVICE");
        HtmlConverter.convertToPdf(orderHtml, target, converterProperties);
        byte[] bytes = target.toByteArray();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
    @GetMapping(path = "/expirTransfers")
    @ResponseStatus(HttpStatus.OK)
    public List<Transfer> expirTransfers(){return transferService.getExpiredTransfers();}

    @PostMapping(path="/transferByWallet")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> transferByWallet(@RequestBody Transfer transfer){
        return transferService.transferByWallet(transfer);
    }
    @PostMapping(path="/transferByWalletMult")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> transferByWalletMult(@RequestBody List<Transfer> transfers){
        return transferService.transferByWalletMult(transfers);
    }


}

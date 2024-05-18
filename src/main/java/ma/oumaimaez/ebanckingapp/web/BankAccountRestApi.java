package ma.oumaimaez.ebanckingapp.web;

import lombok.AllArgsConstructor;
import ma.oumaimaez.ebanckingapp.dtos.*;
import ma.oumaimaez.ebanckingapp.entities.BankAccount;
import ma.oumaimaez.ebanckingapp.exceptions.BalanceNotSufficientException;
import ma.oumaimaez.ebanckingapp.exceptions.BankAccountNotFoundException;
import ma.oumaimaez.ebanckingapp.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")

public class BankAccountRestApi {
    private BankAccountService bankAccountService;

  @GetMapping("/account/{accountID}")
  public BankAccountDTO getBankAccount(@PathVariable String accountID) throws BankAccountNotFoundException {
     return bankAccountService.getBankAccount(accountID);
  }

  @GetMapping("/accounts")
  public List<BankAccountDTO> listAccount(){
      return bankAccountService.listAccounts();
  }

  @GetMapping(path = "/accounts/{accountId}/pageOperations")
  public AccountHistoryDTO getAccountHistory(@PathVariable String accountId, @RequestParam(name="page",defaultValue = "0") int page, @RequestParam(name ="size",defaultValue = "5") int size ) throws BankAccountNotFoundException {
           return bankAccountService.getAccountHistory(accountId,page,size);
  }

  @PostMapping("/operations/debit")
  public DebitDTO debit(@RequestBody  DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
      this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(), debitDTO.getDescription());
      return debitDTO;
  }
    @PostMapping("/operations/credit")
    public CreditDTO credit( @RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/operations/transfer")
    public void transfer( @RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAccountDestination(),
                transferRequestDTO.getAmount()

             );
    }

    @GetMapping("/{customerId}/accounts")
    public List<BankAccount> getCustomerAccounts(@PathVariable Long customerId) {
        // Récupérer la liste des comptes clients depuis le service
        List<BankAccount> customerAccounts = bankAccountService.getCustomersAccounts(customerId);

        // Retourner la liste des comptes clients
        return customerAccounts;
    }



}

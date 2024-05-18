package ma.oumaimaez.ebanckingapp.services;

import ma.oumaimaez.ebanckingapp.dtos.*;
import ma.oumaimaez.ebanckingapp.entities.BankAccount;
import ma.oumaimaez.ebanckingapp.exceptions.BalanceNotSufficientException;
import ma.oumaimaez.ebanckingapp.exceptions.BankAccountNotFoundException;
import ma.oumaimaez.ebanckingapp.exceptions.CustomerNotFoundException;

import java.util.List;

//definir le besoin fonctionnel
public interface BankAccountService {
     CustomerDTO saveCustomer(CustomerDTO customerDTO);
     CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

     SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
     List<BankAccountDTO> listAccounts();
     List<CustomerDTO> listCustomers();
     BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
     void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
     void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
     void transfer(String accountIdSource, String accountIdDestination, double amount ) throws BankAccountNotFoundException, BalanceNotSufficientException;
     CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
     CustomerDTO updateCustomer(CustomerDTO customerDTO);
     void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

     AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    List<BankAccount> getCustomersAccounts(Long customerId);
}

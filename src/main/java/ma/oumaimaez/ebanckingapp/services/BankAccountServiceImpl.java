package ma.oumaimaez.ebanckingapp.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.oumaimaez.ebanckingapp.dtos.*;
import ma.oumaimaez.ebanckingapp.entities.*;
import ma.oumaimaez.ebanckingapp.enums.AccountStatus;
import ma.oumaimaez.ebanckingapp.enums.OperationType;
import ma.oumaimaez.ebanckingapp.exceptions.BalanceNotSufficientException;
import ma.oumaimaez.ebanckingapp.exceptions.BankAccountNotFoundException;
import ma.oumaimaez.ebanckingapp.exceptions.CustomerNotFoundException;
import ma.oumaimaez.ebanckingapp.mappers.BankAccountMapperImpl;
import ma.oumaimaez.ebanckingapp.repository.AccountOperationRepository;
import ma.oumaimaez.ebanckingapp.repository.AccountRepository;
import ma.oumaimaez.ebanckingapp.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements  BankAccountService{


    private CustomerRepository customerRepository;
    private AccountRepository BankaccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    //Logger log= LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =customerRepository.save(customer);
      return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer== null){
            throw  new CustomerNotFoundException("Customer not Found !!");
        }
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        CurrentAccount savedBankAccount=BankaccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer== null){
            throw  new CustomerNotFoundException("Customer not Found !!");
        }
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount=BankaccountRepository.save(savingAccount);
        return dtoMapper.fromSavingAccount(savedBankAccount);
    }

    @Override
    public List<BankAccountDTO> listAccounts() {
        List<BankAccount> bankAccounts = BankaccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                SavingAccount savingAccount = (SavingAccount) bankAccount; // Utilisez l'instance existante
                return dtoMapper.fromSavingAccount(savingAccount);
            } else {
                CurrentAccount currentAccount = (CurrentAccount) bankAccount; // Utilisez l'instance existante
                return dtoMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers=customerRepository.findAll();
        //programmation fontionnel apartir les streams
        List<CustomerDTO> customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        /* List<CustomerDTO> customersDTO=new ArrayList<>();
        for (Customer customer:customers){
            CustomerDTO customerDTO=dtoMapper.fromCustomer(customer);
            customersDTO.add(customerDTO);
        }
        */
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=BankaccountRepository.findById(accountId).orElseThrow(()->
            new BankAccountNotFoundException("BankAccount Not Found !!"));
        if (bankAccount instanceof SavingAccount ){
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return  dtoMapper.fromSavingAccount(savingAccount);

        }else {
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return  dtoMapper.fromCurrentAccount(currentAccount);
        }

    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount=BankaccountRepository.findById(accountId).orElseThrow(()->
                new BankAccountNotFoundException("BankAccount Not Found !!"));
        // Ajouter une ligne de journalisation pour imprimer le solde actuel du compte
        log.info("Current balance: {}", bankAccount.getBalance());

        // Ajouter une ligne de journalisation pour imprimer le montant à débiter
        log.info("Amount to debit: {}", amount);

        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient!!");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setDate(new Date());
        accountOperationRepository.save(accountOperation);
        accountOperation.setBankAccount(bankAccount);
        // save operation
        accountOperationRepository.save(accountOperation);
        // mis a jour le compte
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        BankaccountRepository.save(bankAccount);
    }


    @Override
    public void credit(String accountId, double amount, String description) throws  BankAccountNotFoundException {
        BankAccount bankAccount=BankaccountRepository.findById(accountId).orElseThrow(()->
                new BankAccountNotFoundException("BankAccount Not Found !!"));        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setDate(new Date ());
        accountOperationRepository.save(accountOperation);
        accountOperation.setBankAccount(bankAccount);
        //save operation
        accountOperationRepository.save(accountOperation);
        //mis a jour le compte
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        BankaccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
      debit(accountIdSource,amount,"Transfer to"+accountIdDestination);
      credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer Not found !!"));
        return dtoMapper.fromCustomer(customer);
    }
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer  ");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){

        customerRepository.deleteById(customerId);
    }



    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());

    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount=BankaccountRepository.findById(accountId).orElseThrow(()->
                new BankAccountNotFoundException("BankAccount Not Found !!"));

        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByDateDesc(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customerRepository.searchCustomerByNom(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS ;
    }

    @Override
    public List<BankAccount> getCustomersAccounts(Long customerId){
        return BankaccountRepository.findByCustomerId(customerId);
    }


}

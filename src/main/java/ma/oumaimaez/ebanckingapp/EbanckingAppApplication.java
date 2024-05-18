package ma.oumaimaez.ebanckingapp;

import ma.oumaimaez.ebanckingapp.dtos.BankAccountDTO;
import ma.oumaimaez.ebanckingapp.dtos.CurrentBankAccountDTO;
import ma.oumaimaez.ebanckingapp.dtos.CustomerDTO;
import ma.oumaimaez.ebanckingapp.dtos.SavingBankAccountDTO;
import ma.oumaimaez.ebanckingapp.entities.*;
import ma.oumaimaez.ebanckingapp.enums.AccountStatus;
import ma.oumaimaez.ebanckingapp.enums.OperationType;
import ma.oumaimaez.ebanckingapp.exceptions.BalanceNotSufficientException;
import ma.oumaimaez.ebanckingapp.exceptions.BankAccountNotFoundException;
import ma.oumaimaez.ebanckingapp.exceptions.CustomerNotFoundException;
import ma.oumaimaez.ebanckingapp.repository.AccountOperationRepository;
import ma.oumaimaez.ebanckingapp.repository.AccountRepository;
import ma.oumaimaez.ebanckingapp.repository.CustomerRepository;
import ma.oumaimaez.ebanckingapp.services.BankAccountService;
import ma.oumaimaez.ebanckingapp.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbanckingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbanckingAppApplication.class, args);
    }
   @Bean
   CommandLineRunner commandLineRunner (BankAccountService bankAccountService) {
       return args -> {
           Stream.of("Hassan", "Oumaima", "Roumayssae")
                   .forEach(name->{
                       CustomerDTO customer=new CustomerDTO();
                       customer.setNom(name);
                       customer.setEmail(name+"@gmail.com");
                       bankAccountService.saveCustomer(customer);
                   });
           bankAccountService.listCustomers().forEach(customer -> {
               try {
                   bankAccountService.saveCurrentBankAccount(Math.random()*90000,9800,customer.getId());
                   bankAccountService.saveSavingBankAccount(Math.random()*1200000,5.5,customer.getId());

               } catch (CustomerNotFoundException  e) {
                      e.printStackTrace();
               }

           });
           List<BankAccountDTO> bankAccounts=bankAccountService.listAccounts();
           for(BankAccountDTO bankAccount:bankAccounts){
               for(int i=0;i<10;i++){
                   String accountId;
                   if(bankAccount instanceof SavingBankAccountDTO){
                       accountId=((SavingBankAccountDTO)bankAccount).getId();
                   }else{
                       accountId=((CurrentBankAccountDTO)bankAccount).getId();

                   }
                   bankAccountService.credit(accountId,1000+Math.random()*12000,"CREDIT");
                   bankAccountService.debit(accountId,100+Math.random()*9000,"DEBIT");

               }

           }
       };
   }


    //@Bean
 /* CommandLineRunner start(CustomerRepository customerRepository, AccountRepository accountRepository, AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha")
                    .forEach(name -> {
                        Customer customer = new Customer();
                        customer.setNom(name);
                        customer.setEmail(name + "@gmail.com");
                        customerRepository.save(customer);
                    });
            //creation d'un compte pour chaque customer
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                accountRepository.save(currentAccount);
            });
            customerRepository.findAll().forEach(customer -> {
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                accountRepository.save(savingAccount);
            });
            accountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setAmount(Math.random() * 15000);
                    accountOperation.setDate(new Date());
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);
                }
            });

        };*/


      // }
}

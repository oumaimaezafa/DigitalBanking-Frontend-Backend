package ma.oumaimaez.ebanckingapp.services;

import ma.oumaimaez.ebanckingapp.entities.BankAccount;
import ma.oumaimaez.ebanckingapp.entities.CurrentAccount;
import ma.oumaimaez.ebanckingapp.entities.SavingAccount;
import ma.oumaimaez.ebanckingapp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private AccountRepository accountRepository;

    public void consulter(){

        BankAccount bankAccount = accountRepository.findById("051a1342-671e-4cd9-b937-b7833238a52e").orElse(null);
        if (bankAccount != null) {
            System.out.println("*******************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getNom());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Over Draft => " + ((CurrentAccount) bankAccount).getOverDraft());
                ;
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate => " + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(op -> {
                System.out.println(op.getType() + "\t" + op.getDate() + "\t" + op.getAmount());
            });


        }
    };

}

package ma.oumaimaez.ebanckingapp.repository;

import ma.oumaimaez.ebanckingapp.entities.BankAccount;
import ma.oumaimaez.ebanckingapp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<BankAccount,String> {

    List<BankAccount> findByCustomerId(Long customerId);
}

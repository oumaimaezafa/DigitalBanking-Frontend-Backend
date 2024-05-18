package ma.oumaimaez.ebanckingapp.repository;

import ma.oumaimaez.ebanckingapp.entities.AccountOperation;
import ma.oumaimaez.ebanckingapp.entities.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
public List<AccountOperation> findByBankAccountId(String accountId);
    Page<AccountOperation> findByBankAccountIdOrderByDateDesc(String accountId, Pageable pageable);


}

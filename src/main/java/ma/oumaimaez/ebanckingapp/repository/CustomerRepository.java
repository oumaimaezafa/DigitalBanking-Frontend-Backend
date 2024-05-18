package ma.oumaimaez.ebanckingapp.repository;

import ma.oumaimaez.ebanckingapp.dtos.CustomerDTO;
import ma.oumaimaez.ebanckingapp.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.naming.Name;
import java.util.List;

public interface CustomerRepository  extends JpaRepository<Customer,Long> {

    @Query("select c from Customer c where c.nom like :kw ")
    List<Customer> searchCustomerByNom(@Param(value ="kw") String keyword);
}

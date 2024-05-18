package ma.oumaimaez.ebanckingapp.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.oumaimaez.ebanckingapp.dtos.CustomerDTO;
import ma.oumaimaez.ebanckingapp.entities.Customer;
import ma.oumaimaez.ebanckingapp.exceptions.CustomerNotFoundException;
import ma.oumaimaez.ebanckingapp.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;


    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping(path = "/customers/{customerId}")
    public CustomerDTO getCustomer(@PathVariable  Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomer(customerId);
    }
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);

    }
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping(path = "/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return  bankAccountService.updateCustomer(customerDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping(path = "/customers/{id}")
    public void deleteCustomer(@PathVariable Long id ){
        bankAccountService.deleteCustomer(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomer(@RequestParam(name="keyword" ,defaultValue = "") String keyword){
        return bankAccountService.searchCustomers("%"+keyword+"%");

    }
}

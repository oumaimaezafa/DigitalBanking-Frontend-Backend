package ma.oumaimaez.ebanckingapp.mappers;

import com.fasterxml.jackson.databind.util.BeanUtil;
import ma.oumaimaez.ebanckingapp.dtos.AccountOperationDTO;
import ma.oumaimaez.ebanckingapp.dtos.CurrentBankAccountDTO;
import ma.oumaimaez.ebanckingapp.dtos.CustomerDTO;
import ma.oumaimaez.ebanckingapp.dtos.SavingBankAccountDTO;
import ma.oumaimaez.ebanckingapp.entities.AccountOperation;
import ma.oumaimaez.ebanckingapp.entities.CurrentAccount;
import ma.oumaimaez.ebanckingapp.entities.Customer;
import ma.oumaimaez.ebanckingapp.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
//MapStruct
@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO=new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return  customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return  customer;
    }

    public SavingBankAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingBankAccountDTO savingBankAccountDTO =new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());

        return  savingBankAccountDTO;
    }
    public SavingAccount fromSavingAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
        SavingAccount savingAccount =new SavingAccount();
        BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
        return  savingAccount;

    }
    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount currentAccount){
         CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
         BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
         currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
         currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
         return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currenAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currenAccount);
        currenAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currenAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return accountOperationDTO;
    }
}

package ma.oumaimaez.ebanckingapp.dtos;

import jakarta.persistence.*;
import lombok.Data;
import ma.oumaimaez.ebanckingapp.entities.BankAccount;
import ma.oumaimaez.ebanckingapp.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long operation;
    private Date date;
    private double amount;
    private OperationType type;
    private String description;
}

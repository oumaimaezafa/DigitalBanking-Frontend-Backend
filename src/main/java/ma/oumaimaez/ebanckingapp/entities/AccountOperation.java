package ma.oumaimaez.ebanckingapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.oumaimaez.ebanckingapp.enums.OperationType;

import java.util.Date;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity
public class AccountOperation {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long operation;
    private Date date;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    private  BankAccount bankAccount;
    private String description;
}

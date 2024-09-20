package ma.zouhir.ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.zouhir.ebankingbackend.enumes.OperationType;

import java.math.BigDecimal;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountOperation {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType operationType;
    @ManyToOne
    private BankAccount bankAccount;
}

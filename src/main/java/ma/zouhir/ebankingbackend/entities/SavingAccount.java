package ma.zouhir.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//6@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {
    private double interestRate;
}

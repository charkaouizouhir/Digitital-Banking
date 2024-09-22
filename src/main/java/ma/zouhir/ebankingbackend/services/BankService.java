package ma.zouhir.ebankingbackend.services;

import jakarta.transaction.Transactional;
import ma.zouhir.ebankingbackend.entities.BankAccount;
import ma.zouhir.ebankingbackend.entities.CurrentAccount;
import ma.zouhir.ebankingbackend.entities.SavingAccount;
import ma.zouhir.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void consulter() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        System.out.println("****************************************************************************************************");

            BankAccount bankAccount =bankAccounts.get(0);
            System.out.println("Bank Account ID: " + bankAccount.getId());
            System.out.println("Balance: " + bankAccount.getBalance());
            System.out.println("Status: " + bankAccount.getStatus());
            System.out.println("Created At:" + bankAccount.getCreatedAt());

            if (bankAccount.getCustomer() != null) {
                System.out.println("Customer Name:" + bankAccount.getCustomer().getName());
            }

            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Overdraft:" + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Interest Rate:" + ((SavingAccount) bankAccount).getInterestRate());
            }

            bankAccount.getAccountOperations().forEach(operation -> {
                System.out.println("=========================================");
                System.out.println("Operation Type:" + operation.getOperationType());
                System.out.println("Bank Account:" + operation.getBankAccount().toString());
                System.out.println("Amount: " + operation.getAmount());
            });

    }
}

package ma.zouhir.ebankingbackend;

import ma.zouhir.ebankingbackend.entities.*;
import ma.zouhir.ebankingbackend.enumes.AccountStatus;
import ma.zouhir.ebankingbackend.enumes.OperationType;
import ma.zouhir.ebankingbackend.repositories.AccountOperationRepository;
import ma.zouhir.ebankingbackend.repositories.BankAccountRepository;
import ma.zouhir.ebankingbackend.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassin", "Aicha").forEach(name -> {
                // Création et sauvegarde d'un client
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);

            });
            customerRepository.findAll().forEach(customer->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(Math.random()*11000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(Math.random()*10);
                bankAccountRepository.save(savingAccount);
            });
        bankAccountRepository.findAll().forEach(bankAccount->{
            for(int i=0;i<10;i++){
                AccountOperation accountOperation = new AccountOperation();
                accountOperation.setBankAccount(bankAccount);
                accountOperation.setOperationDate(new Date());
                accountOperation.setAmount((int)(Math.random()*1000));
                accountOperation.setOperationType((Math.random() > 0.5) ? OperationType.CREDIT : OperationType.DEBIT);
                accountOperationRepository.save(accountOperation);
            }
        });


        };
    }
}

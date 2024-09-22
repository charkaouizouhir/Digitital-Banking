package ma.zouhir.ebankingbackend;

import ma.zouhir.ebankingbackend.entities.*;
import ma.zouhir.ebankingbackend.enums.AccountStatus;
import ma.zouhir.ebankingbackend.enums.OperationType;
import ma.zouhir.ebankingbackend.exceptions.BalanceNotEnoughException;
import ma.zouhir.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.zouhir.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.zouhir.ebankingbackend.repositories.AccountOperationRepository;
import ma.zouhir.ebankingbackend.repositories.BankAccountRepository;
import ma.zouhir.ebankingbackend.repositories.CustomerRepository;
import ma.zouhir.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Hassan","Iman","Mohamed").forEach(name->{
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@mail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomer().forEach(customer ->{
                System.out.println(customer.toString());

                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 9000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 120200, 5.5, customer.getId());
                } catch (CustomerNotFoundException e) {
                    throw new RuntimeException(e);
                }
                List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
                for (BankAccount bankAccount : bankAccounts) {
                    try {
                        BankAccount b= bankAccountService.getBankAccount(bankAccount.getId()) ;
                        bankAccountService.credit(b.getId(),120000+(int)(Math.random()*10000),"credit");
                        bankAccountService.debit(b.getId(),90000+(int)(Math.random()*10000),"debit");
                    } catch (BankAccountNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (BalanceNotEnoughException e) {
                        throw new RuntimeException(e);
                    }
                    List<AccountOperation> operations =bankAccount.getAccountOperations();
                    for (AccountOperation accountOperation : operations) {
                        System.out.println(accountOperation.toString());
                    }

                }

            });
    };
   }



    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassin", "Aicha").forEach(name -> {
                // Create and save customer
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                // Create and save current account
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 9000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(Math.random() * 11000);
                bankAccountRepository.save(currentAccount);

                // Create and save saving account
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 9000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(Math.random() * 10);
                bankAccountRepository.save(savingAccount);
            });

            // Create and save account operations
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i < 10; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setBankAccount(bankAccount);
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount((int) (Math.random() * 1000));
                    accountOperation.setOperationType((Math.random() > 0.5) ? OperationType.CREDIT : OperationType.DEBIT);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }
}

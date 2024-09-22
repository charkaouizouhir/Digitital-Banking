package ma.zouhir.ebankingbackend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.zouhir.ebankingbackend.entities.*;
import ma.zouhir.ebankingbackend.enums.AccountStatus;
import ma.zouhir.ebankingbackend.enums.OperationType;
import ma.zouhir.ebankingbackend.exceptions.BalanceNotEnoughException;
import ma.zouhir.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.zouhir.ebankingbackend.exceptions.CustomerNotFoundException;
import ma.zouhir.ebankingbackend.repositories.AccountOperationRepository;
import ma.zouhir.ebankingbackend.repositories.BankAccountRepository;
import ma.zouhir.ebankingbackend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final AccountOperationRepository accountOperationRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new customer with name: {}", customer.getName());
        return customerRepository.save(customer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(Double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        currentAccount.setStatus(Math.random()>0.5? AccountStatus.CREATED:AccountStatus.ACTIVATED);
        log.info("Saving current account for customer {}", customer.getName());
        return bankAccountRepository.save(currentAccount);
    }

    @Override
    public SavingAccount saveSavingBankAccount(Double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        savingAccount.setStatus(Math.random()>0.5? AccountStatus.CREATED:AccountStatus.ACTIVATED);
        log.info("Saving saving account for customer {}", customer.getName());
        return bankAccountRepository.save(savingAccount);
    }

    @Override
    public List<Customer> listCustomer() {
        log.info("Fetching all customers");
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        log.info("Fetching bank account with ID: {}", accountId);
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotEnoughException, BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotEnoughException("Not enough balance for this operation");
        }
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
        log.info("Debited {} from account ID: {}", amount, accountId);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
        log.info("Credited {} to account ID: {}", amount, accountId);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotEnoughException, BankAccountNotFoundException {
        debit(accountIdSource, amount, "Transfer to account " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from account " + accountIdSource);
        log.info("Transferred {} from account ID: {} to account ID: {}", amount, accountIdSource, accountIdDestination);
    }

    @Override
    public List<BankAccount> bankAccountList() {
        log.info("Fetching all bank accounts");
        return bankAccountRepository.findAll();
    }
}

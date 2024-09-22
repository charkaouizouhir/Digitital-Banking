package ma.zouhir.ebankingbackend.services;

import ma.zouhir.ebankingbackend.entities.BankAccount;
import ma.zouhir.ebankingbackend.entities.CurrentAccount;
import ma.zouhir.ebankingbackend.entities.Customer;
import ma.zouhir.ebankingbackend.entities.SavingAccount;
import ma.zouhir.ebankingbackend.exceptions.BalanceNotEnoughException;
import ma.zouhir.ebankingbackend.exceptions.BankAccountNotFoundException;
import ma.zouhir.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);
    CurrentAccount saveCurrentBankAccount(Double intialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(Double intialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<Customer> listCustomer();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotEnoughException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;
    void transfer(String AccountIdSource,String AccountIdDestination,double amount) throws BalanceNotEnoughException, BankAccountNotFoundException;
    List<BankAccount> bankAccountList();
}

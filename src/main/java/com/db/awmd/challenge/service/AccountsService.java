package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  public String sendMoney(String accountToId, String accountFromId, BigDecimal amount) {
if (amount.compareTo(BigDecimal.ONE) < 0)
   throw new InvalidAmountException("Kindly enter the amount which is more than one");
Account fromAccount = this.accountsRepository.getAccount(accountFromId);
Account toAccount = this.accountsRepository.getAccount(accountToId);
if (ObjectUtils.isEmpty(fromAccount) || ObjectUtils.isEmpty(toAccount))
   throw new CustomerNotRegisteredException();
if (fromAccount.getBalance().compareTo(amount) < 0) {
   throw new InsufficientFundsException(amount);
}
fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
toAccount.setBalance(toAccount.getBalance().add(amount));
this.accountsRepository.updateAccount(fromAccount);
this.accountsRepository.updateAccount(toAccount);
notificationService.notifyAboutTransfer(fromAccount, amount + "transferred from" + fromAccount);
notificationService.notifyAboutTransfer(toAccount, amount + "transferred to" + toAccount);
return "funds transferred successfully";
    }
  
}

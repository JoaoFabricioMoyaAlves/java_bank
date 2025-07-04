package java_bank.model;

import static java_bank.model.BankService.INVESTMENT;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;


public class InvestimentWallet extends Wallet {

    private final Investiment investiment;
    private final AccountWallet account;

   public InvestimentWallet(final Investiment investiment, final AccountWallet account, final long amount){
    super(INVESTMENT);
    this.investiment = investiment;
    this.account = account;
    addMoney(account.reduceMoney(amount), getService(), "investimento");
   }

   public AccountWallet getAccount(){
     return this.account;
   }

   public Investiment getInvestment(){
     return investiment;
   }

   public void updateAmount(final long percent){
        var amount = getFounds() * percent / 100;
        var history = new MoneyAudit(UUID.randomUUID(), getService(), "rendimentos", OffsetDateTime.now());
        var money = Stream.generate(()-> new Money(history)).limit(amount).toList();
        this.money.addAll(money);
   }


}

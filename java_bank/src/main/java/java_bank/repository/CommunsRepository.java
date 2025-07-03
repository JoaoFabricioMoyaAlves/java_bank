package java_bank.repository;
import static java_bank.model.BankService.ACCOUNT;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.List;
import java_bank.model.Money;
import java_bank.model.MoneyAudit;
import java_bank.expcetion.NoFoundsEnoughException;
import java_bank.model.AccountWallet;
import java_bank.model.Wallet;

public final class CommunsRepository {
    

    public static void checkFoundsForTransaction(final AccountWallet source, final long amount){
        if(source.getFounds()<amount){
            throw new NoFoundsEnoughException("Sua conta não tem dinheiro o suficiente para realizar essa transação");

        }

    }
    

    public static void checkFoundsForTransaction(final Wallet source, final long amount){
        if(source.getFounds()<amount){
            throw new NoFoundsEnoughException("Sua conta não tem dinheiro o suficiente para realizar essa transação");

        }

    }

    public static List<Money> generateMoney(final UUID transactionId, final long funds, final String description){
        var history = new MoneyAudit(transactionId, ACCOUNT, description, OffsetDateTime.now());
        return Stream.generate(()-> new Money(history)).limit(funds).toList();
    }

}

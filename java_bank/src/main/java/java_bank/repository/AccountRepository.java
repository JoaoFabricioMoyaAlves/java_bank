package java_bank.repository;

import java_bank.model.AccountWallet;
import static java_bank.repository.CommunsRepository.checkFoundsForTransaction;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java_bank.expcetion.AccountNotFoundException;
import java_bank.expcetion.PixUseException;

import java.util.stream.Stream;
import java_bank.repository.CommunsRepository;
//import javax.security.auth.login.AccountNotFoundException;


public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialFunds){
      if(!accounts.isEmpty()){
        var pixInUse =  accounts.stream().flatMap(a -> a.getPix().stream()).toList();
        for (var p : pix) {
          if( pixInUse.contains(p)){
 
            throw new PixUseException("O pix "+p+ " já está em uso");
          }
        }
    }
        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }
    public  List<AccountWallet> getHistory(){
        return accounts;
    }

    public void deposit(final String pix, final long foundsAmount){
        var target = findByPix(pix);
        target.addMoney(foundsAmount, "depósito");
    }

    public long withdraw(final String pix, final long amount){

        var source = findByPix(pix);
        checkFoundsForTransaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source = findByPix(targetPix);
        checkFoundsForTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = "pix enviado de "+sourcePix+ " para "+ targetPix +" ";
        target.addMoney(source.reduceMoney(amount), source.getService(), message);
    }


    public AccountWallet findByPix(final String pix){
    return accounts.stream()
    .filter(a -> a.getPix().contains(pix))
    .findFirst()
    .orElseThrow(()-> new AccountNotFoundException("A conta com a chave pix "+pix+ " nao existe ou foi encerrada"));
    }

    public List<AccountWallet> list(){
        return this.accounts;
    }

    
    
}

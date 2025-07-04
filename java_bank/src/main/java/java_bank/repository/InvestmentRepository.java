package java_bank.repository;
import static java_bank.repository.CommunsRepository.checkFoundsForTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import java_bank.expcetion.AccountWithInvestmentException;
import java_bank.expcetion.InvestmentNotFoundException;
import java_bank.expcetion.PixUseException;
import java_bank.expcetion.WalletNotFoundException;
import java_bank.model.AccountWallet;
import java_bank.model.Investiment;
import java_bank.model.InvestimentWallet;

public class InvestmentRepository {
    
    private long nextId = 0;
    private final List<Investiment> investiments = new ArrayList<>();
    private final List<InvestimentWallet> wallets = new ArrayList<>();

    public Investiment create(final long tax, final long initialFunds){
        this.nextId++;
       var investment = new Investiment(this.nextId, tax, initialFunds);
       investiments.add(investment);
       return investment;
    }


    public InvestimentWallet initInvestment(final AccountWallet account, final long id){
          
        if(!wallets.isEmpty()){
           var accountInUse =  wallets.stream().map(InvestimentWallet::getAccount).toList();
       
          if( accountInUse.contains(account)){
 
            throw new AccountWithInvestmentException("A conta "+account+ " já possui um investimento");
          }
        
        }
        var investmant = findById(id);
        checkFoundsForTransaction(account, investmant.initialFunds());
        var wallet = new InvestimentWallet(investmant, account, investmant.initialFunds());
        wallets.add(wallet);
        return wallet;


    }


    public InvestimentWallet deposit(final String pix, final long funds){
         var wallet = findWalletByAccountPix(pix);
         wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "Investimento");
         return wallet;
    }

    public InvestimentWallet withdraw(final String pix, final long funds){
        var wallet = findWalletByAccountPix(pix);
        checkFoundsForTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "saque de investimentos");
        if(wallet.getFounds() == 0){
            wallets.remove(wallet);
        }
        return wallet;
    }

    public void updateAmount(){
        wallets.forEach(w -> w.updateAmount(w.getInvestment().tax()));
    }

    public Investiment findById(final long id){
        return investiments.stream().filter(a -> a.id() == id)
                                    .findFirst()
                                    .orElseThrow(()-> new InvestmentNotFoundException("Investimento "+id+" não foi encontrado"));
    }

    public InvestimentWallet findWalletByAccountPix(final String pix){
     return wallets.stream()
                   .filter(w ->w.getAccount().getPix().contains(pix))
                   .findFirst()
                   .orElseThrow(()-> new WalletNotFoundException("A carteira não foi encontrada"));
    }

    public List<InvestimentWallet> listWallets(){
        return this.wallets;
    }

    public List<Investiment> list(){
        return this.investiments;
    }

}

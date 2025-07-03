package java_bank.model;

import java.util.ArrayList;
import java.util.List;

public class Money {
    private final List<MoneyAudit> history = new ArrayList<>();

    public Money(MoneyAudit history){
       this.history.add(history);
    }

    public List<MoneyAudit> getHistory(){
        return history;
    }

    public void addHistory(MoneyAudit history){
    this.history.add(history);
    }

}

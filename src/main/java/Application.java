import java.util.ArrayList;
import java.util.List;

public class Application {

    public static void main(String... args) {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade("New", "Open", "42", "CLIENT42", "Option", 42.0, "EURUSD", 1.0, 11.0, 1.1, 2.1));
        trades.add(new Trade("New", "Open", "43", "CLIENT42", "Option", 43.0, "EURUSD", 2.0, 12.0, 1.2, 2.2));
        trades.add(new Trade("New", "Closed", "44", "CLIENT42", "Option", 44.0, "EURUSD", 3.0, 13.0, 1.3, 2.3));
        trades.add(new Trade("New", "Closed", "45", "CLIENT42", "Option", 45.0, "EURUSD", 4.0, 14.0, 1.4, 2.4));
        trades.add(new Trade("New", "Closed", "46", "CLIENT42", "Option", 46.0, "EURUSD", 5.0, 15.0, 1.5, 2.5));

        System.out.println("======");
        trades.stream()
                .filter(TradeFilterFactory.parseExpression("trade status = \"Open\""))
                .forEach(trade -> System.out.println(trade.getTradeId()));
//        ======
//        42
//        43

        System.out.println("======");
        trades.stream()
                .filter(TradeFilterFactory.parseExpression("trade status = \"Open\" or spot > 2.4"))
                .forEach(trade -> System.out.println(trade.getTradeId()));
//        ======
//        42
//        43
//        46

        System.out.println("======");
        trades.stream()
                .filter(TradeFilterFactory.parseExpression("spot >= 2.2 and (strike < 1.3 or strike > 1.4)"))
                .forEach(trade -> System.out.println(trade.getTradeId()));
//        ======
//        43
//        46

        System.out.println("======");
        trades.stream()
                .filter(TradeFilterFactory.parseExpression("spot >= 2.2 and (strike < 1.3 or strike > 1.4 or (ccy pair = \"EURUSD\" and strike > 1.3))"))
                .forEach(trade -> System.out.println(trade.getTradeId()));
//        ======
//        43
//        45
//        46
    }
}

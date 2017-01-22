import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TradeFilter implements Predicate<Trade> {

    public TradeFilter(String expression) {
        
    }

    @Override
    public boolean test(Trade trade) {
        return false;
    }
}

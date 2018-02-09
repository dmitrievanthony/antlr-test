import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TradeFilterFactory {

    public static Predicate<Trade> parseExpression(String expression) {
        ANTLRInputStream inputStream = new ANTLRInputStream(expression);
        WhereClauseLexer lexer = new WhereClauseLexer(inputStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        WhereClauseParser parser = new WhereClauseParser(tokens);
        ParseTree tree = parser.expression();

        WhereClauseBaseVisitor<Predicate<Trade>> expressionVisitor = new WhereClauseBaseVisitor<Predicate<Trade>>() {

            @Override
            public Predicate<Trade> visitExpression(WhereClauseParser.ExpressionContext ctx) {
                List<Predicate<Trade>> atomicPredicates = ctx.atomic().stream().map(this::visitAtomic).collect(Collectors.toList());
                return trade -> {
                    boolean flag = atomicPredicates.get(0).test(trade);
                    for (int i = 0; i < ctx.andOrOperator().size(); i++) {
                        if (ctx.andOrOperator(i).AND() != null) {
                            // it's "and" operator
                            flag = flag & this.visitAtomic(ctx.atomic(i + 1)).test(trade);
                        }
                        else {
                            // it's "or" operator
                            if (flag)
                                return true;
                            else
                                flag = this.visitAtomic(ctx.atomic(i + 1)).test(trade);
                        }
                    }
                    return flag;
                };
            }

            @Override
            public Predicate<Trade> visitAtomic(WhereClauseParser.AtomicContext ctx) {
                if (ctx.compositPredicate() != null)
                    return this.visitCompositPredicate(ctx.compositPredicate());
                else
                    return this.visitAtomic(ctx.atomic());
            }

            @Override
            public Predicate<Trade> visitCompositPredicate(WhereClauseParser.CompositPredicateContext ctx) {
                return trade -> {
                    boolean flag = this.visitPredicate(ctx.predicate()).test(trade);
                    for (int i = 0; i < ctx.atomic().size(); i++) {
                        if (ctx.andOrOperator(i).AND() != null) {
                            // it's "and" operator
                            flag = flag & this.visitAtomic(ctx.atomic(i)).test(trade);
                        }
                        else {
                            // it's "or" operator
                            if (flag)
                                return true;
                            else
                                flag = this.visitAtomic(ctx.atomic(i)).test(trade);
                        }
                    }
                    return flag;
                };
            }

            @Override
            public Predicate<Trade> visitPredicate(WhereClauseParser.PredicateContext ctx) {
                if (ctx.stringColumn().size() > 0) {
                    // string column
                    return trade -> {
                        String leftArg;
                        if (ctx.stringColumn(0).TRADE_EVENT_COLUMN() != null)
                            leftArg = trade.getTradeEvent();
                        else if (ctx.stringColumn(0).TRADE_STATUS_COLUMN() != null)
                            leftArg = trade.getTradeStatus();
                        else if (ctx.stringColumn(0).TRADE_ID_COLUMN() != null)
                            leftArg = trade.getTradeId();
                        else if (ctx.stringColumn(0).CLIENT_ID_COLUMN() != null)
                            leftArg = trade.getClientId();
                        else if (ctx.stringColumn(0).CCY_PAIR_COLUMN() != null)
                            leftArg = trade.getCcyPair();
                        else if (ctx.stringColumn(0).PRODUCT_TYPE_COLUMN() != null)
                            leftArg = trade.getProductType();
                        else
                            throw new RuntimeException();

                        String rightArg;
                        if (ctx.stringColumn().size() > 1) {
                            if (ctx.stringColumn(1).TRADE_EVENT_COLUMN() != null)
                                rightArg = trade.getTradeEvent();
                            else if (ctx.stringColumn(1).TRADE_STATUS_COLUMN() != null)
                                rightArg = trade.getTradeStatus();
                            else if (ctx.stringColumn(1).TRADE_ID_COLUMN() != null)
                                rightArg = trade.getTradeId();
                            else if (ctx.stringColumn(1).CLIENT_ID_COLUMN() != null)
                                rightArg = trade.getClientId();
                            else if (ctx.stringColumn(1).CCY_PAIR_COLUMN() != null)
                                rightArg = trade.getCcyPair();
                            else if (ctx.stringColumn(1).PRODUCT_TYPE_COLUMN() != null)
                                rightArg = trade.getProductType();
                            else
                                throw new RuntimeException();
                        }
                        else
                            rightArg = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
                        if (ctx.stringComparisonOperator().EQ() != null)
                            return leftArg.equals(rightArg);
                        else if (ctx.stringComparisonOperator().NEQ() != null)
                            return !leftArg.equals(rightArg);
                        else if (ctx.stringComparisonOperator().STARTS_WITH() != null)
                            return leftArg.startsWith(rightArg);
                        else
                            throw new RuntimeException();
                    };
                }
                else {
                    // numeric column
                    return trade -> {
                        Double leftArg;
                        if (ctx.numericColumn(0).LOW_BARRIER_COLUMN() != null)
                            leftArg = trade.getLowBarrier();
                        else if (ctx.numericColumn(0).NOMINAL_VALUE_COLUMN() != null)
                            leftArg = trade.getNominalValue();
                        else if (ctx.numericColumn(0).SPOT_COLUMN() != null)
                            leftArg = trade.getSpot();
                        else if (ctx.numericColumn(0).STRIKE_COLUMN() != null)
                            leftArg = trade.getStrike();
                        else if (ctx.numericColumn(0).UP_BARRIER_COLUMN() != null)
                            leftArg = trade.getStrike();
                        else
                            throw new RuntimeException();

                        Double rightArg;
                        if (ctx.numericColumn().size() > 1) {
                            if (ctx.numericColumn(1).LOW_BARRIER_COLUMN() != null)
                                rightArg = trade.getLowBarrier();
                            else if (ctx.numericColumn(1).NOMINAL_VALUE_COLUMN() != null)
                                rightArg = trade.getNominalValue();
                            else if (ctx.numericColumn(1).SPOT_COLUMN() != null)
                                rightArg = trade.getSpot();
                            else if (ctx.numericColumn(1).STRIKE_COLUMN() != null)
                                rightArg = trade.getStrike();
                            else if (ctx.numericColumn(1).UP_BARRIER_COLUMN() != null)
                                rightArg = trade.getStrike();
                            else
                                throw new RuntimeException();
                        }
                        else
                            rightArg = Double.valueOf(ctx.NUMERIC().getText());
                        if (ctx.numericComparisonOperaotr().EQ() != null)
                            return leftArg.equals(rightArg);
                        else if (ctx.numericComparisonOperaotr().NEQ() != null)
                            return !leftArg.equals(rightArg);
                        else if (ctx.numericComparisonOperaotr().GTE() != null)
                            return leftArg >= rightArg;
                        else if (ctx.numericComparisonOperaotr().GTH() != null)
                            return leftArg > rightArg;
                        else if (ctx.numericComparisonOperaotr().LTE() != null)
                            return leftArg <= rightArg;
                        else if (ctx.numericComparisonOperaotr().LTH() != null)
                            return leftArg < rightArg;
                        else
                            throw new RuntimeException();
                    };
                }
            }
        };

        return expressionVisitor.visit(tree);
    }
}

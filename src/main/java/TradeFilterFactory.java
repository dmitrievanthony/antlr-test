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
                            if (flag) {
                                return true;
                            }
                            else {
                                flag = this.visitAtomic(ctx.atomic(i + 1)).test(trade);
                            }
                        }
                    }
                    return flag;
                };
            }

            @Override
            public Predicate<Trade> visitAtomic(WhereClauseParser.AtomicContext ctx) {
                if (ctx.compositPredicate() != null) {
                    return this.visitCompositPredicate(ctx.compositPredicate());
                }
                else {
                    return this.visitAtomic(ctx.atomic());
                }
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
                            if (flag) {
                                return true;
                            }
                            else {
                                flag = this.visitAtomic(ctx.atomic(i)).test(trade);
                            }
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
                        String leftArgument = null;
                        if (ctx.stringColumn(0).TRADE_EVENT_COLUMN() != null) {
                            leftArgument = trade.getTradeEvent();
                        }
                        else if (ctx.stringColumn(0).TRADE_STATUS_COLUMN() != null) {
                            leftArgument = trade.getTradeStatus();
                        }
                        else if (ctx.stringColumn(0).TRADE_ID_COLUMN() != null) {
                            leftArgument = trade.getTradeId();
                        }
                        else if (ctx.stringColumn(0).CLIENT_ID_COLUMN() != null) {
                            leftArgument = trade.getClientId();
                        }
                        else if (ctx.stringColumn(0).CCY_PAIR_COLUMN() != null) {
                            leftArgument = trade.getCcyPair();
                        }
                        else if (ctx.stringColumn(0).PRODUCT_TYPE_COLUMN() != null) {
                            leftArgument = trade.getProductType();
                        }
                        else {
                            throw new RuntimeException();
                        }
                        String rightArgument = null;
                        if (ctx.stringColumn().size() > 1) {
                            if (ctx.stringColumn(1).TRADE_EVENT_COLUMN() != null) {
                                rightArgument = trade.getTradeEvent();
                            }
                            else if (ctx.stringColumn(1).TRADE_STATUS_COLUMN() != null) {
                                rightArgument = trade.getTradeStatus();
                            }
                            else if (ctx.stringColumn(1).TRADE_ID_COLUMN() != null) {
                                rightArgument = trade.getTradeId();
                            }
                            else if (ctx.stringColumn(1).CLIENT_ID_COLUMN() != null) {
                                rightArgument = trade.getClientId();
                            }
                            else if (ctx.stringColumn(1).CCY_PAIR_COLUMN() != null) {
                                rightArgument = trade.getCcyPair();
                            }
                            else if (ctx.stringColumn(1).PRODUCT_TYPE_COLUMN() != null) {
                                rightArgument = trade.getProductType();
                            }
                            else {
                                throw new RuntimeException();
                            }
                        }
                        else {
                            rightArgument = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
                        }
                        if (ctx.stringComparisonOperator().EQ() != null) {
                            return leftArgument.equals(rightArgument);
                        }
                        else if (ctx.stringComparisonOperator().NEQ() != null) {
                            return !leftArgument.equals(rightArgument);
                        }
                        else if (ctx.stringComparisonOperator().STARTS_WITH() != null) {
                            return leftArgument.startsWith(rightArgument);
                        }
                        else {
                            throw new RuntimeException();
                        }
                    };
                }
                else {
                    // numeric column
                    return trade -> {
                        Double leftArgument = null;
                        if (ctx.numericColumn(0).LOW_BARRIER_COLUMN() != null) {
                            leftArgument = trade.getLowBarrier();
                        }
                        else if (ctx.numericColumn(0).NOMINAL_VALUE_COLUMN() != null) {
                            leftArgument = trade.getNominalValue();
                        }
                        else if (ctx.numericColumn(0).SPOT_COLUMN() != null) {
                            leftArgument = trade.getSpot();
                        }
                        else if (ctx.numericColumn(0).STRIKE_COLUMN() != null) {
                            leftArgument = trade.getStrike();
                        }
                        else if (ctx.numericColumn(0).UP_BARRIER_COLUMN() != null) {
                            leftArgument = trade.getStrike();
                        }
                        else {
                            throw new RuntimeException();
                        }
                        Double rightArgument = null;
                        if (ctx.numericColumn().size() > 1) {
                            if (ctx.numericColumn(1).LOW_BARRIER_COLUMN() != null) {
                                rightArgument = trade.getLowBarrier();
                            }
                            else if (ctx.numericColumn(1).NOMINAL_VALUE_COLUMN() != null) {
                                rightArgument = trade.getNominalValue();
                            }
                            else if (ctx.numericColumn(1).SPOT_COLUMN() != null) {
                                rightArgument = trade.getSpot();
                            }
                            else if (ctx.numericColumn(1).STRIKE_COLUMN() != null) {
                                rightArgument = trade.getStrike();
                            }
                            else if (ctx.numericColumn(1).UP_BARRIER_COLUMN() != null) {
                                rightArgument = trade.getStrike();
                            }
                            else {
                                throw new RuntimeException();
                            }
                        }
                        else {
                            rightArgument = Double.valueOf(ctx.NUMERIC().getText());
                        }
                        if (ctx.numericComparisonOperaotr().EQ() != null) {
                            return leftArgument.equals(rightArgument);
                        }
                        else if (ctx.numericComparisonOperaotr().NEQ() != null) {
                            return !leftArgument.equals(rightArgument);
                        }
                        else if (ctx.numericComparisonOperaotr().GTE() != null) {
                            return leftArgument >= rightArgument;
                        }
                        else if (ctx.numericComparisonOperaotr().GTH() != null) {
                            return leftArgument > rightArgument;
                        }
                        else if (ctx.numericComparisonOperaotr().LTE() != null) {
                            return leftArgument <= rightArgument;
                        }
                        else if (ctx.numericComparisonOperaotr().LTH() != null) {
                            return leftArgument < rightArgument;
                        }
                        else {
                            throw new RuntimeException();
                        }
                    };
                }
            }
        };

        return expressionVisitor.visit(tree);
    }
}

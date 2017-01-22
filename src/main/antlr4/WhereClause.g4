grammar WhereClause;

TRADE_EVENT_COLUMN : 'trade event';
TRADE_STATUS_COLUMN : 'trade status';
TRADE_ID_COLUMN : 'trade id';
CLIENT_ID_COLUMN : 'client id';
PRODUCT_TYPE_COLUMN : 'product type';
NOMINAL_VALUE_COLUMN : 'nominal value';
CCY_PAIR_COLUMN : 'ccy pair';
LOW_BARRIER_COLUMN : 'low barrier';
UP_BARRIER_COLUMN : 'up barrier';
STRIKE_COLUMN : 'strike';
SPOT_COLUMN : 'spot';
AND: 'and' ;
OR: 'or' ;
EQ : '=' ;
NEQ: '!=' ;
LTH : '<' ;
LTE : '<=' ;
GTH : '>' ;
GTE : '>=' ;
STARTS_WITH : 'starts with';
LPAREN : '(' ;
RPAREN : ')' ;
WS : ( ' ' | '\t' )+ ;
STRING : '"' (~[\\"] | '\\' [\\"])* '"' ;
NUMERIC : [0-9]+('.'[0-9]+)? ;

andOrOperator
    : AND
    | OR
    ;

stringColumn
    : TRADE_EVENT_COLUMN
    | TRADE_STATUS_COLUMN
    | TRADE_ID_COLUMN
    | CLIENT_ID_COLUMN
    | PRODUCT_TYPE_COLUMN
    | CCY_PAIR_COLUMN
    ;

numericColumn
    : NOMINAL_VALUE_COLUMN
    | LOW_BARRIER_COLUMN
    | UP_BARRIER_COLUMN
    | STRIKE_COLUMN
    | SPOT_COLUMN
    ;

stringComparisonOperator
    : EQ
    | NEQ
    | STARTS_WITH
    ;

numericComparisonOperaotr
    : EQ
    | NEQ
    | LTH
    | LTE
    | GTH
    | GTE
    ;

predicate
    : stringColumn WS stringComparisonOperator WS stringColumn
    | stringColumn WS stringComparisonOperator WS STRING
    | numericColumn WS numericComparisonOperaotr WS numericColumn
    | numericColumn WS numericComparisonOperaotr WS NUMERIC
    ;

compositPredicate
    : predicate ( WS andOrOperator WS atomic )*
    ;

atomic
    : compositPredicate
    | LPAREN WS? atomic WS? RPAREN
    ;

expression:
    atomic ( WS andOrOperator WS atomic)* EOF
    ;


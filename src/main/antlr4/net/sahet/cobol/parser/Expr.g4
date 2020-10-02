grammar Expr; //name of grammar

//parser rules - starts with lower case
prog: (expr NEW_LINE)*;
expr:
	expr ('*' | '/') expr
	| ('+' | '-') expr
	| INT
	| '(' expr ')';

//lexer rules - starts with upper cases
NEW_LINE: [\r\n]+;
INT: [0-9]+;
WS: [ \t\r\n]+ -> skip;

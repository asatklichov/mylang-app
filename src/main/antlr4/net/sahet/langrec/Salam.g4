grammar Salam;
rec : 'salam' ID;
ID  : [a-z]+;
WS  : [\t\r\n]+ -> skip; 

/* JFlex example: part of Java language lexer specification */
package ru.spbau.mit.dbmsau.syntax;

import java_cup.runtime.*;
import ru.spbau.mit.dbmsau.syntax.LexemeType;
import ru.spbau.mit.dbmsau.syntax.ast.*;
import ru.spbau.mit.dbmsau.syntax.exception.LexicalError;

%%
%class GeneratedLexer
%unicode
%cup
%line
%column
%caseless
%{
  StringBuffer string = new StringBuffer();
  int stringBeginLine,stringBeginColumn;

  private Symbol symbol(int type) {
    return symbol(type, yytext().toString().toLowerCase());
  }
  private Symbol symbol(int type, Object value) {
    TerminalNode node = new TerminalNode(value, type, yyline+1, yycolumn+1, yylength());
    Symbol s = new Symbol(type, yyline+1, yycolumn+1, node);
    return s;
  }

  public int getLine() {
        return yyline+1;
  }
  public int getColumn() {
        return yycolumn+1;
  }
%}
%eofval{ 
return symbol(LexemeType.EOF);
%eofval}
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier = [:jletter:] [:jletterdigit:]*

Sign = [+-]

DecIntegerLiteral = (0 | [1-9][0-9]*)
DecDoubleLiteral = (0 | [1-9][0-9]*)  "." [0-9]+

%state STRING

%%
 /* keywords */
<YYINITIAL> {
"CREATE"         { return symbol(LexemeType.CREATE); }
"QUIT"           { return symbol(LexemeType.QUIT); }
"SELECT"         { return symbol(LexemeType.SELECT); }
"DELETE"         { return symbol(LexemeType.DELETE); }
"FROM"           { return symbol(LexemeType.FROM); }
"TABLE"          { return symbol(LexemeType.TABLE); }
"INSERT"         { return symbol(LexemeType.INSERT); }
"INTO"           { return symbol(LexemeType.INTO); }
"VALUES"         { return symbol(LexemeType.VALUES); }
"WHERE"          { return symbol(LexemeType.WHERE); }
"JOIN"           { return symbol(LexemeType.JOIN); }
"UPDATE"         { return symbol(LexemeType.UPDATE); }
"SET"            { return symbol(LexemeType.SET); }
"ON"             { return symbol(LexemeType.ON); }
"INDEX"          { return symbol(LexemeType.INDEX); }
"LOAD"           { return symbol(LexemeType.LOAD); }
"DUMP"           { return symbol(LexemeType.DUMP); }
"FOR"            { return symbol(LexemeType.FOR); }
"NOT"            { return symbol(LexemeType.NOT); }
"AND"            { return symbol(LexemeType.AND); }
"OR"             { return symbol(LexemeType.OR); }
"/"              { return symbol(LexemeType.DIV); }
"%"              { return symbol(LexemeType.MOD); }
"("              { return symbol(LexemeType.LEFTPAR); }
")"              { return symbol(LexemeType.RIGHTPAR); }
";"              { return symbol(LexemeType.EOF); }
<<EOF>>          { return symbol(LexemeType.EOF); }
","              { return symbol(LexemeType.COMMA); }
"="              { return symbol(LexemeType.EQUALS); }
":"              { return symbol(LexemeType.COLON); }
"<>"             { return symbol(LexemeType.NOTEQUAL); }
"<"              { return symbol(LexemeType.LESS); }
"<="             { return symbol(LexemeType.LESSOREQUAL); }
">"              { return symbol(LexemeType.MORE); }
">="             { return symbol(LexemeType.MOREOREQUAL); }
"+"              { return symbol(LexemeType.PLUS); }
"-"              { return symbol(LexemeType.MINUS); }
"*"              { return symbol(LexemeType.ASTERISK); }
"["              { return symbol(LexemeType.LEFTBRACKET); }
"]"              { return symbol(LexemeType.RIGHTBRACKET); }
"."              { return symbol(LexemeType.DOT); }


{DecIntegerLiteral} { return symbol(LexemeType.INTEGER_LITERAL, Integer.parseInt(yytext())); }
{DecDoubleLiteral}  { return symbol(LexemeType.DOUBLE_LITERAL, Double.parseDouble(yytext())); }

{Identifier}        { return symbol(LexemeType.IDENT); }

{WhiteSpace}                   { /* ignore */ }
'                             { string.setLength(0); 
                                stringBeginLine=yyline+1;
                                stringBeginColumn=yycolumn+1; 
                                yybegin(STRING); 
                            }
}
<STRING> {
'                            { yybegin(YYINITIAL); 
                                    TerminalNode node = new TerminalNode(string.toString(), 
                                                LexemeType.STRING_LITERAL, stringBeginLine,
                                                stringBeginColumn, yyline, yycolumn
                                                );
                                   Symbol s =  new Symbol(LexemeType.STRING_LITERAL, 
                                   stringBeginLine,
                                   stringBeginColumn,
                                   node);
                                   return s;
                             }
  [^'\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\'                           { string.append('\''); }
  \\                             { string.append('\\'); }
<<EOF>>                     { throw new LexicalError("Expected end of string literial", yyline+1, yycolumn+1); }
}

 /* error fallback */
.|\n                             { throw new LexicalError("Illegal character <"+
                                                    yytext()+">", yyline+1, yycolumn+1); }

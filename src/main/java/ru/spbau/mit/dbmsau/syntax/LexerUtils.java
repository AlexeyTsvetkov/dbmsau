/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.spbau.mit.dbmsau.syntax;


public class LexerUtils {
    public static String getLexemeTypeName(int id) {
        switch (id) {
            case LexemeType.EOF: {
                return "EOF";
            }
            case LexemeType.OR: {
                return "OR";
            }
            case LexemeType.DIV: {
                return "DIV";
            }
            case LexemeType.MOD: {
                return "MOD";
            }
            case LexemeType.AND: {
                return "AND";
            }
            case LexemeType.MAIN: {
                return "MAIN";
            }
            case LexemeType.LEFTPAR: {
                return "LEFTPAR";
            }
            case LexemeType.COMMA: {
                return "COMMA";
            }
            case LexemeType.EQUALS: {
                return "EQUALS";
            }
            case LexemeType.COLON: {
                return "COLON";
            }
            case LexemeType.NOT: {
                return "NOT";
            }
            case LexemeType.NOTEQUAL: {
                return "NOTEQUAL";
            }
            case LexemeType.LESS: {
                return "LESS";
            }
            case LexemeType.LESSOREQUAL: {
                return "LESSOREQUAL";
            }
            case LexemeType.MORE: {
                return "MORE";
            }
            case LexemeType.MOREOREQUAL: {
                return "MOREOREQUAL";
            }
            case LexemeType.PLUS: {
                return "PLUS";
            }
            case LexemeType.MINUS: {
                return "MINUS";
            }
            case LexemeType.ASTERISK: {
                return "ASTERISK";
            }
            case LexemeType.DIVIDE: {
                return "DIVIDE";
            }
            case LexemeType.LEFTBRACKET: {
                return "LEFTBRACKET";
            }
            case LexemeType.RIGHTBRACKET: {
                return "RIGHTBRACKET";
            }
            case LexemeType.DOUBLE_LITERAL: {
                return "DOUBLE_LITERAL";
            }
            case LexemeType.STRING_LITERAL: {
                return "STRING_LITERAL";
            }
            case LexemeType.INTEGER_LITERAL: {
                return "INTEGER_LITERAL";
            }
            case LexemeType.IDENT: {
                return "IDENT";
            }
        }
        return "<UNKNOWN>(" + Integer.toString(id) + ")";
    }
}

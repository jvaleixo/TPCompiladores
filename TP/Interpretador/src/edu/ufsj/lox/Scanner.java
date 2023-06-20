package edu.ufsj.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.ufsj.lox.TokenType.*;

class Scanner{
    private static final Map<String, TokenType> keywords;
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private final String source;
    private final List<Token> tokens = new ArrayList<Token>();

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        while(!isAtEnd()){
          // inicio do proximo lexema
          start = current;
          scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private char advance(){
        current++;
        return source.charAt(current-1);
    }

    private void addToken(TokenType type){
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal){
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private void scanToken(){
        char c = advance();
        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '?': addToken(INTERROGATION); break;//AQUI
		        case ':': addToken(COLON); break;//AQUI
            case '!': addToken(match('=') ? BANG_EQUAL: BANG); break;
            case '=': addToken(match('=') ? EQUAL_EQUAL: EQUAL); break;
            case '<': addToken(match('=') ? LESS_EQUAL: LESS); break;
            case '>': addToken(match('=') ? GREATER_EQUAL: GREATER); break;
            case '/':
                // /* c */ *
                //  a|
                if(match('*')){
                    //comentarios de varias linhas
                    while(!isAtEnd()){
                        char a = advance();
                        if(a == '\n') line++;
                        if(a == '*' && peek() == '/'){
                            advance();
                            break;
                        }
                    };

                }else if(match('/')){
                    //comentarios de uma linha
                    while(peek() != '\n' && !isAtEnd()){
                        advance();
                    }
                }
                else addToken(SLASH);
                break;
            case '"': string(); break;
            case ' ':
            case '\t':
            case '\r':
                    break;
            case '\n': line++; break;
            default:
              if(isDigit(c)){
                  number();
            } else if (isAlpha(c)){
              identifier();
            } else { Lox.error(line, "Unexpected character.");
            }
             break;
        }
      }

      private void string(){
        while(peek() != '"' &&!isAtEnd()){
          if(peek() == '\n') line++;
          advance();
        }
        // abre o " sem o  respectivo  fecha "
        if(isAtEnd()){
          Lox.error(line, "Unterminated string");
          return;
        }
        // o fecha "
        advance();
        // remove os "s
        String value = source.substring(start + 1, current - 1);
        addToken(STRING,value);
      }

      private void number(){
        while(isDigit(peek())) advance();
        //procura a parte fracionaria
        if(peek() == '.' && isDigit(peekNext())){
          // consome o "."
          while(isDigit(peek())) advance();
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start,current)));
      }

      private boolean isDigit(char c){
        return (c >= '0' && c <= '9');
      }

      private boolean match(char expected){
          if(isAtEnd()) return false;
          if(source.charAt(current) != expected) return false;

          current++;
          return true;
      }

      private char peek(){
          if(isAtEnd()) return '\0';
          return source.charAt(current);
      }

      private char peekNext(){
        if(current + 1 >= source.length()) return '0';
        return source.charAt(current+1);
      }

      private boolean isAlpha(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_');
      }

      private boolean isAlphaNumberic(char c){
        return isAlpha(c) || isDigit(c);
      }

      private void identifier(){
        while(isAlphaNumberic(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if(type == null) type = IDENTIFIER;
        addToken(type);
      }

      static{
        keywords = new HashMap<String,TokenType>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);//AQUI
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
      }
}

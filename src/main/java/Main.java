import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import com.interpreter.utils.*;

public class Main {
  static List<Token> tokens = new java.util.ArrayList<>(List.of());
  private static final HashMap<Character, TokenType> lexGrammar = new HashMap<>();
  private static boolean hadRuntimeError = false;

  static {
    lexGrammar.put('{', TokenType.LEFT_BRACE);
    lexGrammar.put('}', TokenType.RIGHT_BRACE);
    lexGrammar.put('(', TokenType.LEFT_PAREN);
    lexGrammar.put(')', TokenType.RIGHT_PAREN);
    lexGrammar.put(',', TokenType.COMMA);
    lexGrammar.put('.', TokenType.DOT);
    lexGrammar.put('-', TokenType.MINUS);
    lexGrammar.put('+', TokenType.PLUS);
    lexGrammar.put('/', TokenType.SLASH);
    lexGrammar.put('*', TokenType.STAR);
    lexGrammar.put(';', TokenType.SEMICOLON);

    lexGrammar.put('!', TokenType.BANG);
    lexGrammar.put('=', TokenType.EQUAL);
    lexGrammar.put('>', TokenType.GREATER);
    lexGrammar.put('<', TokenType.LESS);

    lexGrammar.put('"', TokenType.STRING);
  }

  private static final HashMap<String, TokenType> keywords =  new HashMap<>();

  static {
    keywords.put("and",    TokenType.AND);
    keywords.put("class",  TokenType.CLASS);
    keywords.put("else",   TokenType.ELSE);
    keywords.put("false",  TokenType.FALSE);
    keywords.put("for",    TokenType.FOR);
    keywords.put("fun",    TokenType.FUN);
    keywords.put("if",     TokenType.IF);
    keywords.put("nil",    TokenType.NIL);
    keywords.put("or",     TokenType.OR);
    keywords.put("print",  TokenType.PRINT);
    keywords.put("return", TokenType.RETURN);
    keywords.put("super",  TokenType.SUPER);
    keywords.put("this",   TokenType.THIS);
    keywords.put("true",   TokenType.TRUE);
    keywords.put("var",    TokenType.VAR);
    keywords.put("while",  TokenType.WHILE);
  }

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
//    System.err.println("Logs from your program will appear here!");

    if (args.length < 2) {
      System.err.println("Usage: ./interpreter.sh tokenize <filename>");
      System.exit(1);
    }

    String command = args[0];
    String filename = args[1];

    if(command.equals("tokenize")) {
      tokenizeFile(filename, true);
    }
    else if(command.equals("parse")) {
      parseFile(filename, true);
    }
    else if(command.equals("evaluate")) {
      interpretFile(filename, true);
      if(hadRuntimeError) System.exit(70);
    }
    else if(command.equals("run")) {
      interpretFileNew(filename, true);
      if(hadRuntimeError) System.exit(70);
    }
    else{
      System.err.println("Unknown command: " + command);
      System.exit(1);
    }

  }

  private static List<Stmt> parseFileNew(String filename, boolean print) {

    boolean hadError = tokenizeFile(filename, false);
    // Stop if there was a syntax error.
    if (hadError) return null;
    ParserNew parser = new ParserNew(tokens);
//    Expr expression = parser.parse();

    List<Stmt> statements = parser.parse();
    if(statements.getFirst() == null) System.exit(65);



    if(print) {
//      System.out.println(new AstPrinter().print(statements));
    }

      return statements;
  }


  private static void interpretFileNew(String filename, boolean print) {

    List<Stmt> statements = parseFileNew(filename, false);

    InterpreterNew interpreter = new InterpreterNew();
    interpreter.interpret(statements, print);
  }

  private static Expr parseFile(String filename, boolean print) {

    boolean hadError = tokenizeFile(filename, false);
    Parser parser = new Parser(tokens);
    Expr expression = parser.parse();

    if(expression == null) System.exit(65);

    // Stop if there was a syntax error.
    if (hadError) return null;

    if(print) {
      System.out.println(new AstPrinter().print(expression));
    }

    return expression;
  }
  private static void interpretFile(String filename, boolean print) {

    Expr expression = parseFile(filename, false);

    Interpreter interpreter = new Interpreter();
    interpreter.interpret(expression, print);
  }

  private static boolean tokenizeFile(String filename, boolean print) {
    String fileContents = "";
    try {
      fileContents = Files.readString(Path.of(filename));
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      System.exit(1);
    }


    if (!fileContents.isEmpty()) {

      boolean syntaxError = false;
      String token;
      TokenType tokenType;
      Character curChar;

      int lineNo = 1;

      for(int curInd = 0; curInd<fileContents.length(); curInd++){
        curChar = fileContents.charAt(curInd);

        if(Character.isWhitespace(curChar) ||
                Character.isSpaceChar(curChar)){
          if(curChar.equals('\n')) lineNo++;
          continue;
        }

        token = String.valueOf(curChar);

        if(lexGrammar.containsKey(curChar))
        {

          tokenType = lexGrammar.get(curChar);

          if (fileContents.startsWith("//", curInd)){
            if(fileContents.indexOf("\n", curInd)!=-1){
              curInd = fileContents.indexOf("\n", curInd);
              lineNo+=1;
              continue;
            }
            break;
          }

          Token longerLex = checkLongerLex(fileContents, curInd, lineNo);

          if(longerLex!=null){
            tokens.add(longerLex);
            curInd++;
          }
          else if(curChar.equals('"')){

            int strLiteralEndInd = fileContents.indexOf('"', curInd+1);
            if(strLiteralEndInd == -1){
               System.err.println("[line "+lineNo+"] Error: Unterminated string.");
              syntaxError = true;
              break;
            }

            tokens.add(new Token(TokenType.STRING,
                    fileContents.substring(curInd, strLiteralEndInd+1),
                    fileContents.substring(curInd+1, strLiteralEndInd),
                    lineNo));
            curInd = strLiteralEndInd;

          }
          else{
            tokens.add(new Token(tokenType, token, null, lineNo));
          }
        }
        else if(Character.isDigit(curChar)){
          curInd = tokenizeDigits(curInd, curChar, fileContents, lineNo);
        }
        else if(Character.isAlphabetic(curChar) || curChar.equals('_')){
          curInd = tokenizeWords(curInd, curChar, fileContents, lineNo);
        }
        else{
           System.err.println("[line "+lineNo+"] Error: Unexpected character: "+token);
          syntaxError = true;
        }
      }

      // Print the current tokens
      for(Token curToken: tokens){
        if(print) System.out.println(curToken.toString());
      }

      tokens.add(new Token(TokenType.EOF, "", null, lineNo));
      if(print) System.out.println("EOF  null");
      if(syntaxError) System.exit(65);

      return syntaxError;
    } else {
      if(print) System.out.println("EOF  null"); // Placeholder, remove this line when implementing the scanner
      return true;
    }

  }

  private static int tokenizeWords(int curInd, Character curChar, String fileContents, int lineNo) {
    int startInd = curInd;
    while(Character.isAlphabetic(curChar) || curChar.equals('_') || Character.isDigit(curChar)){
      curInd++;
      if(curInd>=fileContents.length()) break;
      curChar = fileContents.charAt(curInd);
    }
    String variable = fileContents.substring(startInd, curInd);
    tokens.add(new Token(keywords.getOrDefault(variable, TokenType.IDENTIFIER),
            variable, null, lineNo));
    curInd--;
    return curInd;
  }

  private static int tokenizeDigits(int curInd, Character curChar, String fileContents, int lineNo) {
    int startInd = curInd;
    while(Character.isDigit(curChar) || curChar.equals('.')){
      curInd++;
      if(curInd>=fileContents.length()) break;
      curChar = fileContents.charAt(curInd);
    }

    String num = fileContents.substring(startInd, curInd);
    tokens.add(new Token(TokenType.NUMBER,num, Double.parseDouble(num), lineNo) );
    curInd--;
    return curInd;
  }

  private static Token checkLongerLex(String fileContents, int i, int lineNo) {

    if((i+1) < fileContents.length()){

      if(fileContents.startsWith("!=", i)){

        return new Token(TokenType.BANG_EQUAL, "!=", null, lineNo);

      } else if (fileContents.startsWith("<=", i)){

        return new Token(TokenType.LESS_EQUAL, "<=", null, lineNo);

      }
      else if (fileContents.startsWith(">=", i)){

        return new Token(TokenType.GREATER_EQUAL, ">=", null, lineNo);

      }
      else if (fileContents.startsWith("==", i)){

        return new Token(TokenType.EQUAL_EQUAL, "==", null, lineNo);

      }
    }
    return null;
  }

  static void runtimeError(RuntimeError error) {
    System.err.println(error.getMessage() +
            "\n[line " + error.token.line + "]");
    hadRuntimeError = true;
  }


}

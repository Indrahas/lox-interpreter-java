import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class Main {
  static HashMap<Character, TokenType> lexGrammar = new HashMap<>();

  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
//    System.err.println("Logs from your program will appear here!");

    if (args.length < 2) {
      System.err.println("Usage: ./your_program.sh tokenize <filename>");
      System.exit(1);
    }

    String command = args[0];
    String filename = args[1];

    if (!command.equals("tokenize")) {
      System.err.println("Unknown command: " + command);
      System.exit(1);
    }

    String fileContents = "";
    try {
      fileContents = Files.readString(Path.of(filename));
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      System.exit(1);
    }

    // Uncomment this block to pass the first stage

     if (!fileContents.isEmpty()) {
       List<Token> tokens = new java.util.ArrayList<>(List.of());
       grammarInit();

       boolean syntaxError = false;
       String token;
       TokenType tokenType;
       Character curChar;

       int lineNo = 1;

       for(int i = 0; i<fileContents.length(); i++){

         curChar = fileContents.charAt(i);

         if(Character.isWhitespace(curChar) ||
           Character.isSpaceChar(curChar)){
           if(curChar.equals('\n')) lineNo++;
           continue;
         }

         token = String.valueOf(curChar);

         if(lexGrammar.containsKey(curChar))
         {

           tokenType = lexGrammar.get(curChar);

           if (fileContents.startsWith("//", i)){
             if(fileContents.indexOf("\n", i)!=-1){
               i = fileContents.indexOf("\n", i);
               lineNo+=1;
               continue;
             }
             break;
           }

           Token longerLex = checkLongerLex(fileContents, i, lineNo);

           if(longerLex!=null){
             tokens.add(longerLex);
             i++;
           }
           else if(curChar.equals('"')){

             int strLiteralEndInd = fileContents.indexOf('"', i+1);
             if(strLiteralEndInd == -1){
               System.err.println("[line "+lineNo+"] Error: Unterminated string.");
               syntaxError = true;
               break;
             }
             String strLiteral = fileContents.substring(i, strLiteralEndInd+1);
             tokens.add(new Token(TokenType.STRING,
                                  fileContents.substring(i, strLiteralEndInd+1),
                                  fileContents.substring(i+1, strLiteralEndInd),
                                  lineNo));
             i = strLiteralEndInd;

           }
           else{
             tokens.add(new Token(tokenType, token, null, lineNo));
           }
         }
         else if(Character.isDigit(curChar)){
           int startInd = i;
           while(Character.isDigit(curChar) || curChar.equals('.')){
             i++;
             if(i>=fileContents.length()) break;
             curChar = fileContents.charAt(i);
           }
           String num = fileContents.substring(startInd, i);
           tokens.add(new Token(TokenType.NUMBER,num, Float.parseFloat(num), lineNo) );
            i--;
         }
         else{

           System.err.println("[line "+lineNo+"] Error: Unexpected character: "+token);
           syntaxError = true;
         }
       }

       for(Token curToken: tokens){
         System.out.println(curToken.toString());
       }

       System.out.println("EOF  null");
       if(syntaxError) System.exit(65);

     } else {
       System.out.println("EOF  null"); // Placeholder, remove this line when implementing the scanner
     }
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

  private static void grammarInit() {
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
}

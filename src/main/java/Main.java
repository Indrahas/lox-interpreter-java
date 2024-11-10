import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

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
       grammarInit();
       boolean syntaxError = false;
       String token;
       TokenType tokenType;
       for(int i = 0; i<fileContents.length(); i++){
         if(Character.isWhitespace(fileContents.charAt(i)) ||
           Character.isSpaceChar(fileContents.charAt(i))){
           continue;
         }
         token = String.valueOf(fileContents.charAt(i));
         if(lexGrammar.containsKey(fileContents.charAt(i)))
         {
           tokenType = lexGrammar.get(fileContents.charAt(i));
           if((i+1) < fileContents.length()){
             if(fileContents.startsWith("!=", i)){
               tokenType = TokenType.BANG_EQUAL;
               token = "!=";
               i++;
             } else if (fileContents.startsWith("<=", i)){
               tokenType = TokenType.LESS_EQUAL;
               token = "<=";
               i++;
             }
             else if (fileContents.startsWith(">=", i)){
               tokenType = TokenType.GREATER_EQUAL;
               token = ">=";
               i++;
             }
             else if (fileContents.startsWith("==", i)){
               tokenType = TokenType.EQUAL_EQUAL;
               token = "==";
               i++;
             }
             else if (fileContents.startsWith("//", i)){
               break;
             }
           }
           System.out.println(tokenType + " " + token + " null");
         }

         else{
           System.err.println("[line 1] Error: Unexpected character: "+token);
           syntaxError = true;

         }

       }
       System.out.println("EOF  null");
       if(syntaxError) System.exit(65);
     } else {

       System.out.println("EOF  null"); // Placeholder, remove this line when implementing the scanner
     }
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


  }
}

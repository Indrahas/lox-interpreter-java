import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Main {
  static HashMap<String, TokenType> lexGrammar = new HashMap<>();
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
       for(int i = 0; i<fileContents.length(); i++){
         System.out.println(lexGrammar.get(fileContents.substring(i,i+1)) + " " + fileContents.charAt(i) + " null");
       }
       System.out.println("EOF  null");
//       throw new RuntimeException("Scanner not implemented");
     } else {

       System.out.println("EOF  null"); // Placeholder, remove this line when implementing the scanner
     }
  }

  private static void grammarInit() {
    lexGrammar.put("{", TokenType.LEFT_BRACE);
    lexGrammar.put("}", TokenType.RIGHT_BRACE);
    lexGrammar.put("(", TokenType.LEFT_PAREN);
    lexGrammar.put(")", TokenType.RIGHT_PAREN);
    lexGrammar.put(",", TokenType.COMMA);
    lexGrammar.put(".", TokenType.DOT);
    lexGrammar.put("-", TokenType.MINUS);
    lexGrammar.put("+", TokenType.PLUS);
    lexGrammar.put("/", TokenType.SLASH);
    lexGrammar.put("*", TokenType.STAR);

    lexGrammar.put("!", TokenType.BANG);
    lexGrammar.put("!=", TokenType.BANG_EQUAL);
    lexGrammar.put("=", TokenType.EQUAL);
    lexGrammar.put("==", TokenType.EQUAL_EQUAL);
    lexGrammar.put(">", TokenType.GREATER);
    lexGrammar.put(">=", TokenType.GREATER_EQUAL);
    lexGrammar.put("<", TokenType.LESS);
    lexGrammar.put("<=", TokenType.LESS_EQUAL);

  }
}

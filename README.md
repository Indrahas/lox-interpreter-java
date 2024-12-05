# Java-Based Lox Interpreter  

[![Java](https://img.shields.io/badge/Language-Java-007396?logo=java&logoColor=white)](https://www.java.com/)  
[![Lox Language](https://img.shields.io/badge/Lox-Language-Informational)](https://www.craftinginterpreters.com/the-lox-language.html)  

## Project Overview  

This project is a **Java-based interpreter** for the [Lox programming language](https://www.craftinginterpreters.com/the-lox-language.html), implemented as described in the book *Crafting Interpreters* by Robert Nystrom. The interpreter provides functionality for tokenizing, parsing, and evaluating Lox code, along with the ability to run complete scripts.  

---

## Features  
- **Tokenization**: Breaks the source code into lexical tokens.  
- **Parsing**: Generates an abstract syntax tree (AST) from the tokens.  
- **Evaluation**: Executes the AST to produce results.  
- **Script Execution**: Runs full Lox programs written in `.lox` files.  

---

## How to Run  

### Prerequisites  
- Java Development Kit (JDK) 11 or higher installed.  
- Unix-based environment (Linux/Mac) or WSL for running the `.sh` scripts.  

### Steps  

1. **Clone the Repository**  
   ```bash  
   git clone https://github.com/Indrahas/lox-interpreter-java.git  
   cd lox-interpreter-java  
  
2. **Write Your Lox Code**
   Create or modify the test.lox file in the root directory to include the Lox code you wish to execute.

3. **Run the Interpreter**
   Use the interpreter.sh script to execute the Lox code. For example:
   ```bash  
   ./interpreter.sh run test.lox  
  

## Example  

### Input (from `test.lox`)  
```lox
// A simple Lox program  
var age = 67;

var isAdult = age >= 18;
if (isAdult) { print "eligible for voting: true"; }
else { print "eligible for voting: false"; }

if (age < 16) { print "eligible for driving: false"; }
else if (age < 18) { print "eligible for driving: learner's permit"; }
else { print "eligible for driving: full license"; }

if (age < 21) { print "eligible for drinking (US): false"; }
else { print "eligible for drinking (US): true"; }


var quz = "after";
{
  var quz = "before";

  for (var quz = 0; quz < 1; quz = quz + 1) {
    print quz;
    var quz = -1;
    print quz;
  }
}

{
  for (var quz = 0; quz > 0; quz = quz + 1) {}

  var quz = "after";
  print quz;

  for (quz = 0; quz < 1; quz = quz + 1) {
    print quz;
  }
}
```

### Input (from `test.lox`)  
```plaintext
eligible for voting: true
eligible for driving: full license
eligible for drinking (US): true
0
-1
after
0

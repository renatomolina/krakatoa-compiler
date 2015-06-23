package AST;

public class Letter {
    char letter;

    Letter(char l){
        this.letter = l;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.print(letter + "");
    }
}

package AST;

public class Digit {
    int digit;

    Digit(int digit){
        this.digit = digit;
    }

    public void genK(PW pw, boolean putParenthesis){
        pw.print(String.valueOf(this.digit));
    }

}

package AST;

public class SignalFactor {
    private int s;
    private Factor f;
public SignalFactor(int s, Factor f){
    this.s = s;
    this.f = f;

}
    void genK(PW pw, boolean putParenthesis) {
        if(s == 4)
            pw.print("-");
        else if(s == 3)
            pw.print("+");
        f.genK(pw, putParenthesis);
    }

}

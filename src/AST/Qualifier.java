package AST;

public class Qualifier {
    private boolean Static;
    private int protection;

    public Qualifier(boolean Static, int protection){
        this.Static = Static;
        this.protection = protection;
    }
    public int getProtection(){
        return protection;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(Static)
            pw.printIdent("static ");
        if(protection==38)
            pw.printIdent("public ");
        else
            pw.printIdent("private ");
    }
}

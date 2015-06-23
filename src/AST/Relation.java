package AST;

public class Relation {
    private int relation;
    public Relation(int relation){
        this.relation = relation;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(relation == 17)
            pw.print("==");
        else if(relation == 16)
            pw.print("!=");
        else if(relation == 15)
            pw.print(">=");
        else if(relation == 14)
            pw.print(">");
        else if(relation == 13)
            pw.print("<=");
        else
            pw.print("<");
    }

}

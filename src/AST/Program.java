package AST;

import java.util.ArrayList;

public class Program {
    ClassDec cDec;
    ArrayList<ClassDec> cDecList;

    public Program(ClassDec cDec, ArrayList<ClassDec> cDecList){
        this.cDec = cDec;
        this.cDecList = cDecList;
    }

    public void genK(PW pw, boolean putParenthesis){
        cDec.genK(pw, putParenthesis);
        for(ClassDec cd : cDecList){
            cd.genK(pw, putParenthesis);
        }
    }

}

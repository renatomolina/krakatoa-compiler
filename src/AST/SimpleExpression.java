package AST;

import java.util.ArrayList;

public class SimpleExpression {
    private Term term;
    private ArrayList<LowOperator> loList;
    private ArrayList<Term> tList;

    public SimpleExpression(Term term,ArrayList<LowOperator> loList,ArrayList<Term> tList ){
        this.term = term;
        this.loList = loList;
        this.tList = tList;
    }

    public void genK(PW pw, boolean putParenthesis){
        term.genK(pw, putParenthesis);
        int s = loList.size();
        for(int i = 0; i <s ; i++){
            loList.get(i).genK(pw, putParenthesis);
            tList.get(i).genK(pw, putParenthesis);
        }
    }

    }

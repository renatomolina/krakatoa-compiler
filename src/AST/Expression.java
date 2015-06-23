package AST;

public class Expression {

    SimpleExpression sExp;
    Relation relation;
    SimpleExpression sExp2;

   public Expression(SimpleExpression sExp, Relation relation, SimpleExpression sExp2){
        this.sExp = sExp;
        this.sExp2 = sExp2;
        this.relation = relation;
    }

    public void genK(PW pw, boolean putParenthesis){        
        sExp.genK(pw, putParenthesis);
        if(this.relation != null){
            relation.genK(pw, putParenthesis);
            sExp2.genK(pw, putParenthesis);
        }
    }
}

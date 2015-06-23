package AST;

abstract public class Member {
    abstract public void genK(PW pw, boolean putParenthesis);
    abstract public StatementList getList();
}

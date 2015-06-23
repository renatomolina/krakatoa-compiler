package AST;

abstract public class Type {
    private String name;
abstract public void genK(PW pw, boolean putParenthesis);
abstract public Type getType();
public Type(String name){
    this.name = name;
}

}

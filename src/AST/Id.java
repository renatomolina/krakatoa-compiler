package AST;

public class Id extends Type{
    private Letter letter;
    private String name;
    
   public Id(String name) {
     super(name);
    this.name = name;
   }
   public String getName(){
    return name;
   }
    public void genK(PW pw, boolean putParenthesis){
        pw.add();
        if(putParenthesis)
            pw.print(name);
        else
            pw.printIdent(name);
        pw.sub();
    }

    public Type getType() {
        return this;
    }
}

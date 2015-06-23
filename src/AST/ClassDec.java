package AST;

public class ClassDec {


    Id id;
    Id id2;
    MemberList memberList;

    public ClassDec(Id id, Id id2, MemberList mbList){
        this.id = id;
        this.id2 = id2;
        this.memberList = mbList;
    }

     public ClassDec(Id id, Id id2){
        this.id = id;
        this.id2 = id2;
    }

    public void setMemberList(MemberList memberList){
        this.memberList = memberList;
    }


    public String getName(){
        return id.getName();
    }
    public void genK(PW pw , boolean putParenthesis){        
        pw.print("class ");
        id.genK(pw, putParenthesis);        
        if(id2 != null){
            pw.print("extends");
            id.genK(pw, putParenthesis);
        }
        pw.printlnIdent("{");
        memberList.genK(pw, putParenthesis);
        pw.printlnIdent("}");
        
    }
}

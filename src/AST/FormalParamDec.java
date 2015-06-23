package AST;

import java.util.ArrayList;

public class FormalParamDec {
    ParamDec pDec;
    ArrayList<ParamDec> listPDec;

    public FormalParamDec(ParamDec param, ArrayList<ParamDec> listPDec){
        this.pDec = param;
        this.listPDec = listPDec;
    }

    public void genK(PW pw, boolean putParenthesis){
        if(pDec != null){
        pDec.genK(pw, putParenthesis);
        pw.print(",");
        for(ParamDec pd : listPDec){
            pd.genK(pw, putParenthesis);
        }
    }
    }
}

package Comp;

import java.util.*;
import AST.*;

public class SymbolTable {

    public SymbolTable() {
        globalTable = new Hashtable();
        localTable  = new Hashtable();
    }

    //# corrija: value deve ter o tipo usado para representar classes
    // o mesmo se aplica a getInGlobal
    public Object putInGlobal( String key, ClassDec value ) {
       return globalTable.put(key, value);
    }

    public ClassDec getInGlobal( String key ) {
       return (ClassDec ) globalTable.get(key);
    }

    public Id putInLocal( String key, Id value ) {
       return (Id ) localTable.put(key, value);
    }

    public Id getInLocal( String key ) {
       return (Id ) localTable.get(key);
    }

    public Object get( String key ) {
        // returns the object corresponding to the key.
        Object result;
        if ( (result = localTable.get(key)) != null ) {
              // found local identifier
            return result;
        }
        else {
              // global identifier, if it is in globalTable
            return globalTable.get(key);
        }
    }

    public void removeLocalIdent() {
           // remove all local identifiers from the table
         localTable.clear();
    }


    private Hashtable globalTable, localTable;
}
            
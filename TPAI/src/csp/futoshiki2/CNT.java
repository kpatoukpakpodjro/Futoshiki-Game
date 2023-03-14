/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csp.futoshiki2;

/**
 *
 * @author MAK
 */
class CNT implements Comparable<CNT>{
    private String var, C;

    public CNT( String var, String C) {
        this.var = var;
        this.C = C;      
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public String getCh() {
        return C;
    }

  
    public void setCh(String C) {
        this.C = C;
    }

    @Override
    public int compareTo(CNT o) {
     if (this.var.equals(o.var) && this.C.equals(o.C)) 
            return 0;
     else if (this.var.equals(o.var))
            return  this.C.compareTo(o.C);
     
     return this.var.compareTo(o.var);
    }

}


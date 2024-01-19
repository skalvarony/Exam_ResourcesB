package com.example.demo;

import es.ufv.dis.mock.AFN.Observation2;

public class Observation2Mock extends Observation2 {
    
    public Observation2Mock(String mscode, String year, String estCode, Float estimate, Float se, Float lowerCIB, Float upperCIB, String flag) {
        super();
        setMscode(mscode);
        setYear(year);
        setEstCode(estCode);
        setEstimate(estimate);
        setSe(se);
        setLowerCIB(lowerCIB);
        setUpperCIB(upperCIB);
        setFlag(flag);
      }
    
      public String get_id(){
        System.out.println("Does not call API");
        return "idprueba";
      }
}

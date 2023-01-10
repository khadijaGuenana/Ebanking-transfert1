package org.sid.transferservice.utils;

public class Generator {
    public static String getPin(){
        String pin = "";
        for(int i=0;i<8;i++) pin+=(int)Math.floor(Math.random()*10);
        return pin;
    }
    public static String getRef(){
        String ref="EDF";
        for(int i=0;i<13;i++) ref+=(int)Math.floor(Math.random()*10);
        return ref;
    }
    public static void main(String[] args){
        System.out.println("Pin : "+Generator.getPin());
        System.out.println("Ref: "+Generator.getRef());
    }
}

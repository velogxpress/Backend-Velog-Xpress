package com.velogexpress.tools;

public class

UserID {
    public String SERIAL(String abreger,String clientname){
        String serie="";
        String leftcode="";
        String rightcode="";
        String start="";

        try {
            for(int i=0;i<=2;i++){
                leftcode=leftcode+(int)(Math.random()*9 + 1)+"";
                rightcode=rightcode+(int)(Math.random()*9 + 1)+"";
            }
            start=SPLITNAME(clientname);
            serie=leftcode+start+rightcode+"-"+abreger;
        } catch (Exception e) {
        }
        return serie;
    }

    public String SPLITNAME(String prix){
        String price="";
        String price2="";
        String start="";
        String end="";
        String result=null;

        String strArray[] = null;
        strArray = prix.split(" ");
        if(strArray.length>1){
            price2=strArray[1];
            price=strArray[0];
            start=String.valueOf(price.charAt(0));
            end=String.valueOf(price2.charAt(0));
        }else{
            price=strArray[0];
            start= price.charAt(0) +String.valueOf(price.charAt(1));
            end="";
        }
        result=start+end;
        return result.toUpperCase();
    }



}

package com.bocchi;

public class App {
    public static void main(String[] args) {
        System.out.println(fun());System.out.println("ddd");
    }

    public static String fun(){
        try {
        System.out.println(1/0);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "zhengc";
    }
}

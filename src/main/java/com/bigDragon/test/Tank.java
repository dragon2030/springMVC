package com.bigDragon.test;


public class Tank {
    public String state = "empty";
    
    public Tank(String state){
        this.state = state;
    }
    
    void clear(){
        this.state = "empty";
    }
    
    @Override
    protected void finalize() throws Throwable {
        System.out.println("------finalize---------");
        if(!state.equals("empty")){
            System.out.println("Error!!Tank未被清空");
        }
        super.finalize();
    }
    
    public static void main(String[] args) {
        Tank tank1 = new Tank("full");
        tank1.clear();
        System.gc();
        System.runFinalization();
        
        Tank tank2 = new Tank("full");
        tank1 = null;
        tank2 = null;
        System.gc();
        System.runFinalization();
    }

}

package com.bigDragon.javase.ioStream.caseRecord;

public class V2rayPortInstructGenerate {
    public static void main(String[] args) {
        V2rayPortInstructGenerate main = new V2rayPortInstructGenerate();
//        main.removePortInstructGenerate(60008,60098);
        main.createPortInstructGenerate(50003,50099);
    }

    private void removePortInstructGenerate(Integer startPort,Integer endPort){
        StringBuffer stringBuffer = new StringBuffer("firewall-cmd --zone=public");
        for(int i=startPort;i<=endPort;i++){
            stringBuffer.append(" --remove-port=").append(i).append("/tcp");
        }
        stringBuffer.append(" --permanent");
        System.out.println(stringBuffer);
    }

    private void createPortInstructGenerate(Integer startPort,Integer endPort){
        StringBuffer stringBuffer = new StringBuffer("firewall-cmd --zone=public");
        for(int i=startPort;i<=endPort;i++){
            stringBuffer.append(" --add-port=").append(i).append("/tcp");
        }
        stringBuffer.append(" --permanent");
        System.out.println(stringBuffer);
    }
}

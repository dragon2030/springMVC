package com.bigDragon.lombok.printCase;

import lombok.Data;

@Data
public class Father2 {
    private String fName;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
}

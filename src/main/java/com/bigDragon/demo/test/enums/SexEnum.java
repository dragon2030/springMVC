package com.bigDragon.demo.test.enums;

/**
 * 性别
 * @author 3759
 *
 */
public enum SexEnum {
    Man("1","男"),
    Woman("2","女");
	
    private final String value;
    private final String message;
    
    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
    
    private SexEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }
}

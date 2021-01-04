package com.bigDragon.javase.java8;

import java.util.Objects;

/**
 * @author bigDragon
 * @create 2020-12-21 19:45
 */
public class Boy {
    private Girl girl;

    @Override
    public String toString() {
        return "Boy{" +
                "girl=" + girl +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boy boy = (Boy) o;
        return Objects.equals(girl, boy.girl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(girl);
    }

    public Girl getGirl() {
        return girl;
    }

    public void setGirl(Girl girl) {
        this.girl = girl;
    }

    public Boy() {
    }

    public Boy(Girl girl) {
        this.girl = girl;
    }
}

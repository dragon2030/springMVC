package com.bigDragon.testMain;

/**
 * @author: bigDragon
 * @create: 2022/4/8
 * @Description:
 */
public class InfoDto {
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "InfoDto{" +
                "student=" + student +
                '}';
    }
}

package com.bigDragon.testMain;

/**
 * @author bigDragon
 * @create 2020-12-09 20:31
 */
public class Main {

    public static void main(String[] args) {
        InfoDto infoDto = new InfoDto();
        Student student = infoDto.getStudent();
        System.out.println(student);

        Student student2 = new Student();
        student2.setName("Mike");
        infoDto.setStudent(student2);

        System.out.println(student2);
        System.out.println(infoDto);
        System.out.println(student);
        System.out.println(infoDto.getStudent());




        System.out.println(infoDto.getStudent()==infoDto.getStudent());
        Student student3 = infoDto.getStudent();
        Student student4 = infoDto.getStudent();
        System.out.println(student3==student4);
        System.out.println(student==student4);
        System.out.println(student==infoDto.getStudent());

/*        Student student3 = new Student();
        student3.setName("Bob");
        infoDto.setStudent(student3);

        System.out.println(student2);
        System.out.println(infoDto);
        System.out.println(student3);*/
    }

}

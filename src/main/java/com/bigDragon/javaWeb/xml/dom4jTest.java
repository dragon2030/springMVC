package com.bigDragon.javaWeb.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author bigDragon
 * @create 2021-05-28 13:40
 */
public class dom4jTest {
    //dom4j 解析xml
    @Test
    public void test1() throws Exception {
        // 创建一个SaxReader输入流，去读取 xml配置文件，生成Document对象
        //1.读取book.xml文件
        SAXReader saxReader = new SAXReader();
        //在Junit测试只不过，相对路径是从模块名开始算
        Document document = saxReader.read("src/main/webapp/static/books.xml");
        //2.根据Document对象获取根元素
        Element rootElement = document.getRootElement();
        //System.out.println(rootElement);
        //3.通过根元素获取book标签对象
        //element()和elements()都是通过标签名查找子元素
        List<Element> books = rootElement.elements("book");
        //4.遍历，处理每个book标签转换为Book类
        for (Element book:books){
            //asXML():把标签对象，转换为标签字符串
            //System.out.println(element.asXML());
            Element nameElement = book.element("name");
            //System.out.println(nameElement.asXML());
            //getText():可以获取标签中的文本内容
            String nameText = nameElement.getText();
            //System.out.println(nameText);

            //elementText()：直接获取指定标签名的文本内容  以上两步合一
            String priceText = book.elementText("price");
            //System.out.println(priceText);
            String authorText = book.elementText("author");

            //attributeValue() 获取属性值
            String idValue = book.attributeValue("id");

            Book book1 = new Book(idValue, nameText, new BigDecimal(priceText), authorText);
            System.out.println(book1.toString());
        }

    }
}

/**
 * 
 */
package com.bigDragon.javase.ioStream.BIO;

/**
 * 编码与解码测试类
 * 
 * @author: bigDragon
 * @date: 2020年8月28日
 * 
 */
public class EncoderDecode {

/**
 * 一、对java中char的取值范围思考
 * https://blog.csdn.net/qq_41063182/article/details/81034139
 * 
 * java中的编码采用的就是unicode编码，源文件使用的是utf-8编码，jvm虚拟机找的.class编译文件使用的是utf-16编码格式。
 * 
 * Unicode：采用的是十六进制形式，输入的时候采用 \u0000形式，除了前边的0-255，还在剩余的空间中加入了其余的符号，比如欧洲的一些语言，中国的汉语，而我们的汉字
 * 编码是从\u4e00开始，一直到\u8c9f结束,这也将剩余的空间占用了，也就有了取值范围是\u0000 - \uffff的说法，不过可惜汉字很多，unicode编码只收录了一部分常用的汉字。
 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EncoderDecode encoderDecode = new EncoderDecode();
		//测试java中int、char、byte转换问题
		//encoderDecode.compareIntCharByte();
		//打印所有Unicode收录中午字符
		//encoderDecode.printUnicode();
		//对字符串进行编码转换
		String importStr = "2020-08-20 23:57:23,534 [http-nio-8084-exec-12] WARN  [com.goldcard.goldcardiot.modules.sys.web.MessageProcessController] - 姊︾綉骞冲彴鎻愪氦鐘舵€侊紙鎺ユ敹鍥炴墽鏂瑰紡锛夎繑鍥炴暟鎹細command=RT_RESPONSE&spid=JS2289&mtmsgid=-8480786345923443457&rtstat=ACCEPTD&rterrcode=000";
		String outputStr = encoderDecode.chargeCharset(importStr, "GBK", "UTF-8");
		System.out.println(outputStr);
	}
	/**
	 * 测试java中int、char、byte转换问题
	 */
	public void compareIntCharByte(){
		int i = 20013;
		System.out.println("当前值："+intToBinaryString(i));
		char c = (char) i;
		System.out.println("当前值："+intToBinaryString(c));
		byte b = (byte) c;
		System.out.println("当前值："+intToBinaryString(b));
		char c2 = (char) b;
		System.out.println("c: "+c);
		System.out.println(b);
		System.out.println(c2);
	}
	
	/**
	 * 十进制转二进制
	 * @param i
	 * @return
	 */
	public String intToBinaryString(int i){
		String binaryString=Integer.toBinaryString(i);
		return binaryString;
	}

	/**
	 * 二进制转十进制
	 * @param binaryString
	 * @return
	 */
	public int binaryStringToInt(String binaryString){
		//方式一
		int a = Integer.valueOf(binaryString);
		//方式二
		//int a = Integer.parseInt(binaryString);
		return a;
	}
	
	/**
	 * 十进制转十六进制
	 * @param decimalism
	 * @return
	 */
	public String decimalismToHexadecimal(int decimalism){
		String hexadecimalStr = Integer.toHexString(decimalism);
		return hexadecimalStr;
	}

	/**
	 * 打印所有Unicode收录中午字符
	 */
	public void printUnicode(){
		 int count = 1; //记录汉字的个数
	        for(char c = '\u4e00'; c <= '\u8c9f'; c++ ) {
	            System.out.print(c + "\t");
	            count++;
	            if (count % 10 == 0) {
	                System.out.println("");
	            }
	        }
	        System.out.println(count);
	}
	
	/**
	 * 对字符串进行编码转换
	 * @param importStr	输入字符串
	 * @param originalCharset 原编码方式
	 * @param convertCharset 需要转换的编码方式
	 * @return
	 */
	public String chargeCharset(String importStr,String originalCharset,String convertCharset) {
		//将二进制数组编码为字符串
		String exportStr = null;
		try {
			//将字符串解码为二进制byte数组
			byte[] byteArray = importStr.getBytes(originalCharset);
			exportStr = new String(byteArray, convertCharset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exportStr;
	}
}

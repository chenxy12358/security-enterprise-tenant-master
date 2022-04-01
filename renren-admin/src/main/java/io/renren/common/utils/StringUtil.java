package io.renren.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * 字符串工具类
 * 
 */
public class StringUtil {

	private final static Logger log = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * 
	 * 判断字符是否为空.
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isEmpty(String source) {
		return (source == null || ("".equals(source)));
	}

	/**
	 * 
	 * 判断字符是否为空.
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isNotEmpty(String source) {
		return !isEmpty(source);
	}

	/**
	 * 
	 * 将字符串转换成utf-8类型
	 * 
	 * @param str
	 * @return
	 */
	public static String transformUtf_8(String str) {
		if (StringUtil.isEmpty(str)) {
			log.warn("需要转换的数据类型为空");
			return "";
		}
		String restr = str;
		try {
			restr = new String(str.getBytes("ISO8859-1"), "utf-8");
			return restr;
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return restr;
		}
	}

	/**
	 * 
	 * 随机产生一个UUID
	 * 
	 * @return 返回字符串类型的UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 
	 * 转换NULL为"";即：空值转换; 如str不为null时原值返回
	 * 
	 * @param str
	 * @return
	 */
	public static String transformNull(String str) {
		return str == null ? "" : str;

	}

	/**
	 * 将汉字转换为全拼
	 * 
	 * @param src
	 * @return
	 *//*
	public static String getPingYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
		t3.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断是否为汉字字符
				if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4 += t2[0];
				} else {
					t4 += java.lang.Character.toString(t1[i]);
				}
			}
			// System.out.println(t4);
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}*/
}


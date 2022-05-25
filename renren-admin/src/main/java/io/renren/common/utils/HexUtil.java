package io.renren.common.utils;

import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HexUtil {


    /**
     * 通用的下发命令方法
     *
     * @param deviceCode 设备号
     * @param destInfo   Json格式的执行目标信息  必须有值
     * @param param      Json格式参数
     *                   如果服务器要设备回传会话，
     *                   须在本json中加入一个key为“_session”的对象，
     *                   设备在回传时将本对象放入专门的_session部分返回
     * @param data       附加数据
     * @return
     */
    public static byte[] sendCmmd(String deviceCode, String destInfo, String param, String data, int count) {
        int paramLength = 0;
        if (param.length() > 0) {
            String param_16 = stringToHexString(param);
            paramLength = param_16.length()/2;
        }

        byte[] bytes = new byte[39];
        //协议flag
        bytes[0] = (byte) 0x48;
        bytes[1] = (byte) 0x59;
        bytes[2] = (byte) 0x56;
        bytes[3] = (byte) 0x52;
        // ver
        bytes[4] = (byte) 0x01;
        //类型  5
        bytes[5] = (byte) 0x05;
        //设备编号
        byte[] b = toBytes(stringToHexString(deviceCode));
        bytes[6] = b[0];
        bytes[7] = b[1];
        bytes[8] = b[2];
        bytes[9] = b[3];
        bytes[10] = b[4];
        bytes[11] = b[5];
        bytes[12] = b[6];
        bytes[13] = b[7];
        bytes[14] = b[8];
        bytes[15] = b[9];
        bytes[16] = b[10];
        bytes[17] = b[11];
        bytes[18] = b[12];
        bytes[19] = b[13];
        bytes[20] = b[14];
        bytes[21] = b[15];
        bytes[22] = b[16];
        // 荷载长度
        byte[] c = toBytes(set2Len(Integer.toString(destInfo.length() + paramLength + data.length() + count * 4, 16), 8, "prev"));
        bytes[23] = c[0];
        bytes[24] = c[1];
        bytes[25] = c[2];
        bytes[26] = c[3];

        // DestInfoLen
        byte[] d = toBytes(set2Len(Integer.toString(destInfo.length(), 16), 8, "prev"));
        bytes[27] = d[0];
        bytes[28] = d[1];
        bytes[29] = d[2];
        bytes[30] = d[3];

        // ParamLen
        byte[] e = toBytes(set2Len(Integer.toString(paramLength, 16), 8, "prev"));
        bytes[31] = e[0];
        bytes[32] = e[1];
        bytes[33] = e[2];
        bytes[34] = e[3];

        // DataLen
        byte[] f = toBytes(set2Len(Integer.toString(data.length(), 16), 8, "prev"));
        bytes[35] = f[0];
        bytes[36] = f[1];
        bytes[37] = f[2];
        bytes[38] = f[3];

        byte[] destInfos = toBytes(stringToHexString(destInfo));
        byte[] reBytes1 = append(bytes, destInfos);
        byte[] params = toBytes(stringToHexString(param));
        byte[] reBytes2 = append(reBytes1, params);
        byte[] datas = toBytes(stringToHexString(data));
        byte[] reBytes3 = append(reBytes2, datas);

        return reBytes3;
    }


    /**
     * 发送初始化消息
     *
     * @param deviceCode
     * @return
     */
    public static byte[] sendInitInfo(String deviceCode, String data) {
        byte[] bytes = new byte[27];
        //协议flag
        bytes[0] = (byte) 0x48;
        bytes[1] = (byte) 0x59;
        bytes[2] = (byte) 0x56;
        bytes[3] = (byte) 0x52;
        // ver
        bytes[4] = (byte) 0x01;
        //类型  3 - 初始化
        bytes[5] = (byte) 0x03;
        //设备编号
        byte[] b = toBytes(stringToHexString(deviceCode));
        bytes[6] = b[0];
        bytes[7] = b[1];
        bytes[8] = b[2];
        bytes[9] = b[3];
        bytes[10] = b[4];
        bytes[11] = b[5];
        bytes[12] = b[6];
        bytes[13] = b[7];
        bytes[14] = b[8];
        bytes[15] = b[9];
        bytes[16] = b[10];
        bytes[17] = b[11];
        bytes[18] = b[12];
        bytes[19] = b[13];
        bytes[20] = b[14];
        bytes[21] = b[15];
        bytes[22] = b[16];
        byte[] c = toBytes(set2Len(Integer.toString(data.length(), 16), 8, "prev"));
        bytes[23] = c[0];
        bytes[24] = c[1];
        bytes[25] = c[2];
        bytes[26] = (byte) 0x22;
        byte[] datas = toBytes(stringToHexString(data));
        byte[] reBytes = append(bytes, datas);
        return reBytes;
    }


    /**
     * 上传数据确认回复
     *
     * @param deviceCode
     * @return
     */
    public static byte[] sendUploadDataRep(String deviceCode) {
        byte[] bytes = new byte[30];
        //协议flag
        bytes[0] = (byte) 0x48;
        bytes[1] = (byte) 0x59;
        bytes[2] = (byte) 0x56;
        bytes[3] = (byte) 0x52;
        // ver
        bytes[4] = (byte) 0x01;
        //类型  3 - 初始化
        bytes[5] = (byte) 0x03;
        //设备编号
        byte[] b = toBytes(stringToHexString(deviceCode));
        bytes[6] = b[0];
        bytes[7] = b[1];
        bytes[8] = b[2];
        bytes[9] = b[3];
        bytes[10] = b[4];
        bytes[11] = b[5];
        bytes[12] = b[6];
        bytes[13] = b[7];
        bytes[14] = b[8];
        bytes[15] = b[9];
        bytes[16] = b[10];
        bytes[17] = b[11];
        bytes[18] = b[12];
        bytes[19] = b[13];
        bytes[20] = b[14];
        bytes[21] = b[15];
        bytes[22] = b[16];
        byte[] c = toBytes(set2Len("3", 8, "prev"));
        bytes[23] = c[0];
        bytes[24] = c[1];
        bytes[25] = c[2];
        bytes[26] = c[3];

        bytes[27] = (byte) 0x00;
        bytes[28] = (byte) 0x00;
        bytes[29] = (byte) 0x00;

        return bytes;
    }


    /**
     * 心跳回复
     *
     * @param deviceCode
     * @param data
     * @return
     */
    public static byte[] makeHeartReply(String deviceCode, String data) {

        byte[] bytes = new byte[31];
        //协议flag
        bytes[0] = (byte) 0x48;
        bytes[1] = (byte) 0x59;
        bytes[2] = (byte) 0x56;
        bytes[3] = (byte) 0x52;
        // ver
        bytes[4] = (byte) 0x01;
        //类型
        bytes[5] = (byte) 0x02;
        //设备编号
        byte[] b = toBytes(stringToHexString(deviceCode));
        bytes[6] = b[0];
        bytes[7] = b[1];
        bytes[8] = b[2];
        bytes[9] = b[3];
        bytes[10] = b[4];
        bytes[11] = b[5];
        bytes[12] = b[6];
        bytes[13] = b[7];
        bytes[14] = b[8];
        bytes[15] = b[9];
        bytes[16] = b[10];
        bytes[17] = b[11];
        bytes[18] = b[12];
        bytes[19] = b[13];
        bytes[20] = b[14];
        bytes[21] = b[15];
        bytes[22] = b[16];
        //载荷长度
        byte[] c = toBytes(set2Len("4", 8, "prev"));
        bytes[23] = c[0];
        bytes[24] = c[1];
        bytes[25] = c[2];
        bytes[26] = c[3];
        //data
        byte[] d = toBytes(set2Len(data, 8, "prev"));
        bytes[27] = d[0];
        bytes[28] = d[1];
        bytes[29] = d[2];
        bytes[30] = d[3];
     /*   int crcint=CRC16.alex_crc16(d, d.length);
        byte[] e=HexUtil.getBytes(crcint);*/
        return bytes;
    }

    public static byte[] makeReturn(String sourceID, String seqNo) {
        byte[] d = new byte[19];
        d[0] = (byte) 0x55;
        d[1] = (byte) 0xAA;
        d[2] = (byte) 0x01;
        d[3] = (byte) 0x83;
        byte[] a = toBytes(seqNo);
        d[4] = a[0];//随机序号
        d[5] = (byte) 0xAA;
        d[6] = (byte) 0xAA;
        d[7] = (byte) 0xAA;
        d[8] = (byte) 0xAA;
        sourceID = decodeHexString(sourceID);
        byte[] s = toBytes(sourceID);
        d[9] = s[0];
        d[10] = s[1];
        d[11] = s[2];
        d[12] = s[3];
        //长度
        d[13] = (byte) 0x12;
        d[14] = (byte) 0x00;
        //命令
        d[15] = (byte) 0x03;
        d[16] = (byte) 0x01;
        //数据
        d[17] = (byte) 0x04;
        d[18] = (byte) 0x00;
        byte[] date = date2HexStr3(DateUtil2.getCurrentDateTime());
        byte[] c = append(d, date);
        byte[] f = append(c, date);
        //crc
      /*  int crcint=CRC16.alex_crc16(f, f.length);
        byte[] bytes=HexUtil.getBytes(crcint);
		return append(f, bytes);*/
        return null;
    }

    /**
     * 时间处理为6为字节
     *
     * @param date
     * @return
     */
    public static byte[] date2HexStr3(String date) {
        date = date.replaceAll("-", "");
        date = date.replaceAll(":", "");
        date = date.replaceAll(" ", "");
        byte[] b_date = new byte[6];
        b_date[0] = (byte) Integer.parseInt(date.substring(2, 4));
        b_date[1] = (byte) Integer.parseInt(date.substring(4, 6));
        b_date[2] = (byte) Integer.parseInt(date.substring(6, 8));
        b_date[3] = (byte) Integer.parseInt(date.substring(8, 10));
        b_date[4] = (byte) Integer.parseInt(date.substring(10, 12));
        b_date[5] = (byte) Integer.parseInt(date.substring(12, 14));
        return b_date;
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }


    /**
     * @param org
     * @param to
     * @return
     * @Description: TODO<p>数组合并</p>
     * @Title: append
     * @author bait
     * @date 2016年10月11日 上午11:24:59
     */
    public static byte[] append(byte[] org, byte[] to) {

        byte[] newByte = new byte[org.length + to.length];

        System.arraycopy(org, 0, newByte, 0, org.length);

        System.arraycopy(to, 0, newByte, org.length, to.length);

        return newByte;

    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组
     *
     * @param n short
     * @return byte[]
     */
    public static byte[] toLH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }

    private short id = 1234;

    public static byte[] join(byte[] a1, byte[] a2) {
        byte[] result = new byte[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    // 带符号位的16进度转10进度
    public static int get16to10(String hexStr) {
        if (hexStr.length() != 4) {
            return 0;
        } else {
            return (short) (Integer.valueOf(hexStr, 16) & 0xffff);
        }
    }

    /**
     * 计算消息头和消息体的校验码
     *
     * @param datas
     * @return
     */
    public static int getCheckCode(String datas) {
        int code = HexUtil.get16210(datas.substring(0, 2));
        for (int i = 2; i < datas.length(); i = i + 2) {
            code = code ^ HexUtil.get16210(datas.substring(i, i + 2));
        }
        return code;
    }

    // 十六进制2十进制
    public static int get16210(String retrnStr) {
        String myStr[] = {"a", "b", "c", "d", "e", "f"};
        int result = 0;
        int n = 1;
        for (int i = retrnStr.length() - 1; i >= 0; i--) {
            String param = retrnStr.substring(i, i + 1);
            // System.out.println("param======"+i+"&&&&&&&&&&&"+param);
            for (int j = 0; j < myStr.length; j++) {
                if (param.equalsIgnoreCase(myStr[j])) {
                    param = "1" + String.valueOf(j);
                }
                // System.out.println("param======"+j+"&&&&&&&&&&&"+param);
            }
            result += Integer.parseInt(param) * n;
            // System.out.println("result======"+i+"&&&&&&&&&&&"+result);
            n *= 16;
        }
        return result;
    }

    // ----------------------------------------------------

    // -----------------------------------------------------

    /**
     * 补全两位
     *
     * @param s
     * @return
     */
    public static String len2(String s) {
        return (s.length() == 2 ? s : ("0" + s));
    }

    /**
     * 补全4位
     *
     * @param s
     * @return
     */
    public static String len4(String s) {
        return (s.length() == 4 ? s : (s.length() == 3 ? ("0" + s) : (s
                .length() == 2 ? ("00" + s) : ("000" + s))));
    }

    // byte2十进制高地位
    public static int gethax2strhighlew(byte[] b) {
        int result = 0;
        int aa = 0;
        for (int n = 0; n < b.length; n++) {
            result += (b[n] & 0xff) << aa;
            aa += 8;
        }
        return result;
    }

    // 对转换后的字符串进行长度不够的补位
    public static String set2Len(String retrnStr, int len, String place) {
        for (int i = retrnStr.length(); i < len; i++) {
            if ("prev".equals(place)) {
                retrnStr = "0" + retrnStr;
            } else {
                retrnStr += "0";
            }
        }
        System.out.println(retrnStr);
        return retrnStr;
    }

    /**
     * @param b
     * @return
     * @Description: TODO
     * <p>
     * 获取设备的时间
     * </p>
     * @Title: getDate
     * @author bait
     * @date 2016年9月26日 下午10:04:47
     */
    public static String getDate(byte[] b) {
        int b0 = b[0];
        int b1 = b[1];
        int b2 = b[2];
        int b3 = b[3];

        int yyyy = ((0x7f & (b1 >> 1)) + 2000);
        int MM = ((0x0f & (b1 << 3)) | (0x07 & (b0 >> 5)));
        int dd = (b0 & 0x1f);

        int HH = 0x1f & (b3 >> 3);
        int mm = ((0x07 & b3) << 3 | (0x07 & (b2 >> 5)));
        int ss = (b2 & 31) * 2;
        String date = yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm + ":"
                + ss;
        return date;
    }

    public static byte[] getBytes(byte[] data, int begin, int length) {
        byte[] n = new byte[length];
        for (int i = 0; i < length; i++) {
            n[i] = data[begin + i];
        }
        return n;
    }

    /*
     * 时间处理为压缩格式HEX
     */
    public static String date2HexStr(String date) {
        date = date.replaceAll("-", "");
        byte[] b_date = new byte[4];
        b_date[0] = (byte) Integer.parseInt(date.substring(0, 2));
        b_date[1] = (byte) Integer.parseInt(date.substring(2, 4));
        b_date[2] = (byte) Integer.parseInt(date.substring(4, 6));
        b_date[3] = (byte) Integer.parseInt(date.substring(6, 8));
        return HexUtil.byte2HexStr(b_date);
    }

    /**
     * @param date
     * @return
     * @Description: TODO
     * <p>
     * 时间转换为HEX
     * </p>
     * @Title: date2HexStr2
     * @author bait
     * @date 2016年9月26日 下午10:02:29
     */
    public static String date2HexStr2(String date) {
        date = date.replaceAll("-", "");
        date = date.replaceAll(":", "");
        date = date.replaceAll(" ", "");
        byte[] b_date = new byte[7];
        b_date[0] = (byte) Integer.parseInt(date.substring(0, 2));
        b_date[1] = (byte) Integer.parseInt(date.substring(2, 4));
        b_date[2] = (byte) Integer.parseInt(date.substring(4, 6));
        b_date[3] = (byte) Integer.parseInt(date.substring(6, 8));
        b_date[4] = (byte) Integer.parseInt(date.substring(8, 10));
        b_date[5] = (byte) Integer.parseInt(date.substring(10, 12));
        b_date[6] = (byte) Integer.parseInt(date.substring(12, 14));
        return HexUtil.byte2HexStr(b_date);
    }

    /**
     * @param 
     * @return
     * @Description: TODO
     * <p>
     * 将获取的数据转换为hex码
     * </p>
     * @Title: byte2HexStr
     * @author bait
     * @date 2016年9月19日 下午2:10:50
     */
    public static String byte2HexStr(byte[] bytes) {
      /*  String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }

            // System.out.println("stmp"+n+"======"+stmp);
            // System.out.println("hs==========="+stmp);
        }
        return hs.toUpperCase();*/
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();

    }

    private static byte uniteBytes(String src0, String src1) {
        byte b0 = Byte.decode("0x" + src0).byteValue();
        b0 = (byte) (b0 << 4);
        byte b1 = Byte.decode("0x" + src1).byteValue();
        byte ret = (byte) (b0 | b1);
        return ret;
    }

    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        // System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
        }
        return ret;
    }

    public static String getCheckCode(String retrnStr, int len) {
        byte[] data = hexStr2Bytes(retrnStr);
        int i;
        int sum;
        sum = data[1] ^ data[0];
        for (i = 2; i < len; i++) {
            sum = sum ^ data[i];
        }
        System.out.println("sum=" + sum);
        // System.out.println(byte2HexStr(new byte[]{(byte) sum}));
        return byte2HexStr(new byte[]{(byte) sum});
    }

    /**
     * 取校验码
     *
     * @param data
     * @return
     */
    public static String getDataCheckCode(String data) {
        return data.substring(data.length() - 2, data.length());
    }

    public static byte[] getCheckCode(byte[] data, int len) {
        int i;
        int sum;
        sum = data[1] ^ data[0];
        for (i = 2; i < len; i++) {
            sum = sum ^ data[i];
        }
        return getBytes(sum);
    }

    /**
     * 将高字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int hBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[i] >= 0) {
                s = s + b[i];
            } else {
                s = s + 256 + b[i];
            }
            s = s * 256;
        }
        if (b[3] >= 0) {
            s = s + b[3];
        } else {
            s = s + 256 + b[3];
        }
        return s;
    }

    /**
     * 将低字节数组转换为int
     *
     * @param b byte[]
     * @return int
     */
    public static int lBytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }
            s = s * 256;
        }
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
    }

    /**
     * 高字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short hBytesToShort(byte[] b) {
        int s = 0;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        s = s * 256;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 低字节数组到short的转换
     *
     * @param b byte[]
     * @return short
     */
    public static short lBytesToShort(byte[] b) {
        int s = 0;
        if (b[1] >= 0) {
            s = s + b[1];
        } else {
            s = s + 256 + b[1];
        }
        s = s * 256;
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        short result = (short) s;
        return result;
    }

    /**
     * 16转2
     *
     * @param hexString
     * @return
     */
    public static String hex2Bin(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            return null;
        }
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(
                    hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }

    /**
     * 十六进制字符串转float.
     *
     * @param hexStr
     * @return
     */
    public static Float hexStr2Float(String hexStr) throws Exception {
        Float result = null;
        // 先通过apahce的 hex类转换十六进制字符串为byte数组. 也可以自己用JDK原声的方法来循环转
        // Character.digit(ch, 16);
        byte[] decodes = Hex.decodeHex(hexStr.toCharArray());
        // 获得byte转float的结果
        result = getFloat(decodes, 0);
        return result;
    }

    /**
     * 通过byte数组取得float
     *
     * @param b
     * @param index 第几位开始取.
     * @return
     */
    public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * 16进制转时间
     *
     * @param data
     * @return
     */
    public static String hexStr2DateTime(String data) {
        String binStr = hex2Bin(data);
        StringBuffer dateStr = new StringBuffer("");
        dateStr.append("20").append(
                len2(Integer.valueOf(binStr.substring(0, 6), 2).toString()));
        dateStr.append("-");
        dateStr.append(len2(Integer.valueOf(binStr.substring(6, 10), 2)
                .toString()));
        dateStr.append("-");
        dateStr.append(len2(Integer.valueOf(binStr.substring(10, 15), 2)
                .toString()));
        dateStr.append(" ");
        dateStr.append(len2(Integer.valueOf(binStr.substring(15, 20), 2)
                .toString()));
        dateStr.append(":");
        dateStr.append(len2(Integer.valueOf(binStr.substring(20, 26), 2)
                .toString()));
        dateStr.append(":");
        dateStr.append(len2(Integer.valueOf(binStr.substring(26, 32), 2)
                .toString()));
        return dateStr.toString();
    }

    /**
     * 字符串转换为16进制字符串
     *
     * @param
     * @return
     */
  /*  public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }*/
    public static String stringToHexString(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        StringBuilder sb = new StringBuilder(""); //StringBuilder是一个类，可以用来处理字符串,sb.append()字符串相加效率高
        byte[] bs = str.getBytes();//String的getBytes()方法是得到一个操作系统默认的编码格式的字节数组
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4; // 高4位, 与操作 1111 0000
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;  // 低四位, 与操作 0000 1111
            sb.append(chars[bit]);

        }
        return sb.toString();

    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 数字补零
     *
     * @param s
     * @param length
     * @return
     */
    public static byte[] addZero(String s, int length) {
        byte[] head = s.getBytes();
        String len = Integer.toHexString(head.length);
        byte[] lens = hexStr2Bytes(len2(len));
        byte[] heads = new byte[length - head.length];
        for (int i = 0; i < length - head.length; i++) {
            heads[i] = (byte) 0x00;
        }
        byte[] tmp = append(lens, head);
        byte[] result = append(tmp, heads);
        return result;
    }


    //转换16进制字符串 start-------------------
    public static int decodeHexInt(String str) {

        str = HighLowHex(spaceHex(str));
        int value = Integer.parseInt(str, 16);
        return value;
    }

    public static int decodeHexIntnew(String str) {

        str = HighLowHex(spaceHex(str));
        Short t = Integer.valueOf(str, 16).shortValue();
        int value = Integer.parseInt(String.valueOf(t));
        return value;
    }

    public static String decodeHexString(String str) {

        str = HighLowHex(spaceHex(str));
        return str;
    }

    private static String spaceHex(String str) {
        char[] array = str.toCharArray();
        if (str.length() <= 2) {
            return str;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            int start = i + 1;
            if (start % 2 == 0) {
                buffer.append(array[i]).append(" ");
            } else {
                buffer.append(array[i]);
            }
        }
        return buffer.toString();
    }

    private static String HighLowHex(String str) {
        if (str.trim().length() <= 2) {
            return str;
        }
        List<String> list = Arrays.asList(str.split(" "));
        Collections.reverse(list);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : list) {
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }

    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
// 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }


    //转换16进制字符串end-------------------
    public static void main(String[] args) {

   /*     String sss = stringToHexString("{\"Command\":\"SetPreset\",\"_session\":1,\"PresetId\":4,\"PresetName\":\"预置点 4\"}");
        System.err.println("{\"Command\":\"SetPreset\",\"_session\":1,\"PresetId\":4,\"PresetName\":\"预置点 4\"}".length());*/

        String sss = string2Unicode("郑");
        String re = stringToHexString(sss);
        System.err.println(re + "==" + re.length());



    }

}

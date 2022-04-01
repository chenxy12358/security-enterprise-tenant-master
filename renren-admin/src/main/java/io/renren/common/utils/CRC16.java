package io.renren.common.utils;


public class CRC16 {

	public static int alex_crc16(byte[] buf, int len) {
		int i, j;
		int c, crc = 0xFFFF;
		for (i = 0; i < len; i++) {
			c = buf[i] & 0x00FF;
			crc ^= c;
			for (j = 0; j < 8; j++) {
				if ((crc & 0x0001) != 0) {
					crc >>= 1;
					crc ^= 0xA001;
				} else
					crc >>= 1;
			}
		}
		return (crc);
	}

	public static void main(String[] args) {
		CRC16 crc16 = new CRC16();
		byte[] d=new byte[17];
        d[0]=(byte)0x55;
        d[1]=(byte)0xAA;
        d[2]=(byte)0x01;
        d[3]=(byte)0x80;
        d[4]=(byte)0x01;
        d[5]=(byte)0xAA;
        d[6]=(byte)0xAA;
        d[7]=(byte)0xAA;
        d[8]=(byte)0xAA;
        d[9]=(byte)0xCC;
        d[10]=(byte)0xCC;
        d[11]=(byte)0xCC;
        d[12]=(byte)0xCC;
        d[13]=(byte)0x04;
        d[14]=(byte)0x00;
        d[15]=(byte)0x00;
        d[16]=(byte)0x01;
		/*d[9]=(byte)0xf9;
		d[10]=(byte)0x6c;*/

        int a=crc16.alex_crc16(d, 17);
        System.out.println(Integer.toHexString(a).toUpperCase());
	}
}

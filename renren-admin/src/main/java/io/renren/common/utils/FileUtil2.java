package io.renren.common.utils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;

public class FileUtil2 {
    public static void main(String[] args) throws Exception {
        System.out.println(readTxt().replaceAll(" ", ""));
        txtTransImage(readTxt().replaceAll(" ", "").toUpperCase(), "c:/image11111.jpeg");

    }


    private static String readTxt() throws Exception {
        Reader inputStream = new FileReader("C:\\Users\\DS\\Desktop/test.txt");
        BufferedReader reader = new BufferedReader(inputStream);
        String str = "";
        StringBuilder builder = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            builder.append(str);
        }
        return builder.toString();
    }

    private static void txtTransImage(String src, String filePath) throws Exception {
        if (src == null || ("").equals(src)) {
            return;
        }
        FileOutputStream outputStream = new FileOutputStream(filePath);
        byte[] bytes = src.getBytes();
        for (int i = 0; i < bytes.length; i += 2) {
            outputStream.write(charToInt(bytes[i]) * 16 + charToInt(bytes[i + 1]));
        }
        outputStream.close();
    }

    private static int charToInt(byte ch) {
        int val = 0;
        if (ch >= 0x30 && ch <= 0x39) {
            val = ch - 0x30;
        } else if (ch >= 0x41 && ch <= 0x46) {
            val = ch - 0x41 + 10;
        }
        return val;
    }

}
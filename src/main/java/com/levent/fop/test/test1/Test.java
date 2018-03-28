package com.levent.fop.test.test1;

import org.apache.fop.fonts.apps.TTFReader;

public class Test {
    public static void main(String[] args) {
        String[] parameters = {
                "-ttcname",
                "simkai",           //字体名称
                "src//main//resources//conf//msyh.ttf", //字体ttf文件地址
                "src//main//resources//conf//msyh.xml", //要生成的xml文件的地址
        };
        TTFReader.main(parameters);
    }
}

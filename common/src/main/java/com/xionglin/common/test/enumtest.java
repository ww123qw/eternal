package com.xionglin.common.test;

public class enumtest {

    public enum one{
        OK(0, "成功","fff"),
        ERROR_A(100, "错误A","fff"),
        ERROR_B(200, "错误B","fff");
        one(int number, String description,String description1) {
            this.code = number;
            this.description = description;
            this.description1 = description;
        }

        private int code;
        private String description;
        private String description1;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static void main(String[] args) {

System.out.print(one.ERROR_A.getCode());
        System.out.print(one.values());
    }
}

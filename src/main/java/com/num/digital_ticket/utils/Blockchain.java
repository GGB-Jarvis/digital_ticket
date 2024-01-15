package com.num.digital_ticket.utils;

public class Blockchain {
    //创建账户地址
    public static String createUserUrl = "http://api.ctblock.cn/api/account/createAccount";
    //public static String createUserUrl = "http://47.98.187.17:8000/api/account/createAccount";
    //导出账户地址
    public static String exportUser = "http://api.ctblock.cn/api/account/exportAccount";
    //public static String exportUser = "http://47.98.187.17:8000/api/account/exportAccount";
    //创建合约地址
    //public static String createCollect = "http://47.98.187.17:8000/api/account/createctCollect";
    public static String createCollect = "http://api.ctblock.cn/api/account/createctCollect";
    //批量铸造
    public static String createNft = "http://api.ctblock.cn/api/account/createctNft1155Async";
    //public static String createNft = "http://47.98.187.17:8000/api/account/createctNft1155Async";

    // 交易
    public static String transfer = "http://api.ctblock.cn/api/account/transfer_f";
    //回调地址
    public static String reback = "http://artvery.txwsyun.cn:8080/numshop/reback?";

    //本地账号
    public static String address = "0xc072Ac9D4D99d936EF4Dd9D8D0c47985389905A4";

    //本地密码
    public static String password = "ecedf2ade10b695ed598cc07502f0d";

    //本地私钥
    public static String privateKey = "0x112287931df235606cc5a2bbc427e56dffa27163d371eda5569163185f75a220";

    //合约地址
    //public static String collectAddress = "0x589038Dfc6eA17dA9a152E2C8716D1873507AFCe";
    public static String collectAddress = "0x3b63b06acE9787B8FcF44944F5676C66B3636dC0";

    //交易hash
    //public static String hash = "0x1fdaf8f51f7bea92d4f73643de9aadb0d1cd7376a8c9f8328bb823aa7d45bc2f";
    public static String hash = "0xe3329f78d6afbcfede32b7b76fd59df5d2c8c9a9833801ef9fed9706ead4fa58";


}

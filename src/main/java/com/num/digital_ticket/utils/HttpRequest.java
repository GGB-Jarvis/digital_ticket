package com.num.digital_ticket.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.num.digital_ticket.entity.Activity;
import com.num.digital_ticket.entity.Ticket;
import com.num.digital_ticket.sevice.impl.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class HttpRequest {
    public static String toUrl(String path, String method, String data) {
        System.out.println(path);
        System.out.println(data);
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(path);
            //打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            /**设置URLConnection的参数和普通的请求属性****start***/
            //设置是否向httpUrlConnection输出，设置是否从httpUrlConnection读入，此外发送post请求必须设置这两个
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //GET和POST必须全大写
            conn.setRequestMethod(method);
            // 设置不用缓存
            conn.setUseCaches(false);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            byte[] tmp = new byte[0];
            if ("POST".equals(method) && data != null && data.length() > 0) {
                //转换为字节数组
                tmp = data.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(tmp.length));
            }
            // 设置文件类型:
            conn.setRequestProperty("contentType", "application/json");
            conn.setRequestProperty("content-Type", "application/json");
            /**设置URLConnection的参数和普通的请求属性****end***/

            /**GET方法请求*****start*/
            /**
             * 如果只是发送GET方式请求，使用connet方法建立和远程资源之间的实际连接即可；
             * 如果发送POST方式的请求，需要获取URLConnection实例对应的输出流来发送请求参数。
             * */
            conn.connect();
            /**GET方法请求*****end*/

            /***POST方法请求****start*/
            if ("POST".equals(method) && data != null && data.length() > 0) {
                //获取URLConnection对象对应的输出流
                OutputStream out = conn.getOutputStream();
                //发送请求参数即数据
                out.write(tmp);
                out.flush();
                out.close();
            }
            /***POST方法请求****end*/
            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
                System.out.println("连接成功");
                //获取URLConnection对象对应的输入流
                InputStream is = conn.getInputStream();
                //构造一个字符流缓存
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String str = "";
                while ((str = br.readLine()) != null) {
                    //解决中文乱码问题
                    str = new String(str.getBytes(), "UTF-8");
                    sb.append(str);
                    System.out.println(str);
                }
                //关闭流
                is.close();
            }
            //断开连接，最好写上，disconnect是在底层tcp socket链接空闲时才切断。如果正在被其他线程使用就不切断。
            //固定多线程的话，如果不disconnect，链接会增多，直到收发不出信息。写上disconnect后正常一些。
            conn.disconnect();
            System.out.println("完整结束");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static String createUser(String password){
        password = MD5Utils.getMd5(password);
        Map map = new HashMap();
        map.put("password", password);
        String post = toUrl(Blockchain.createUserUrl,"POST", JSON.toJSONString(map));
        JSONObject obj = JSON.parseObject(post);
        String result = null;
        if("200".equals(obj.getString("code"))){
            result = obj.getJSONObject("result").getString("address");
        }
        return result;
    }
    public static String getPrivateKey(String password,String address){
        Map map = new HashMap();
        map.put("password", password);
        map.put("address", address);
        String post = toUrl(Blockchain.exportUser, "POST", JSON.toJSONString(map));
        System.out.println("post:"+post);
        JSONObject obj = JSON.parseObject(post);
        String result = null;
        if("200".equals(obj.getString("code"))){
            result = obj.getJSONObject("result").getString("privateKey");
        }
        return result;
    }


    public static String[] createCollect(String name,String address,String password,int type){
        Map map = new HashMap();
        map.put("password", password);
        map.put("address", address);
        map.put("type", type);
        Map sub = new HashMap();
        sub.put("name", name);
        sub.put("symbol", "ALN");
        sub.put("tokenUrlPrefix", "http://artvery.txwsyun.cn/");
        sub.put("contractUrl", "http://artvery.txwsyun.cn/xx");
        map.put("cMetadata", sub);
        String post = toUrl(Blockchain.createCollect, "POST", JSON.toJSONString(map));
        System.out.println("post:"+post);
        JSONObject obj = JSON.parseObject(post);
        String[] result = new String[2];
        if("200".equals(obj.getString("code"))){
            result[0] = obj.getJSONObject("result").getString("collectAddress");
            result[1] = obj.getJSONObject("result").getString("hash");
        }
        return result;
    }

    /**
     * 批量创建
     * 待修改
     * @param address
     * @param password
     * @param collectAddress
     * @param file
     * @return
     */
    public static String createNft(String address,
                                   String password,
                                   String collectAddress,
                                   String file,
                                   String title,
                                   String description,
                                   String author,
                                   String authorDesc,
                                   String supply){
        Map<String, Object> sub = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("password", password);
        map.put("address", address);
        map.put("collectAddress", collectAddress);
        sub.put("title", title);
        sub.put("description", description);
        sub.put("author", author);
        sub.put("authorDesc", authorDesc);
        map.put("file", file);
        map.put("supply", supply);
        map.put("rebackUrl", Blockchain.reback + "reback=1");
        map.put("data", sub);
        System.out.println("map:"+JSON.toJSONString(map));
        String post = toUrl(Blockchain.createNft, "POST", JSON.toJSONString(map));
        System.out.println("请求结果是:|"+post+"|");
        JSONObject jsonObject = JSON.parseObject(post);
        String result = null;
        if("200".equals(jsonObject.getString("code"))){
            result = jsonObject.getJSONObject("result").getString("tokenId");
        }
        return result;
    }

    public static boolean transfer(String address,
                                  String password,
                                  String collectAddress,
                                  String tokenId,
                                  String amount,
                                  String to,
                                  String orderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("password", password);
        map.put("address", address);
        map.put("collectAddress", collectAddress);
        map.put("tokenId", tokenId);
        map.put("amount", amount);
        map.put("to", to);
        map.put("orderId", orderId);
        map.put("rebackUrl", Blockchain.reback + "reback=1");
        String resp = toUrl(Blockchain.transfer, "POST", JSON.toJSONString(map));
        JSONObject jsonObject = JSON.parseObject(resp);
        if("200".equals(jsonObject.getString("code"))) {
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
//        String post = "{\"code\":200,\"message\":\"null\",\"result\":{\"address\":\"0xc072Ac9D4D99d936EF4Dd9D8D0c47985389905A4\",\"privateKey\":\"0x112287931df235606cc5a2bbc427e56dffa27163d371eda5569163185f75a220\"}}";
//        JSONObject obj = JSON.parseObject(post);
//        String result = null;
//        if("200".equals(obj.getString("code"))){
//            result = obj.getJSONObject("result").getString("tokenId");
//        }
//        System.out.println(result);
//        String[] result = createCollect("数字藏品",Blockchain.address,Blockchain.password, 10);
//        System.out.println(Arrays.toString(result));
//        String nft = createNft(Blockchain.address, Blockchain.password,Blockchain.collectAddress,"http://artvery.txwsyun.cn:8080/numshop/getImage/img_2023_11_14103057856.jpg","1","中国营造学社梁思成滇西考查公函","","2045矩阵空间","");
//        System.out.println(nft);
//        String a= "0xc072Ac9D4D99d936EF4Dd9D8D0c47985389905A4c12345678901699952964727";
//        String b = "0xc072Ac9D4D99d936EF4Dd9D8D0c47985389905A4c12345678901699952964727";
//        System.out.println(a.equals(b));
        //String url = toUrl("https://apis.juhe.cn/fapigw/naming/query?key=111111", "GET", null);

        HttpRequest.transfer(Blockchain.address,
                Blockchain.password,
                Blockchain.collectAddress,
                "0xc072Ac9D4D99d936EF4Dd9D8D0c47985389905A4c12345678901705302563412",
                "1",
                "0x9DFAc6f20f63315D8DbbF0c6916496095B9FB678",
                "202206203545888878");
    }
}

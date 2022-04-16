package com.miniprogramlearn.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

public class AESUtil {
    /**加密方式 加密算法  这里只是为了说明后面没有用到*/
    public static String KEY_ALGORITHM="AES";
    /**
     * //数据填充方式
     * 解密的时候一般会用PKCS7 加密好像会用5
     * */
    String algorithmStr="AES/CBC/PKCS7Padding";
    /**
     * 避免重复new生成多个BouncyCastleProvider对象，因为GC回收不了，会造成内存溢出
     * 只在第一次调用decrypt()解密方法时才new 对象*/
    public static boolean initialized=false;
    /**
     *
     * @param originalContent 字节类型明文数据
     * @param encryptKey 自定义的字节类型加密密钥（解密也需要使用）
     * @param ivByte 自定义的字节类型的初始化向量，
     *               在实际加密过程，会在算法内部再次进行进一步的转换为真实使用的iv
     *               （解密也需要使用）
     * @return
     */
    //加密算法方法
    public static byte[] encrypt(byte[] originalContent,byte[] encryptKey,byte[] ivByte){
        initialized();//初始化

        try{
            /**创建加密工具需要的实例 这里加密用的是PKCS5,但是解密用的是PKCS7因为小程序规定解密使用7
             *  应该这个PKCS7这类模式在加密和解密的时候用的都应该是相同的，这里只是因为方柏霓演示上面的加密写了5*/
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS5Padding");
            /**SecretKeySpec是key的子类，这里需要使用自定义个字节类型的数据密钥 生成一个密钥类实例*/
            SecretKeySpec secretKeySpec=new SecretKeySpec(encryptKey,"AES");

            /**Cipher.ENCRYPT_MODE,表示进行加密，
             * 传入生成的密钥，
             * 然后使用IvParameterSpec构造方法使用自定义的初始化向量的字节数据生成初始化向量
             * IvParameterSpec 可以看作AlgorithmParameters的子类*/
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,new IvParameterSpec(ivByte));

            /**开始加密，传入明文的字节数据内容 返回的就是加密后的字节数据内容*/
            byte[] encryptedStr=cipher.doFinal(originalContent);
            return encryptedStr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * AES解密
     * 填充模式AES/CBC/PKCS7Padding
     * 解密模式128
     * @param content
     *            目标密文，在传入进行解密的时候需要的是字节类型
     * @param aesKey
     *          之前在加密的时候的那个自定义的字节类型数据的密钥，传入的时候需要的是字节类型
     * @param ivByte
     *          之前在加密的时候自定义个初始化向量数据，传入的需要的是字节类型，而且内部还需要进行再次转换生成，算法需要的那个iv类
     *
     *
     *
     */
    //解密算法
    public static byte[] decrypt(byte[] content,byte[] aesKey,byte[] ivByte){
        initialized();
        try{
            /**创建解密工具需要的实例 这里解密用的是PKCS7因为微信小程序规定了用7 ,但是加密用的是PKCS5
             * 应该这个PKCS7在加密和解密的时候用的都应该是相同的，这里只是因为方柏霓演示上面的加密写了5*/
            Cipher cipher=Cipher.getInstance("AES/CBC/PKCS7Padding");

            /**SecretKeySpec是key的子类，这里需要使用自定义个字节类型的数据密钥 生成一个密钥类实例*/
            Key secretKey=new SecretKeySpec(aesKey,"AES");

            /**Cipher.ENCRYPT_MODE,表示进行加密，
             * 传入生成的密钥，
             * 之前加密直接使用IvParameterSpec构造方法使用自定义的初始化向量的字节数据生成初始化向量
             * 但是解密时还需要使用类AlgorithmParameters实例来初始化 上面使用构造方法得到的初始化向量 最后得到IV
             * AlgorithmParameters实例的创建，使用它本身的静态getInstance方法，传入解密算法的名称作为参数*/
            cipher.init(Cipher.DECRYPT_MODE,secretKey,generateIV(ivByte));

            /**开始解密解密后的数据就是密文解密后的 字节类型数据*/
            byte[] result = cipher.doFinal(content);

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 既然解密加密都需要使用的是数据的字节形式，所以在使用之前需要将字符串等转换为字节形式
     * 但是转换为字节形式有不同的编码方式
     * 一般为了更多的在网络设备上使用，会采用BASE64的编码，方式
     * 设置BASE64的编码的工具类有很多，之前会使用apache的，但是现在会使用java8的base64，速度更快
     * 下面的方法是对字符产与base64编码对应的字节数据之间进行相应的转换
     *
     * 在这里为什么要使用base64的编码格式了’
     * 因为小程序的官方文档说明了，我们获取到的session_key和密文都是base64格式的在解密之前需要使用base64解码
     * 官方说明：
     * 需要对接口返回的加密数据(encryptedData) 进行对称解密。 解密算法如下：
     * 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充。
     * 对称解密的目标密文为 Base64_Decode(encryptedData)。
     * 对称解密秘钥 aeskey = Base64_Decode(session_key), aeskey 是16字节。
     * 对称解密算法初始向量 为Base64_Decode(iv)，其中iv由数据接口返回。
     *
     * java11 base64用法：
     * //编码
     * //String encodeToString = Base64.getEncoder().encodeToString(msg.getBytes());
     * //解码
     * //byte[] decode = Base64.getDecoder().decode(encodeToString);*/
    public static byte[] decodeBase(String origin){//将字符串解码为byte[]
        byte[] decode = Base64.getDecoder().decode(origin);
        return decode;
    }

    /**下面这个函数表示对解密后的字节数据用指定的编码方式转换为字符串的工具*/
    public static String encodeStr(byte[] resultByte,String encodeFormat) throws UnsupportedEncodingException {
        if(resultByte!=null&&resultByte.length>0){
            String result=new String(resultByte,encodeFormat);
            return result;
        }
        return null;
    }







    /**BouncyCastle作为安全提供，防止我们加密解密时候因为jdk内置的不支持改模式运行报错。
     * 初始化方法*/
    public static void initialized(){
        if(initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized=true;
    }

    /**解密的时候生成IV的方法，传入的是加密的时候自定义的AlgorithmParameters类型的iv 字节数据
     * IvParameterSpec 可以看作AlgorithmParameters的子类
     */
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters parameters=AlgorithmParameters.getInstance("AES");

        parameters.init(new IvParameterSpec(iv));
        return parameters;
    }


}

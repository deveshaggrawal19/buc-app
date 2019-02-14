package com.buyucoinApp.buyucoin.cipher;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherAES {
    private static final String ALGO = "AES";
    private byte[] keyvalue;

    public CipherAES(String key){
        keyvalue = key.getBytes();
    }

    public String encrypt(String Data) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        byte ivbytes[] =  new byte[c.getBlockSize()];
        IvParameterSpec iv = new IvParameterSpec(ivbytes);
        c.init(Cipher.ENCRYPT_MODE,key,iv);
        byte[] encval = c.doFinal(Data.getBytes());
        byte[] encryptedValue =  Base64.encode(encval,Base64.DEFAULT);
        return new String(encryptedValue);
    }

    public String decrypt(String encrypteddata) throws Exception{
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
        byte ivbytes[] =  new byte[c.getBlockSize()];
        IvParameterSpec iv = new IvParameterSpec(ivbytes);
        c.init(Cipher.DECRYPT_MODE,key,iv);
        byte[] decodedValue = Base64.decode(encrypteddata,Base64.DEFAULT);
        byte[] decval = c.doFinal(decodedValue);
        return new String(decval);
    }

    private Key generateKey() {
        return new SecretKeySpec(keyvalue,ALGO);
    }


}

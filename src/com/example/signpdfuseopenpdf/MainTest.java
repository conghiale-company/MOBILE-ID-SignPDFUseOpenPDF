package com.example.signpdfuseopenpdf;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Enumeration;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class MainTest {
    public static void main(String[] args) throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream("D:\\OneDrive\\work\\mobile-id\\internal\\keystores\\vudp.p12");
        keystore.load(is, "12345678".toCharArray());

        Enumeration<String> e = keystore.aliases();
        while (e.hasMoreElements()) {
            String aliasName = e.nextElement();
            PrivateKey privKey = (PrivateKey) keystore.getKey(aliasName, "12345678".toCharArray());
            if (privKey != null) {
                break;
            }
        }
    }
}

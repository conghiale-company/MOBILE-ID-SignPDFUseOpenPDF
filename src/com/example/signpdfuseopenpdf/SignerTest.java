package com.example.signpdfuseopenpdf;


import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * @author DELL
 */
public class SignerTest {

    final private static String KEY01 = "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";
    final private static String KEY01_PASS = "12345678";
    //
    final private static String KEY02 = "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";
    final private static String KEY02_PASS = "12345678";

    public static List<SignerKey> init() throws Exception {

        List<SignerKey> keys = new ArrayList<>();

        KeyStore keystore = null;
        InputStream is = null;
        Enumeration<String> e = null;
        PrivateKey privKey = null;
        X509Certificate cert = null;
        List<X509Certificate> x509CertChain = null;
        List<Certificate> certChain = null;
        String aliasName = null;
        String file = null;
        String filePassPhrase = null;
        Certificate[] chain = null;

//        x509CertChain = new ArrayList<>();
//        certChain = new ArrayList<>();
//        keystore = KeyStore.getInstance("PKCS12");

//        x509CertChain = new ArrayList<>();
//        certChain = new ArrayList<>();
//        keystore = KeyStore.getInstance("PKCS12");
//        is = new ByteArrayInputStream(Base64.getDecoder().decode(KEY01));
//        keystore.load(is, KEY01_PASS.toCharArray());
//
//        e = keystore.aliases();
//        while (e.hasMoreElements()) {
//            aliasName = e.nextElement();
//            privKey = (PrivateKey) keystore.getKey(aliasName, KEY01_PASS.toCharArray());
//            if (privKey != null) {
//                break;
//            }
//        }
//        cert = (X509Certificate) keystore.getCertificate(aliasName);
//        chain = keystore.getCertificateChain(aliasName);
//        for (Certificate c : chain) {
//            x509CertChain.add((X509Certificate) c);
//            certChain.add(c);
//        }
//        keys.add(new SignerKey(privKey, cert, x509CertChain, certChain));

        x509CertChain = new ArrayList<>();
        certChain = new ArrayList<>();
        keystore = KeyStore.getInstance("PKCS12");
        is = new ByteArrayInputStream(Base64.getDecoder().decode(KEY02));
        keystore.load(is, KEY02_PASS.toCharArray());

        e = keystore.aliases();
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
            privKey = (PrivateKey) keystore.getKey(aliasName, KEY02_PASS.toCharArray());
            if (privKey != null) {
                break;
            }
        }
        cert = (X509Certificate) keystore.getCertificate(aliasName);
        chain = keystore.getCertificateChain(aliasName);
        for (Certificate c : chain) {
            x509CertChain.add((X509Certificate) c);
            certChain.add(c);
        }
        keys.add(new SignerKey(privKey, cert, x509CertChain, certChain));

        return keys;
    }

    public static class SignerKey {

        private PrivateKey privateKey;
        private X509Certificate signerCert;
        private List<X509Certificate> x509CertChain;
        private List<Certificate> certChain;

        public SignerKey(PrivateKey privateKey, X509Certificate signerCert, List<X509Certificate> chain, List<Certificate> certChain) {
            this.privateKey = privateKey;
            this.signerCert = signerCert;
            this.x509CertChain = chain;
            this.certChain = certChain;
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public X509Certificate getSignerCert() {
            return signerCert;
        }

        public List<X509Certificate> getX509CertChain() {
            return x509CertChain;
        }

        public List<Certificate> getCertChain() {
            return certChain;
        }

    }

    public static byte[] paddingSHAOID(byte[] hashedData, String hashAlg) throws Exception {

        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(hashAlg);
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] readFile(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }

    public static void writeFile(String filePath, byte[] data) throws Exception {
        Path path = Paths.get(filePath);
        Files.write(path, data);
    }
}


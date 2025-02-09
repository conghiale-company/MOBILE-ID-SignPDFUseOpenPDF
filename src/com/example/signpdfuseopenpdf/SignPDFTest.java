package com.example.signpdfuseopenpdf;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import vn.mobileid.openpdf.Rectangle;
import vn.mobileid.openpdf.*;

import java.awt.*;
import java.io.File;
import java.security.Security;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class SignPDFTest {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws Exception {
        List<SignerTest.SignerKey> keys = SignerTest.init();

        byte[] data = SignerTest.readFile("file\\ok.pdf");
        List<PDFSignObject> dataToBeSigns = new ArrayList<>();

        PDFSignObject pdf1 = new PDFSignObject();
        pdf1.setDocument(data);

//        Set coordinates: x1, y1, x2, y2
        pdf1.setRectangle(new Rectangle(250, 150, 500, 180));

//        Set location and page number
        pdf1.setLocation("Hanoi, Vietnam");
        pdf1.setPageNo("Last");

//        Set reason for signing.
        pdf1.setReason("Document Approval");

//        Configure visibility status
        pdf1.setVisibleValidationSymbol(false);

//        Add padding to text.
        pdf1.setTextPaddingLeft(30);

//        Configure signature image
        SignatureImage signatureImage = new SignatureImage(SignerTest.readFile("file\\logo.png"));
        signatureImage.setImageAligment(ImageAligment.LEFT_MIDDLE);
        signatureImage.scaleToFit(50, 50);
        pdf1.setSignatureImage(signatureImage);

//        Set up signer information
        pdf1.setSignerInformation("Ký bởi: {signby}\n"
                + "CCCD: {personalid}\n"
                + "Tổ chức: {organize}\n"
                + "MST: {enterpriseid}\n"
                + "Chức vụ: {title}\n"
                + "SN: {serialnumber}\n"
                + "Valid from: {validfrom}\n"
                + "Valid to: {validto}\n"
                + "CA: {issuer}\n"
                + "Lý do: {reason}\n"
                + "Ký ngày: {date}\n"
                + "Nơi ký: {location}"
        );

//        Align text from right to left
        pdf1.setTextAligment(TextAlignment.LEFT_MIDDLE); //LEFT_MIDDLE

//        Date signed format
//        pdf1.setDatetimeFormat("dd/MM/yyyy HH:mm:ss");

//        Lock file after signing
//        pdf1.setLockAfterSigning(false);

//        Enable signature display
//        pdf1.setVisibleSignature(true);

//        Make sure not to show authentication status
        pdf1.setVisibleValidationSymbol(false);

        dataToBeSigns.add(pdf1);

        PDFSigner signer = new PDFSigner();
        PDFSignerResult pdfSignerResult = signer.initSign(dataToBeSigns, keys.get(0).getCertChain());
        List<byte[]> hashList = pdfSignerResult.getHashesList();

        List<byte[]> signatures = new ArrayList<>();

        for (byte[] h : hashList) {
            byte[] dtbs = SignerTest.paddingSHAOID(h, "SHA-256");
            Signature s = Signature.getInstance("NONEwithRSA");
            s.initSign(keys.get(0).getPrivateKey());
            s.update(dtbs);
            signatures.add(s.sign());
        }

        PDFSignerResult finalSign = signer.finalSign(pdfSignerResult.getTemporalDatas(), signatures);
        List<byte[]> signedDocs = finalSign.getSignedDocuments();
        Desktop desktop = Desktop.getDesktop();

        for (int i = 0; i < signedDocs.size(); i++) {
            SignerTest.writeFile("file\\ok" + i + ".signed.pdf", signedDocs.get(i));
            File myFile = new File("file\\ok" + i + ".signed.pdf");
            desktop.open(myFile);
        }
    }

}

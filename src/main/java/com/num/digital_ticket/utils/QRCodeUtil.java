package com.num.digital_ticket.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeUtil {
        private QRCodeUtil() {
            // 工具类不应该被实例化
        }

        public static void main(String[] args) {
            String ticketInfo = "YourTicketInfo"; // 门票信息
            String filePath = "ticket_qrcode.png";

            try {
                QRCodeUtil.generateQRCode(ticketInfo, filePath);
                System.out.println("QR code generated successfully.");

                String decodedInfo = QRCodeUtil.readQRCode(filePath);
                System.out.println("Decoded information: " + decodedInfo);
            } catch (IOException | NotFoundException e) {
                e.printStackTrace();
            }
        }

        public static void generateQRCode(String ticketInfo, String filePath) throws IOException {
            int size = 300;
            String fileType = "png";

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.L);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.QR_VERSION, 5);

            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = writer.encode(ticketInfo, BarcodeFormat.QR_CODE, size, size, hints);
                BufferedImage bufferedImage = toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, fileType, new File(filePath));
            } catch (Exception e) {
                throw new IOException("Failed to generate QR code", e);
            }
        }

        public static String readQRCode(String filePath) throws IOException, NotFoundException {
            BufferedImage bufferedImage = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            MultiFormatReader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(bitmap);
                return result.getText();
            } catch (ReaderException e) {
                throw new IOException("Could not decode QR code", e);
            }
        }

        private static BufferedImage toBufferedImage(BitMatrix matrix) {
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            return image;
        }
}

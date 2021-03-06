package com.mengroba.barcodecamerascan;


/**
 * Created by mengroba on 30/01/2017.
 */

public final class PortraitOrientation {

    private final String contents;
    private final String formatName;
    private final byte[] rawBytes;
    private final Integer orientation;
    private final String errorCorrectionLevel;

    PortraitOrientation() {
        this(null, null, null, null, null);
    }

    PortraitOrientation(String contents,
                      String formatName,
                      byte[] rawBytes,
                      Integer orientation,
                      String errorCorrectionLevel) {
        this.contents = contents;
        this.formatName = formatName;
        this.rawBytes = rawBytes;
        this.orientation = orientation;
        this.errorCorrectionLevel = errorCorrectionLevel;
    }

    /**
     * @return raw content of barcode
     */
    public String getContents() {
        return contents;
    }

    /**
     * @return name of format, like "QR_CODE", "UPC_A". See {@code BarcodeFormat} for more format names.
     */
    public String getFormatName() {
        return formatName;
    }

    /**
     * @return raw bytes of the barcode content, if applicable, or null otherwise
     */
    public byte[] getRawBytes() {
        return rawBytes;
    }

    /**
     * @return rotation of the image, in degrees, which resulted in a successful scan. May be null.
     */
    public Integer getOrientation() {
        return orientation;
    }

    /**
     * @return name of the error correction level used in the barcode, if applicable
     */
    public String getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }

    @Override
    public String toString() {
        String dialogText = "Formato: " + formatName + '\n' +
                "Contenidos: " + contents + '\n' +
                "Raw bytes: (" + rawBytes + " bytes)\n" +
                "Orientacion: " + orientation + '\n' +
                "Error: " + errorCorrectionLevel + '\n';
        int rawBytesLength = rawBytes == null ? 0 : rawBytes.length;
        return dialogText;
    }

}

package gui.myeditorpane;

/*
 * Base64Tool.java
 */
public class Base64Tool{
    private static byte[] encMap, decMap;
    static{ byte[] base64Map ={
            (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F',
            (byte)'G', (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L',
            (byte)'M', (byte)'N', (byte)'O', (byte)'P', (byte)'Q', (byte)'R',
            (byte)'S', (byte)'T', (byte)'U', (byte)'V', (byte)'W', (byte)'X',
            (byte)'Y', (byte)'Z',
            (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f',
            (byte)'g', (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l',
            (byte)'m', (byte)'n', (byte)'o', (byte)'p', (byte)'q', (byte)'r',
            (byte)'s', (byte)'t', (byte)'u', (byte)'v', (byte)'w', (byte)'x',
            (byte)'y', (byte)'z',
            (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
            (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/' };
        encMap = base64Map;
        decMap = new byte[128];
        for (int i=0; i<encMap.length; i++)
        decMap[encMap[i]] = (byte) i;
    }

    /**
     * This class isn't meant to be instantiated.
     */
    private Base64Tool(){
    }

    /**
     * Encodes the given byte[] using the Base64Tool-encoding,
     * as specified in RFC-2045 (Section 6.8).
     * @param in the data to be encoded
     * @return '' the Base64Tool-encoded <var>in</var>
     * @exception IllegalArgumentException if NULL or empty array is passed
     */

    public static String base64Encode(byte[] in) {
        if ((in == null)||(in.length == 0))
            throw new IllegalArgumentException("Cannot encode NULL or empty byte array.");
        byte encodedBuf[] = new byte[((in.length+2)/3)*4];
        // 3-byte to 4-byte conversion
        int srcIndex, destIndex;
        for (srcIndex=0, destIndex=0; srcIndex < in.length-2;srcIndex += 3){
            encodedBuf[destIndex++] = encMap[(in[srcIndex] >>> 2) & 077];
            encodedBuf[destIndex++] = encMap[(in[srcIndex+1] >>> 4) & 017|(in[srcIndex] << 4) & 077];
            encodedBuf[destIndex++] = encMap[(in[srcIndex+2] >>> 6) & 003 |(in[srcIndex+1] << 2) & 077];
            encodedBuf[destIndex++] = encMap[in[srcIndex+2] & 077];
        }
        // Convert the last 1 or 2 bytes
        if (srcIndex < in.length){
            encodedBuf[destIndex++] = encMap[(in[srcIndex] >>> 2) & 077];
            if (srcIndex < in.length-1){
                encodedBuf[destIndex++] = encMap[(in[srcIndex+1] >>> 4) & 017 |(in[srcIndex] << 4) & 077];
                encodedBuf[destIndex++] = encMap[(in[srcIndex+1] << 2) & 077];
            }else{
                encodedBuf[destIndex++] = encMap[(in[srcIndex] << 4) & 077];
            }
        }
        // Add padding to the end of encoded data
        while (destIndex < encodedBuf.length){
            encodedBuf[destIndex] = (byte) '=';
            destIndex++;
        }
        String result = new String(encodedBuf);
        return result;
    }
    /**
     * Decodes the given Base64Tool-encoded data,
     * as specified in RFC-2045 (Section 6.8).
     * @param in the Base64Tool-encoded in.
     * @return '' the decoded <var>in</var>.
     * @exception IllegalArgumentException if NULL or empty data is passed
     */
    public static byte[] base64Decode(String in){
        if ((in == null) || (in.length() == 0))
            throw new IllegalArgumentException("Cannot decode NULL or empty string.");
        byte[] data = in.getBytes();
        // Skip padding from the end of encoded data
        int tail = data.length;
        while (data[tail-1] == '=')
        tail--;
        byte decodedBuf[] = new byte[tail - data.length/4];
        // ASCII-printable to 0-63 conversion
        for (int i = 0; i < data.length; i++)
            data[i] = decMap[data[i]];
        // 4-byte to 3-byte conversion
        int srcIndex, destIndex;
        for (srcIndex = 0, destIndex=0; destIndex < decodedBuf.length-2; srcIndex += 4, destIndex += 3){
            decodedBuf[destIndex] = (byte) (((data[srcIndex] << 2) & 255)|((data[srcIndex+1] >>> 4) & 003) );
            decodedBuf[destIndex+1] = (byte)(((data[srcIndex+1] << 4) & 255)|((data[srcIndex+2] >>> 2) & 017) );
            decodedBuf[destIndex+2] = (byte)(((data[srcIndex+2] << 6) & 255)|(data[srcIndex+3] & 077) );
        }
        // Handle last 1 or 2 bytes
        if (destIndex < decodedBuf.length)
            decodedBuf[destIndex] = (byte)(((data[srcIndex] << 2) & 255)|((data[srcIndex+1] >>> 4) & 003) );
        if (++destIndex < decodedBuf.length)
            decodedBuf[destIndex] = (byte)(((data[srcIndex+1] << 4) & 255)|((data[srcIndex+2] >>> 2) & 017) );
        return decodedBuf;
    }
}
package pkilib;

import java.io.InputStream;
import java.io.OutputStream;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * AESTool.java
 * Created on Jul 17, 2008 at 5:15:23 PM
 * @author Gurmeet Singh
 */
@SuppressWarnings({"UseSpecificCatch", "CallToThreadDumpStack"})
public class AESTool {
    private static int buffSize = 128;
    private SecretKey key;
    private static final String CIPHER_TYPE = "AES/CBC/PKCS5Padding";
    /**
     * generates fresh secret key and store in a private field 'key'
     * @return GenKeyResult status 0 ok
     *                      status 1 failed
     */
    public GenKeyResult generateSeceretKey(){
        GenKeyResult genKeyResult = new GenKeyResult();
        try{
            // Generate a SecretKey
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            key = keyGen.generateKey();
            genKeyResult.statusCode = 0;
            genKeyResult.description = "Key generated format = "+key.getFormat();
            return genKeyResult;
        }catch(Exception e){
            genKeyResult.statusCode = 1;
            genKeyResult.description = e.getMessage();
            return genKeyResult;
        }
    }
    /**
     * build secret key from encryptedKey and store in the private filed 'key'
     * @param encryptedKey
     * @param empNo
     * @return SetKeyResult - status 0 OK
     *                        status 1 Token dos'nt belongs to empno provided
     *                        status 2 Invalid encrypted data
     *                        status 3 other exception
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public SetKeyResult SetSecretKey(String encryptedKey, String empNo){
        SetKeyResult setKeyResult = new SetKeyResult();
        try{
            DecryptRSAResult decryptRSAResult = 
                                       RSATool.decryptRSA(encryptedKey, empNo);
            if(decryptRSAResult.statusCode!=0){
                setKeyResult.statusCode = decryptRSAResult.statusCode;
                setKeyResult.description = decryptRSAResult.description;
                return setKeyResult;
            }
            //decryptRSAResult.show();
            key = new SecretKeySpec(decryptRSAResult.keyBytes, "AES");
            
            setKeyResult.statusCode = 0;
            setKeyResult.description = "OK";
            return setKeyResult;
        }catch(Exception e){
            setKeyResult.statusCode = 3;
            setKeyResult.description = e.getMessage();
            MyUtils.showException("while SetSecretKey", e);
            return setKeyResult;
        }
    }
    /**
     * encrypt the secret key using public key of the employee and return 
     * base64 encoded encrypted key
     * @param empNo - employee for whom key is encrypted
     * @return GetKeyResult - status 0 OK
     *                        status 1 public key certificate not found
     *                        status 2 other exception
     */
    public GetKeyResult getKey( String empNo){
        GetKeyResult getKeyResult = new GetKeyResult();
        try{
            byte[] keyBytes = key.getEncoded();
            /*for(int i=0;i<keyBytes.length;i++){
                System.out.print(keyBytes[i]+" ");
            }
            System.out.println();*/
            EncryptRSAResult encryptRSAResult = 
                                           RSATool.encryptRSA(keyBytes, empNo);
            if(encryptRSAResult.statusCode!=0){
                getKeyResult.statusCode = encryptRSAResult.statusCode;
                getKeyResult.description = encryptRSAResult.description;
                return getKeyResult;
            }
            getKeyResult.statusCode = 0;
            getKeyResult.encryptedKey = encryptRSAResult.encryptedKey;
            getKeyResult.description = "OK";
            return getKeyResult;
        }catch(Exception e){
            getKeyResult.statusCode = 2;
            getKeyResult.description = e.getMessage();
            MyUtils.showException("while getKey", e);
            return getKeyResult;
        }
    }
    /**
     * encrypt data reading from inputstream and 
     * put the encrypted text into outputstream
     * @param in - InputStream
     * @param out - OutputStream
     * @return EncryptStreamResult - status 0 OK
     *                               status 1 Failed
     */
    public EncryptStreamResult encryptStream(InputStream in, OutputStream out){
        EncryptStreamResult encryptResult = new EncryptStreamResult();
        try{
            Cipher ecipher = Cipher.getInstance(CIPHER_TYPE);
            ecipher.init(Cipher.ENCRYPT_MODE, key, 
                                        new IvParameterSpec(key.getEncoded()));
            try (OutputStream cipherOut = new CipherOutputStream(out, ecipher)) {
                byte[] buf = new byte[buffSize];
                int byteread;
                while((byteread=in.read(buf))!=-1){
                    cipherOut.write(buf,0,byteread);
                }
                cipherOut.flush();
            }
            in.close();
            encryptResult.statusCode = 0;
            encryptResult.description = "OK";
            return encryptResult;
        }catch(Exception e){
            encryptResult.statusCode = 1;
            encryptResult.description = e.getMessage();
            MyUtils.showException("while encryptStream", e);
            return encryptResult;
        }
    }
    /**
     * decrypt data reading from inputstream and 
     * put the decrypted text into outputstream
     * @param in
     * @param out
     * @return DecryptStreamResult - status 0 OK
     *                               status 1 Failed
     */
    public DecryptStreamResult decryptStream(InputStream in, OutputStream out){
        DecryptStreamResult decryptResult = new DecryptStreamResult();
        try{
            Cipher deipher = Cipher.getInstance(CIPHER_TYPE);
            deipher.init(Cipher.DECRYPT_MODE, key, 
                                        new IvParameterSpec(key.getEncoded()));
            byte[] buf = new byte[buffSize];
            int byteread;
            try (InputStream cipherIn = new CipherInputStream(in, deipher)) {
                while((byteread = cipherIn.read(buf))!=-1){
                    out.write(buf,0,byteread);
                }
                out.flush();
                out.close();
            }
            decryptResult.statusCode = 0;
            decryptResult.description = "OK";
            return decryptResult;
        }catch(Exception e){
            decryptResult.statusCode = 1;
            decryptResult.description = e.getMessage();
            MyUtils.showException("while decryptStream", e);
            return decryptResult;
        }
    }
    /**
     * encrypt data in inBuff 
     * convert encrypted data into base64
     * put the base64 data into encryptedData field of EncryptDataResult
     * @param inBuff
     * @return EncryptDataResult   - status 0 OK
     */
    public EncryptDataResult encryptData(byte[] inBuff){
        EncryptDataResult encryptedDataResult = new EncryptDataResult();
        try{
            Cipher encipher = Cipher.getInstance(CIPHER_TYPE);
            encipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
            byte[] outBuff = encipher.doFinal(inBuff);
            String encryptedData = Base64.base64Encode(outBuff);
            encryptedDataResult.statusCode = 0;
            encryptedDataResult.encryptedData = encryptedData;
            encryptedDataResult.description = "OK";
            return encryptedDataResult;
        }catch(Exception e){
            encryptedDataResult.statusCode = 1;
            encryptedDataResult.description = e.getMessage();
            MyUtils.showException("while encryptData", e);
            return encryptedDataResult;
        }
    }
    /**
     * decrypt base64 encoded encryptedData 
     * put decrypted data into the data field of DecryptDataResult
     * @param encryptedData
     * @return DecryptDataResult   - status 0 OK
     *                               status 1 Failed
     */
    public DecryptDataResult decryptData(String encryptedData){
        DecryptDataResult decryptDataResult = new DecryptDataResult();
        try{
            byte[] inBuff = Base64.base64Decode(encryptedData);
            Cipher decipher = Cipher.getInstance(CIPHER_TYPE);
            decipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
            byte[] outBuff = decipher.doFinal(inBuff);
            decryptDataResult.statusCode = 0;
            decryptDataResult.data = outBuff;
            decryptDataResult.description = "OK";
            return decryptDataResult;
        }catch(Exception e){
            decryptDataResult.statusCode = 1;
            decryptDataResult.description = e.getMessage();
            MyUtils.showException("while decryptData", e);
            return decryptDataResult;
        }
    }
}
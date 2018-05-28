package tito1.example.com.timeperception;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

class Crypto {

    public static void fileProcessor(int cipherMode, String key, File inputFile, File outputFile){
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    public void main(String[] args) {
        String key = "This is a secret";
        File inputFile = new File( "TestFile.txt");
        File encryptedFile = new File("text.encrypted");
        File decryptedFile = new File("decrypted-text.txt");

//        try {
//            Crypto.fileProcessor(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile);
//            Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
//            System.out.println("Sucess");
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
    }

}


//public class ClaseDePrueba extends AppCompatActivity{
//    private EditText etTexto, etPassword;
//    private TextView tvTexto;
//    private Button btEncriptar, btDesEncriptar, btApiEncriptada;
//    private String textoSalida;
//
//
//    String apiKeyEncriptada ="0SPrEK0JntQ2qCm9cPEabw==";
//    String passwordEncriptacion = "gdsawr";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        etTexto = findViewById(R.id.mainActivityEtTexto);
//        etPassword = findViewById(R.id.mainActivityEtPassword);
//        tvTexto = findViewById(R.id.mainActivityTvTexto);
//
//        btApiEncriptada = findViewById(R.id.mainActivityBtApiEncriptada);
//        btApiEncriptada.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try{
//                    textoSalida = desencriptar(apiKeyEncriptada, passwordEncriptacion);
//                    tvTexto.setText(textoSalida);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        btEncriptar = findViewById(R.id.mainActivityBtEncriptar);
//        btEncriptar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try{
//                    textoSalida = encriptar(etTexto.getText().toString(), etPassword.getText().toString());
//                    tvTexto.setText(textoSalida);
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        btDesEncriptar = findViewById(R.id.mainActivityBtDesencritar);
//        btDesEncriptar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try{
//                    textoSalida = desencriptar(textoSalida, etPassword.getText().toString());
//                    tvTexto.setText(textoSalida);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private String desencriptar(String datos, String password) throws Exception{
//        SecretKeySpec secretKey = generateKey(password);
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] datosDescoficados = Base64.decode(datos, Base64.DEFAULT);
//        byte[] datosDesencriptadosByte = cipher.doFinal(datosDescoficados);
//        String datosDesencriptadosString = new String(datosDesencriptadosByte);
//        return datosDesencriptadosString;
//    }
//
//    private String encriptar(String datos, String password) throws Exception{
//        ///////////////////////
//
//        File file = new File(getApplicationContext().getExternalFilesDir(null), "TestFile.txt");
//
//        byte[] b = new byte[(int) file.length()];
//        try {
//            FileInputStream fileInputStream = new FileInputStream(file);
//            fileInputStream.read(b);
//            for (int i = 0; i < b.length; i++) {
//                System.out.print((char)b[i]);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("File Not Found.");
//            e.printStackTrace();
//        }
//        catch (IOException e1) {
//            System.out.println("Error Reading The File.");
//            e1.printStackTrace();
//        }
//
//        ///////////////////////
//        SecretKeySpec secretKey = generateKey(password);
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
//        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
//        return datosEncriptadosString;
//    }
//
//    private SecretKeySpec generateKey(String password) throws Exception{
//        MessageDigest sha = MessageDigest.getInstance("SHA-256");
//        byte[] key = password.getBytes("UTF-8");
//        key = sha.digest(key);
//        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
//        return secretKey;
//    }
//}

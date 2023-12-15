package bloodmanager;

public class Password {
    public static String encrypt(String password) {
        StringBuilder encrypt = new StringBuilder();
        int len = password.length();
        for(int x = 0; x < len; x++){
            char c = (char)(password.charAt(x) + 7);
            encrypt.append(c);
        }
        return encrypt.toString();
    }

    public static String decrypt(String password) {
        StringBuilder decrypt = new StringBuilder();
        int len = password.length();
        for(int x = 0; x < len; x++){
            char c = (char)(password.charAt(x) - 7);
            decrypt.append(c);
        }
        return decrypt.toString();
    }
}

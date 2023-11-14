## Ticketing Android App Challenge

### Challenge 1: Reverse Engineering an Android App

### Challenge Name: XORacle Quest: Unveiling the Hidden Sauce

In this challenge, the task is to reverse engineer the source code of the Android application to find the hidden cryptographic key used as the admin token. Once you locate it, present the admin token in clear text as the flag.

#### Challenge Walkthrough

1. Use an APK decompiler to obtain the source code of the Android application. Good options include [JADX](https://github.com/skylot/jadx?ref=hackernoon.com)
2. Search through the source code to find any references to an admin token. Look for variables that might represent the token.
3. Once you've identified the `ADMIN_TOKEN` variable, identify the function used to generated the token `getToken()`, and any other resource that the function uses. 
4. Identify the string resource `sauce` used within the `getToken()` function. The string resource `sauce` has some escaped special characters ```\@0a1X4k`~{n>{=!`Lg!`\"```, by removing them, you get ```@0a1X4k`~{n>{=!`Lg!`"``` 
5. The `getToken()` function runs some operations on the `sauce` string. In your own IDE, run the same operations on the `sauce` string to obtain the admin token. You might need to re-escape the special characters in the `sauce` string, depending on how you run the code.
```java
import java.util.Base64;
public class Main {  
    public static String getToken() {
        String seed = "@0a1X4k`~{n>{=!`Lg!`\"";
        byte[] obfuscatedBytes = seed.getBytes();
        for (int round = 2; round >= 0; round--) {
            for (int i = obfuscatedBytes.length - 1; i >= 0; i--) {
                obfuscatedBytes[i] ^= (byte) (i + round);
            }
        }
        return new String(obfuscatedBytes) ;
    }
    public static void main(String[] args){
     System.out.println(getToken());
    }
}
```
6. Submit the contents of the `ADMIN_TOKEN` variable as the flag.

The answer to this challenge is `C0d3_0bfusc4t10n_w4r5`
package com.driverapp.android.auth;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.driverapp.android.core.utils.UserUtil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jesus Christ. Amen.
 */
public class AuthUtil {
    private static String packageName = "com.driverapp.android";

    public static void printSignature(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static String getCertificateFingerprintHash(Context ctx) {
        try {
            if (ctx == null || ctx.getPackageManager() == null)
                return null;
            PackageInfo info = ctx.getPackageManager().getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES);
            assert info.signatures != null;
            String[] result = new String[info.signatures.length];
            int i = 0;
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
//                result[i++] = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                result[i++] = toHex(md.digest());
            }
            String hash = result[0];
            Log.d("KeyHash:", hash);
            return hash;
        } catch (Exception e) {
            return null;
        }
    }
    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static void logout() {
        UserUtil.logout();
    }
}

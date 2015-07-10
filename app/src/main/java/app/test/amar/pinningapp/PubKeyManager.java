package app.test.amar.pinningapp;

import android.util.Log;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by amar on 10/07/15. (modified on top of https://www.owasp.org/index.php/Certificate_and_Public_Key_Pinning#Android)
 */

public final class PubKeyManager implements X509TrustManager {

    private static final String TAG = "PubKeyManager";

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        RSAPublicKey serverPubKey;
        String serverEncodedKey;

        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }

        if (!(chain.length > 0)) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
        } else {
            serverPubKey = (RSAPublicKey) chain[0].getPublicKey();
            serverEncodedKey = new BigInteger(1 /* positive */, serverPubKey.getEncoded()).toString(16);
        }

        if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
            throw new CertificateException("checkServerTrusted: AuthType is not RSA");
        }

        // Perform customary SSL/TLS checks
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
            }
        } catch (Exception e) {
            throw new CertificateException(e);
        }

        // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
        // with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0x00 to drop.

        String keyEncoded = Constants.PUBKEY_GTIHUB_ENCODED;
        Log.d(TAG, "AUTH TYPE: " + authType + "\n*********** RECEIVED ENCODED SERVER PUBLIC KEY ***********" + keyEncoded);
        // Pin it!
        final boolean expected = keyEncoded.equalsIgnoreCase(serverEncodedKey);
        if (!expected) {
            throw new CertificateException("checkServerTrusted():\n*********** Expected public key ***********"
                    + keyEncoded + "\n*********** (failed) RECEIVED ENCODED SERVER PUBLIC KEY ***********\n" + serverEncodedKey);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        //return new X509Certificate[0];
        return null;
    }
}

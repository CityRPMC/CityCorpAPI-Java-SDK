package org.cityrp.citycorpapi.http;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TLSSocketFactory extends SSLSocketFactory {

    private final SSLSocketFactory mInternalSSLSocketFactory;

    public TLSSocketFactory() throws SSLException {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            this.mInternalSSLSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            throw new SSLException(ex.getMessage());
        }
    }

    public TLSSocketFactory(InputStream certificateStream) throws SSLException {
        try (certificateStream) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            for (Certificate cert : cf.generateCertificates(certificateStream)) {
                if (cert instanceof X509Certificate) {
                    String subject = ((X509Certificate) cert).getSubjectDN().getName();
                    keyStore.setCertificateEntry(subject, cert);
                }
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[]) null, tmf.getTrustManagers(), (SecureRandom) null);
            this.mInternalSSLSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new SSLException(e.getMessage());
        }
    }

    public String[] getDefaultCipherSuites() {
        return this.mInternalSSLSocketFactory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.mInternalSSLSocketFactory.getSupportedCipherSuites();
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        return this.enableTLSOnSocket(this.mInternalSSLSocketFactory.createSocket(s, host, port, autoClose));
    }

    public Socket createSocket(String host, int port) throws IOException {
        return this.enableTLSOnSocket(this.mInternalSSLSocketFactory.createSocket(host, port));
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return this.enableTLSOnSocket(this.mInternalSSLSocketFactory.createSocket(host, port, localHost, localPort));
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        return this.enableTLSOnSocket(this.mInternalSSLSocketFactory.createSocket(host, port));
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return this.enableTLSOnSocket(this.mInternalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
    }

    private Socket enableTLSOnSocket(Socket socket) {
        if (socket instanceof SSLSocket) {
            List<String> supportedProtocols = new ArrayList<>(Arrays.asList(((SSLSocket) socket).getSupportedProtocols()));
            supportedProtocols.retainAll(Arrays.asList("TLSv1.2", "TLSv1.1", "TLSv1"));
            ((SSLSocket) socket).setEnabledProtocols(supportedProtocols.toArray(new String[0]));
        }
        return socket;
    }
}
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SecureConnection {
   
    private String host;
    private int port;
    private String username;
    private String password;
    private Connection connection;

    public SecureConnection(final String host, final int port, final String username, final String password) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableEntryException, KeyManagementException, IOException, TimeoutException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        char[] keyPassphrase = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance("PKCS12");
        // ks.load

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "password".toCharArray());

        char[] trustPassphrase = "password".toCharArray();
        KeyStore tks = KeyStore.getInstance("JKS");
        // tks.load

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);

        SSLContext c = SSLContext.getInstance("TLSv1.2");
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(this.host);
        factory.setPort(this.port);
        factory.setUsername(this.username);
        factory.setPassword(this.password);
        factory.useSslProtocol(c);

        this.connection = factory.newConnection();

    }
}

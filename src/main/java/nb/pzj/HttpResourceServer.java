package nb.pzj;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;

@Data
@AllArgsConstructor
public class HttpResourceServer {
    private String uri, path;
    private Integer port;
    private String name;

    public void startListen() {
        try {
            HttpServerProvider provider = HttpServerProvider.provider();
            HttpServer server = provider.createHttpServer(new InetSocketAddress(port), 100);
            server.createContext(uri, new HttpGetHandler(uri, path));
            server.start();
            System.out.println("HttpHandler " + name + " start: listen [ port:" + port + ", uri:" + uri + " ] remote to [ " + path + " ]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

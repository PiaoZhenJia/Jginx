package nb.pzj;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;

@Data
@AllArgsConstructor
public class HttpResourceServer {
    private String uri, path;
    private Integer port;
    private String name;

    public void startListen() {
        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer server = null;
        try {
            server = provider.createHttpServer(new InetSocketAddress(port), 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext(uri, new HttpGetHandler(uri,path));
        server.start();
        System.out.println("HttpHandler test_B start: listen [ port:"+port+", uri:"+uri+" ] remote to ["+path+"]");
    }
}

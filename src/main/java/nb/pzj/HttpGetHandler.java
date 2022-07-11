package nb.pzj;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpGetHandler extends Thread implements HttpHandler {

    private String uri;
    private String path;

    @Override
    public void handle(HttpExchange he) {
        new HttpGetThread(uri,path,he).start();
    }


}

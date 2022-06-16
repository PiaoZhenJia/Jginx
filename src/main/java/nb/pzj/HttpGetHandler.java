package nb.pzj;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import nb.pzj.util.FileUtil;

import java.io.*;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class HttpGetHandler implements HttpHandler {

    private String uri;
    private String path;

    @Override
    public void handle(HttpExchange he) throws IOException {
        String requestMethod = he.getRequestMethod();
        Headers responseHeaders = he.getResponseHeaders();
        OutputStream responseBody = he.getResponseBody();

        if (requestMethod.equalsIgnoreCase("GET")) {
            File file = new File(path + he.getRequestURI().getPath().replace(uri, ""));
            if (!new FileUtil().fileIsExist(file.getAbsolutePath())) {
                responseHeaders.set("Content-Type", "text/html;charset=UTF-8");
                responseHeaders.set("Content-Length", "20");
                he.sendResponseHeaders(200, 0);
                responseBody.write("\r\n404 File Not Found ~".getBytes());
                //throw new RuntimeException();
            } else {
                responseHeaders.set("Content-Type", "applicatoin/octet-stream");
                responseHeaders.set("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                responseHeaders.set("Content-Length", String.valueOf(file.length()));
                he.sendResponseHeaders(200, 0);

                try (InputStream bis = new BufferedInputStream(new FileInputStream(file));
                     OutputStream ops = new BufferedOutputStream(responseBody)) {
                    byte[] body = new byte[1024 * 4];//缓存为4kb
                    int i;
                    while ((i = bis.read(body)) != -1) {
                        ops.write(body, 0, i);
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            responseBody.close();
        }
    }
}

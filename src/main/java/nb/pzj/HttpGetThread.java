package nb.pzj;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;
import nb.pzj.util.FileUtil;

import java.io.*;
import java.net.URLEncoder;

@AllArgsConstructor
public class HttpGetThread extends Thread {

    private String uri;
    private String path;
    private HttpExchange he;

    @Override
    public void run() {
        OutputStream responseBody = he.getResponseBody();
        String requestMethod = he.getRequestMethod();
        Headers responseHeaders = he.getResponseHeaders();
        if (requestMethod.equalsIgnoreCase("GET")) {
            File file = new File(path + he.getRequestURI().getPath().replace(uri, ""));
            this.setName(this.getName() + " download " + file.getName());
            System.out.println(this.getName() + " 开始下载");
            if (!new FileUtil().fileIsExist(file.getAbsolutePath())) {
                responseHeaders.set("Content-Type", "text/html;charset=UTF-8");
                responseHeaders.set("Content-Length", "20");
                try {
                    he.sendResponseHeaders(200, 0);
                    responseBody.write("\r\n404 File Not Found ~".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                responseHeaders.set("Content-Type", "applicatoin/octet-stream");
                try {
                    responseHeaders.set("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                responseHeaders.set("Content-Length", String.valueOf(file.length()));
                try (InputStream bis = new BufferedInputStream(new FileInputStream(file));
                     OutputStream ops = new BufferedOutputStream(responseBody)) {
                    he.sendResponseHeaders(200, 0);
                    byte[] body = new byte[1024 * 4];//缓存为4kb
                    int i;
                    while ((i = bis.read(body)) != -1) {
                        ops.write(body, 0, i);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println(this.getName() + " IO连接中断");
                }
            }
        }
        try {
            responseBody.close();
            System.out.println(this.getName() + " 下载完成");
        } catch (IOException e) {
        } finally {
            System.out.println(this.getName() + " 异常停止");
        }
    }
}

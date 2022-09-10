package nb.pzj;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

public class TcpRemoteThread extends Thread {
    private final Socket fromSocket;
    private final Socket toSocket;

    public TcpRemoteThread(Socket fromSocket, Socket toSocket, String name) {
        super(name);
        this.fromSocket = fromSocket;
        this.toSocket = toSocket;
    }

    @Override
    public void run() {

        System.out.println("Thread start : " + getName());

        try (InputStream in = fromSocket.getInputStream();
             OutputStream out = toSocket.getOutputStream()) {
            long activeCheck = System.currentTimeMillis();
            while (System.currentTimeMillis() - activeCheck < 1000 * 60) {
                if (fromSocket.isClosed() || toSocket.isClosed()) {
                    System.out.println("Thread stop : " + getName() + " cause: socket closed");
                    return;
                }

                byte[] data = new byte[4096];
                int readFlag = in.read(data);
                if (readFlag <= 0) {
                    Thread.sleep(100);
                    continue;
                }
                printIO(data, readFlag);
                out.write(data, 0, readFlag);
                out.flush();
            }

            System.out.println("Thread stop : " + getName() + " cause: keep alive timeout");
        } catch (SocketException e) {
            System.out.println("Thread stop : " + getName() + " cause: socket closed");
            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Thread stop : " + getName() + " cause: socket exception");
            e.printStackTrace();
        } finally {
            try {
                if (null != fromSocket)
                    fromSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (null != toSocket)
                    toSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void printIO(byte[] data, int readFlag) {
        byte[] needPrint = new byte[readFlag];
        System.arraycopy(data, 0, needPrint, 0, readFlag);
        System.out.println("\n*************** " + new Date() + " Thread " + getName() + " ***************\n"
                        + new String(needPrint));
    }
}


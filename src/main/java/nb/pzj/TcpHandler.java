package nb.pzj;

import com.alibaba.fastjson.JSONArray;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class TcpHandler extends Thread {

    Random random = new Random();
    final int localPort;
    final String[] remoteLocationList;

    public TcpHandler(int localPort, String[] remoteLocationList, String name) {
        super(name);
        this.localPort = localPort;
        this.remoteLocationList = remoteLocationList;
    }

    @Override
    public void run() {
        System.out.println("TcpHandler " + getName() + " start: listen [ " + localPort + " ] remote to " + Arrays.toString(remoteLocationList));
        try (ServerSocket serverSocket = new ServerSocket(localPort)) {
            Socket clientSocket = null;
            Socket remoteServerSocket = null;
            while (true) {
                clientSocket = serverSocket.accept();
                String server = findOneServer();
                remoteServerSocket = new Socket(server.split(":")[0], Integer.valueOf(server.split(":")[1]));
                new TcpRemote(clientSocket, remoteServerSocket, getName() + " @ " + clientSocket.getInetAddress().getHostAddress() + " --> " + server).start();
                new TcpRemote(remoteServerSocket, clientSocket, getName() + " @ " + clientSocket.getInetAddress().getHostAddress() + " <-- " + server).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String findOneServer() {
        return remoteLocationList[random.nextInt() % remoteLocationList.length];
    }
}

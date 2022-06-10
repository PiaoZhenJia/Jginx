package nb.pzj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpHandler extends Thread {

    final int localPort;
    final String remoteAddress;
    final int remotePort;

    public TcpHandler(int localPort, String remoteAddress, int remotePort) {
        this.localPort = localPort;
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

    @Override
    public void run() {
        System.out.println("TcpHandler start: listen [ " + localPort + " ] remote to [ " + remoteAddress + ":" + remotePort + " ]");
        try (ServerSocket serverSocket = new ServerSocket(localPort)) {
            Socket clientSocket = null;
            Socket remoteServerSocket = null;
            while (true) {
                clientSocket = serverSocket.accept();
                remoteServerSocket = new Socket(remoteAddress, remotePort);
                new TcpRemote(clientSocket, remoteServerSocket, clientSocket.getInetAddress().getHostAddress() + "-ask").start();
                new TcpRemote(remoteServerSocket, clientSocket, clientSocket.getInetAddress().getHostAddress() + "-ans").start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

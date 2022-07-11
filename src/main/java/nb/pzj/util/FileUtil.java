package nb.pzj.util;

import java.io.*;

/**
 * @author 朴朴朴 https://github.com/PiaoZhenJia
 */
public class FileUtil {

    /**
     * 如果文件存在会返回true
     */
    public boolean fileIsExist(String needFile) {
        File file = new File(needFile);
        return file.exists() && file.isFile();
    }

    /**
     * 释放配置文件
     * @throws IOException
     * 配置文件创建或者释放失败会触发这个异常
     * 标志启动参数没有配置文件路径且自释放配置文件失败
     * 程序在这种情况下不能启动
     */
    public void outputFileAsByte(byte[] need, String needFile) throws IOException {
        File file = new File(needFile);
        //判断文件是否存在
        if (!fileIsExist(needFile)) {
            //如果文件不存在则创建文件 创建不成功会抛出IO异常
            new File(needFile).createNewFile();
        }
        DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
        out.write(need);
        System.out.println("配置文件已释放");
    }

    /**
     * 根据路径获取文件流
     */
    public InputStream getInputStreamFromFileInResources(String fileUrl) {
        return this.getClass().getClassLoader().getResourceAsStream(fileUrl);
    }

    /**
     * 根据文件获取文件二进制
     */
    public byte[] getByteFromFile(File file) {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package nb.pzj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import nb.pzj.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {//允许在命令行定义配置文件位置

        JSONObject configMap;

        FileUtil fileUtil = new FileUtil();
        //判断命令行是否携带配置文件位置
        if (null != args && args.length > 0 && null != args[0] && !"".equals(args[0])) {
            //检查参数中携带文件的合法性
            if (!fileUtil.fileIsExist(args[0])) {
                throw new RuntimeException("参数中携带的配置文件路径不存在或读取失败");
            } else {
                //加载解析这个配置文件
                configMap = JSONObject.parseObject(new String(fileUtil.getByteFromFile(new File(args[0]))));
            }
        } else {
            //命令行没有配置文件的参数
            if (fileUtil.fileIsExist("./Jginx.conf")) {
                //读取默认路径配置文件
                configMap = JSONObject.parseObject(new String(fileUtil.getByteFromFile(new File("./Jginx.conf"))));
            } else {
                //释放默认配置文件
                configMap = new JSONObject();
                ConfigEntity configEntity = new ConfigEntity();
                configEntity.setListenPort(80);
                JSONArray hosts = new JSONArray();
                String[] localhost = {"localhost:8080"};
                configEntity.setRemoteLocation(localhost);
                configMap.put("un_name", configEntity);
                fileUtil.outputFileAsByte(JSONObject.toJSONString(configMap,true).getBytes(), "./Jginx.conf");
                System.out.println("已释放默认配置文件 可查看修改后重新启动");
                return;
            }
        }
        System.out.println("启动监听线程...");
        configMap.forEach((k, v) -> {
            ConfigEntity configEntity = JSONObject.toJavaObject((JSONObject) v, ConfigEntity.class);
            new TcpHandler(configEntity.getListenPort(), configEntity.getRemoteLocation(), k).start();
        });

    }
}

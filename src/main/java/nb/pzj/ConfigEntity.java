package nb.pzj;

import lombok.Data;


@Data
public class ConfigEntity {

    private Integer listenPort;
    private String[] remoteLocation;
    private String mode;

    private String path;
    private String uri;

}

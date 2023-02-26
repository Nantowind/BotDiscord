package org.walycorp.botdiscord.credentials;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.*;

public class EnvReader {
    // Se carga el archivo .env en el objeto Dotenv.
    private static final Dotenv dotenv = Dotenv.load();

    // Mapa que almacena los ID de los administradores.
    public static final Map<String, String> ADMIN_IDS;

    // Mapa que almacena los ID de los canales.
    public static final Map<String, String> CHANNEL_IDS;

    // Mapa que almacena otra información sensible obtenida del archivo .env.
    public static final Map<String, String> OTHER_INFO;

    // Mapa que almacena los ID de los roles.
    public static final Map<String, String> ROLE_IDS;

    // Lista que almacena los ID de todos los canales.
    public static final List<String> ALL_CHANNEL_IDS;

    // Lista que almacena los ID de todos los administradores.
    public static final List<String> ALL_ADMIN_IDS;

    // Bloque de inicialización estática que se ejecuta al cargar la clase.
    static {
        // Mapa que almacena los ID de los administradores.
        Map<String, String> ids = new HashMap<>();
        ids.put("IDWALTER", dotenv.get("IDWALTER"));
        ids.put("IDMARCOS", dotenv.get("IDMARCOS"));
        ids.put("IDBOT", dotenv.get("IDBOT"));

        // Agregar aquí los demás IDs de administradores
        ADMIN_IDS = Collections.unmodifiableMap(ids);

        // Lista que almacena los ID de todos los administradores.
        List<String> allAdminIds = new ArrayList<>(ADMIN_IDS.values());
        ALL_ADMIN_IDS = Collections.unmodifiableList(allAdminIds);

        // Mapa que almacena los ID de los canales.
        Map<String, String> channelIds = new HashMap<>();
        channelIds.put("ADMINCHANNEL", dotenv.get("ADMINCHANNEL"));
        channelIds.put("LOBBYCHANNEL", dotenv.get("LOBBYCHANNEL"));
        channelIds.put("TESTINGCHANNEL", dotenv.get("TESTINGCHANNEL"));
        channelIds.put("DEVOPSCHANNEL", dotenv.get("DEVOPSCHANNEL"));
        // Agregar aquí los demás ID de canales
        CHANNEL_IDS = Collections.unmodifiableMap(channelIds);

        // Lista que almacena los ID de todos los canales.
        List<String> allChannelIds = new ArrayList<>(CHANNEL_IDS.values());
        ALL_CHANNEL_IDS = Collections.unmodifiableList(allChannelIds);

        // Mapa que almacena otra información sensible obtenida del archivo .env.
        Map<String, String> otherInfo = new HashMap<>();
        otherInfo.put("TOKEN", dotenv.get("TOKEN"));
        otherInfo.put("USERDB", dotenv.get("USERDB"));
        otherInfo.put("PASSDB", dotenv.get("PASSDB"));
        otherInfo.put("INFOLOBBY", dotenv.get("INFOLOBBY"));

        // Agregar aquí otra información que necesite
        OTHER_INFO = Collections.unmodifiableMap(otherInfo);

        // Mapa que almacena los ID de los roles.
        Map<String, String> roleIds = new HashMap<>();
        roleIds.put("IDSILENCE", dotenv.get("IDSILENCE"));
        roleIds.put("IDEXPERTO", dotenv.get("IDEXPERTO"));
        roleIds.put("IDNOVATO", dotenv.get("IDNOVATO"));
        roleIds.put("IDCOLABORADOR", dotenv.get("IDCOLABORADOR"));
        roleIds.put("IDMIENBRODESTACADO", dotenv.get("IDMIENBRODESTACADO"));
        // Agregar aquí los demás IDs de roles
        ROLE_IDS = Collections.unmodifiableMap(roleIds);

    }
    //Carga las variables de entorno desde el archivo .env en el directorio raíz del proyecto.
    public static void loadEnv() {
        Dotenv.configure().load();
    }
}

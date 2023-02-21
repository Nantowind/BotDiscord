package org.walycorp.botdiscord;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class Bot {

    // La clase Dotenv se utiliza para leer variables de entorno desde un archivo .env
    private final Dotenv config;
    // ShardManager es la clase principal que representa al bot.
    // Contiene todos los eventos y operaciones de Discord API.
    private final ShardManager shardManager;

    /**
     * Constructor que inicializa el bot.
     * @throws LoginException si no se puede iniciar sesión con el token proporcionado.
     */
    public Bot() throws LoginException {
        // Cargar las variables de entorno desde el archivo .env
        config = Dotenv.configure().load();
        String TOKEN = config.get("TOKEN");

        // Crea un DefaultShardManagerBuilder con el token proporcionado
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN);
        // Establece el estado del bot en línea
        builder.setStatus(OnlineStatus.ONLINE);
        // Establece la actividad del bot como "viendo WallyTV"
        builder.setActivity(Activity.watching("WallyTV"));
        // Construye el ShardManager a partir del builder
        shardManager = builder.build();
    }

    /**
     * Método para obtener la instancia del Dotenv.
     * @return La instancia del Dotenv.
     */
    public Dotenv getConfig() {
        return config;
    }

    /**
     * Método para obtener la instancia del ShardManager.
     * @return La instancia del ShardManager.
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    // Método main que inicia el bot
    public static void main(String[] args) throws LoginException {
        try {
            Bot bot = new Bot();
        } catch (LoginException e) {
            // Si se produce una excepción de inicio de sesión, muestra un mensaje de error.
            System.out.println("ERROR: Provided bot token is invalid");
        }
    }
}

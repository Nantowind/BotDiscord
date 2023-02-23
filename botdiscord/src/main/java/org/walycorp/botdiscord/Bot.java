package org.walycorp.botdiscord;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.walycorp.botdiscord.commands.CommandManager;
import org.walycorp.botdiscord.listeners.EventListener;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.List;

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

        config = Dotenv.configure().load();
        String TOKEN = config.get("TOKEN");


        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(TOKEN);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_PRESENCES);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS,CacheFlag.ACTIVITY);
        // Establece el estado del bot en línea
        builder.setStatus(OnlineStatus.ONLINE);
        // Establece la actividad del bot como "viendo WallyTV"
        builder.setActivity(Activity.watching("WallyTV"));
        // Construye el ShardManager a partir del builder
        shardManager = builder.build();
        // registrar listeners
        shardManager.addEventListener(new EventListener(), new CommandManager());
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

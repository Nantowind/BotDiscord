/**
 * La clase EventListener extiende de ListenerAdapter y se utiliza para manejar eventos en Discord.
 */
package org.walycorp.botdiscord.listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.walycorp.botdiscord.credentials.EnvReader;
import org.walycorp.botdiscord.tools.MessageCleaner;
import org.walycorp.botdiscord.tools.WordFilter;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * La clase EventListener extiende de ListenerAdapter y se utiliza para manejar eventos en Discord.
 */
public class EventListener extends ListenerAdapter {

    /**
     * El objeto scheduler es un ScheduledExecutorService que se utiliza para programar y ejecutar tareas periódicas.
     * En este caso, se utiliza para programar la limpieza periódica de mensajes en los canales especificados.
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    /**
     * Este método se ejecuta cuando el bot está listo para recibir eventos.
     * Recibe el evento ReadyEvent y utiliza el objeto shardManager para programar la limpieza de mensajes en los canales especificados.
     * @param event el evento ReadyEvent que indica que el bot está listo
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        // Se obtiene el objeto shardManager que representa al cliente de Discord.
        ShardManager shardManager = event.getJDA().getShardManager();
        // Se itera sobre los identificadores de canal especificados.
        for (String channelId : EnvReader.ALL_CHANNEL_IDS) {
            // Se crea un objeto MessageCleaner para cada canal y se programa su ejecución periódica.
            MessageCleaner messageCleaner = new MessageCleaner(shardManager, channelId);
            scheduler.scheduleAtFixedRate(messageCleaner, 0, 30, TimeUnit.MINUTES);
        }
    }

    /**
     * Este método se ejecuta cuando se agrega una reacción a un mensaje.
     * Recibe el evento MessageReactionAddEvent y utiliza sus propiedades para crear un mensaje que informa que un usuario ha reaccionado a un mensaje en un canal.
     * @param event el evento MessageReactionAddEvent que indica que se ha agregado una reacción a un mensaje
     */
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        // Se obtienen las propiedades relevantes del evento.
        User user = event.getUser();
        String jumpLink = event.getJumpUrl();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channel = event.getChannel().getAsMention();
        // Se crea un mensaje que informa que un usuario ha reaccionado a un mensaje en un canal.
        String message = user.getAsTag() + " reaccionó a un [mensaje](" + jumpLink + ") con " + emoji + " en el canal "
                + channel + "!";
        // Se envía el mensaje al canal predeterminado del servidor.
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();
    }

    /**

     Este método se llama cuando se recibe un mensaje en un canal de texto.
     Si el mensaje contiene alguna palabra grosera, se silencia al usuario que lo envió y se borra el mensaje.
     @param event El evento que contiene el mensaje recibido.
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // Se verifica si el mensaje fue enviado por el bot.
        if (event.getAuthor().getId().equals(EnvReader.ADMIN_IDS.get("IDBOT"))) {
            return;
        }

        String message = event.getMessage().getContentRaw();
        // Se comprueba si el mensaje contiene alguna palabra grosera utilizando un filtro de palabras.
        if (WordFilter.containsWord(message.toLowerCase())) {
            muteUser(event.getMember(), event.getGuild());
            deleteMessage(event);
        }
    }


    /**

     Este método se llama cuando se actualiza un mensaje en un canal de texto.
     Si el mensaje editado contiene alguna palabra grosera, se silencia al usuario que lo envió y se borra el mensaje editado.
     @param event El evento que contiene el mensaje editado.
     */
    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        // Se verifica si el mensaje fue enviado por el bot.
        if (event.getAuthor().getId().equals(EnvReader.ADMIN_IDS.get("IDBOT"))) {
            return;
        }

        String newMessage = event.getMessage().getContentRaw();
        // Se comprueba si el mensaje editado contiene alguna palabra grosera utilizando un filtro de palabras.
        if (WordFilter.containsWord(newMessage.toLowerCase())) {
            muteUser(event.getMember(), event.getGuild());
            event.getMessage().delete().queue();
        }
    }



    /**

     Este método silencia a un usuario en el servidor y le envía un mensaje privado.
     @param member El miembro que será silenciado.
     @param guild El servidor en el que se realizará la acción.
     */
    private void muteUser(Member member, net.dv8tion.jda.api.entities.Guild guild) {
        Role mutedRole = guild.getRoleById(EnvReader.ROLE_IDS.get("IDSILENCE"));
        if (mutedRole != null) {
            guild.addRoleToMember(member, mutedRole).queue();
            sendPrivateMessage(member.getUser());
            removeMutedRole(member, mutedRole, guild);
        }
    }

    /**

     Este método envía un mensaje privado al usuario silenciado.
     @param user El usuario que recibirá el mensaje privado.
     */
    private void sendPrivateMessage(User user) {
        user.openPrivateChannel().queue(channel -> {
            channel.sendMessage("¡Hey! No uses palabras groseras. Has sido silenciado.").queue();
        });
    }

    /**

     Este método elimina el rol de silenciado del usuario después de un minuto.
     @param member El miembro al que se le eliminará el rol.
     @param mutedRole El rol de silenciado que será eliminado.
     @param guild El servidor en el que se realizará la acción.
     */
    private void removeMutedRole(Member member, Role mutedRole, net.dv8tion.jda.api.entities.Guild guild) {
        guild.removeRoleFromMember(member, mutedRole).queueAfter(1, TimeUnit.MINUTES);
    }

    /**

     Este método borra el mensaje del canal de texto.
     @param event El evento que contiene el mensaje que será eliminado.
     */
    private void deleteMessage(MessageReceivedEvent event) {
        event.getMessage().delete().queue();
    }
}

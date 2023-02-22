/**

 La clase MessageCleaner es una tarea que se ejecuta periódicamente y elimina los mensajes del canal que contienen palabras prohibidas.
 */
package org.walycorp.botdiscord.tools;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.ArrayList;
import java.util.List;

public class MessageCleaner implements Runnable{
    private final TextChannel channel;

    /**
     * Crea un objeto MessageCleaner para limpiar los mensajes de un canal concreto.
     * @param shardManager el administrador de fragmentos utilizado para obtener el canal.
     * @param channelId el ID del canal que se va a limpiar.
     */
    public MessageCleaner(ShardManager shardManager, String channelId) {
        this.channel = shardManager.getTextChannelById(channelId);
    }

    /**
     * Ejecuta la tarea de limpiar los mensajes.
     */
    @Override
    public void run() {
        if (channel != null) {
            List<Message> mensajesConInsultos = buscarMensajesConInsultos(channel);
            eliminarMensajes(mensajesConInsultos);
        }
    }

    /**
     * Busca todos los mensajes en un canal que contienen palabras prohibidas.
     * @param channel el canal en el que se van a buscar los mensajes.
     * @return una lista de mensajes que contienen palabras prohibidas.
     */
    private List<Message> buscarMensajesConInsultos(TextChannel channel) {
        List<Message> mensajesConInsultos = new ArrayList<>();
        for (Message message : channel.getIterableHistory()) {
            String content = message.getContentRaw();
            if (WordFilter.containsWord(content.toLowerCase())) {
                mensajesConInsultos.add(message);
            }
        }
        return mensajesConInsultos;
    }

    /**
     * Elimina una lista de mensajes del canal.
     * @param mensajes la lista de mensajes que se van a eliminar.
     */
    private void eliminarMensajes(List<Message> mensajes) {
        for (Message message : mensajes) {
            message.delete().queue();
        }
    }
}

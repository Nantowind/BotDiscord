package org.walycorp.botdiscord.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.walycorp.botdiscord.tools.WordFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class EventListener extends ListenerAdapter {


    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String jumpLink = event.getJumpUrl();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channel = event.getChannel().getAsMention();

        String message = user.getAsTag() + " reacted to a [message]("+jumpLink+") with " + emoji + " in the " + channel + " channel!";
        event.getGuild().getDefaultChannel().asStandardGuildMessageChannel().sendMessage(message).queue();;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (WordFilter.containsWord(message.toLowerCase())) {
            Member member = event.getMember();
            if (member != null) {
                Role mutedRole = event.getGuild().getRoleById("1077787302404825169");
                if (mutedRole != null) {
                    event.getGuild().addRoleToMember(member, mutedRole).queue();
                    //Enviar mensaje al usuario
                    User user = event.getAuthor();
                    user.openPrivateChannel().queue(channel -> {
                        channel.sendMessage("¡Hey! No uses palabras groseras. Has sido silenciado.").queue();
                    });
                    //Quitar el rol después de 1 minuto
                    event.getGuild().removeRoleFromMember(member, mutedRole).queueAfter(1, TimeUnit.MINUTES);
                }
            }
            //Eliminar el mensaje que contenía la grosería
            event.getMessage().delete().queue();
        }


    }
}

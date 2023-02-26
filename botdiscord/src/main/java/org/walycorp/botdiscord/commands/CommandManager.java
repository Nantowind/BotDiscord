package org.walycorp.botdiscord.commands;

import net.dv8tion.jda.api.entities.Role;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.walycorp.botdiscord.credentials.EnvReader;

import java.util.ArrayList;
import java.util.List;


public class CommandManager extends ListenerAdapter {

    /**
     * Este método se llama cuando un usuario interactúa con un comando de barra inclinada (/).
     * <p>
     * Verifica el nombre del comando y realiza las acciones correspondientes.
     *
     * @param event El evento de interacción de comando de barra inclinada (/).
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Obtener el nombre del comando
        String command = event.getName().toLowerCase();

        // Verificar si el comando es 'welcome'
        if (command.equals("welcome")) {
            String userTag = event.getUser().getAsTag();
            event.reply("Bienvenido al server, " + userTag + "!").setEphemeral(true).queue();
        }

        // Verificar si el comando es 'roles'
        else if (command.equalsIgnoreCase("roles")) {
        // Ejecutar el comando 'roles'
            event.deferReply().queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()) {
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).setEphemeral(true).queue();
        }

        // Verificar si el comando es 'info'
        else if (command.equalsIgnoreCase("info")) {
            event.reply(EnvReader.OTHER_INFO.get("INFOLOBBY").replace("\n", "\n")).setEphemeral(true).queue();
        }

        // Verificar si el comando es 'admcleanchannel'
        else if (command.equalsIgnoreCase("admcleanchannel")) {
        // Verificar si el miembro que ejecuta el comando tiene permiso de administrador
            if (EnvReader.ALL_ADMIN_IDS.contains(event.getMember().getId())) {
                try {
                 // Obtener el canal de texto en el que se ejecuta el comando
                    TextChannel channel = (TextChannel) event.getChannel();
                // Borrar todos los mensajes en el canal
                    channel.purgeMessages(channel.getIterableHistory().complete());
                    event.reply("Se eliminaron todos los mensajes en el canal.").setEphemeral(true).queue();
                } catch (Exception e) {
                    event.reply("Hubo un error al eliminar los mensajes.").setEphemeral(true).queue();
                }
            } else {
                event.reply("No tienes permiso para ejecutar este comando.").setEphemeral(true).queue();
            }
        }
    }

    /**
     * Registra los comandos de barra inclinada (/) como comandos de GUILD (máximo 100).
     * Estos comandos se actualizarán instantáneamente y son excelentes para pruebas.
     *
     * @param event El evento GuildReady.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
    // Agregar los comandos a la lista
        commandData.add(Commands.slash("info", "Informacion util para nuevos."));
        commandData.add(Commands.slash("admcleanchannel", "Elimina todos los mensajes de este canal"));
    // Actualizar los comandos de GUILD
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    /**
     * Registers slash commands as GLOBAL commands (unlimited).
     * These commands may take up to an hour to update.
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Bienvenid@ al servidor. usa /info!"));
        commandData.add(Commands.slash("roles", "Muestra todos los roles de el server."));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }


}

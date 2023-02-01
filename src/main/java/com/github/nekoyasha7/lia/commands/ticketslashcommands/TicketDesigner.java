//<<< Package >>>//
package com.github.nekoyasha7.lia.commands.ticketslashcommands;
//<<< End Package >>>//

//<<< Imports >>>//
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;

import java.util.EnumSet;
//<<< End Imports >>>//
/*
 /* @author NekoYasha
 */
//<<< Class >>>//
public class TicketDesigner extends TicketAutor{

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if(event.getMember().getUser().isBot()) return;

        if (event.getName().equalsIgnoreCase("criar-painel-ticket-designer")) {

            String title = event.getOption("titulo").getAsString();
            String description = event.getOption("descricao").getAsString();
            buttonText = event.getOption("botao").getAsString();

            //Embed setups
            eb.setColor(Color.green);
            eb.setTitle(title);
            eb.setDescription(description);
            eb.addBlankField(false);

            event.getChannel().sendMessageEmbeds(eb.build())
                              .addActionRow(startButton(buttonText))
                              .queue();

            event.reply("Embed enviada com sucesso!")
                    .setEphemeral(true)
                    .queue();
        }
    }

        @Override
        public void onButtonInteraction(ButtonInteractionEvent event){

            if (event.getComponentId().equals("startButtonDesigner")) {

                String roles = String.valueOf(event.getMember().getRoles());

                event.reply("Ticket aberto com sucesso!")
                        .setEphemeral(true)
                        .queue();

                Member member = event.getMember();
                Guild guild = event.getGuild();

                String tickets = String.valueOf(guild.getChannels());
                if (tickets.contains("ticket-" + event.getMember().getUser().getName())) {
                    event.reply("Você já criou um ticket!")
                            .setEphemeral(true)
                            .queue();
                    return;
                }

                Category category = guild.getCategoryById(recrutamentoID);

                TextChannel privateTicket = guild.createTextChannel("ticket-" + member.getUser().getName())
                        .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                        .addPermissionOverride(guild.getRoleById(avaliadorId), EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .setParent(category)
                        .complete();


                avaliadorRoleName = "Vice-Líder";
                avaliadorId = "771617590853369896";
                Role role = guild.getRolesByName(avaliadorRoleName, true)
                        .get(0);


                for (Member roleMember : guild.getMembersWithRoles(role)) {
                    roleMember.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage("Um novo ticket para **Designer** foi aberto por " + member.getUser().getName() + "\n Verifique!")
                                .queue();
                    });
                }


                privateTicket.sendMessage("Olá, " + member.getAsMention() + " Fico contente que tenha optado por fazer uma avaliação na Vulcan Novels para **Designer**.\n" +
                                "Em breve um " + guild.getRoleById(avaliadorId).getAsMention() + " iniciará o seu processo de avaliação!")
                        .addActionRow(Button.danger("closeTicket", "Fechar ticket"))
                        .queue();
            }
        }

        @Override
        protected Button startButton(String buttonText){
            return Button.success("startButtonDesigner", buttonText);
        }
    }

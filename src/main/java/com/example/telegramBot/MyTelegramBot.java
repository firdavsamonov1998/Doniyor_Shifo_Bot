package com.example.telegramBot;

import com.example.accountent.controller.AccountentController;
import com.example.admin.controller.AdminController;
import com.example.config.BotConfig;
import com.example.nurse.controller.NurseController;
import com.example.owner.mainController.MainController;
import com.example.publicMenu.PublicController;
import com.example.service.UsersService;
import com.example.step.Step;
import com.example.step.TelegramUsers;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    List<TelegramUsers> usersList = new ArrayList<>();
    private final MainController mainController;
    private final AdminController adminController;
    private final NurseController nurseController;

    private final AccountentController accountentController;

    private final UsersService usersService;

    private final BotConfig botConfig;
    private final PublicController publicController;


    @Lazy
    public MyTelegramBot(BotConfig botConfig, MainController mainController,
                         AdminController adminController, NurseController nurseController,
                         AccountentController accountentController, UsersService usersService, PublicController publicController) {
        this.botConfig = botConfig;
        this.mainController = mainController;
        this.adminController = adminController;
        this.nurseController = nurseController;
        this.accountentController = accountentController;
        this.usersService = usersService;
        this.publicController = publicController;
    }


    @Override
    public void onUpdateReceived(Update update) {


        Long userId = update.getMessage().getFrom().getId();
        TelegramUsers telegramUsers = saveUser(userId);




        Message message = update.getMessage();

        if (telegramUsers.getStep() == null || telegramUsers.getStep().equals(Step.MAIN)) {


            if (message.hasText()) {

                if (message.getText().equals("#7777")) {

                    usersService.checkPasssword(message);
                    telegramUsers.setStep(Step.START);
                    return;
                }
            }

            if (userId == 1024661500){
                mainController.handle(message);
                return;
            }


            if (userId == 1360565 || userId == 191794566) {
                mainController.handle(message);
                return;
            }


            String role = usersService.findByUserId(message);


            if (role != null) {

                switch (role) {
                    case "NURSE" -> {
                        nurseController.handleNurse(message);

                    }
                    case "ACCOUNTENT" -> {
                        accountentController.handle(message);

                    }
                    default -> {
                        telegramUsers.setStep(Step.MAIN);
                    }
                }
                return;

            }

            if (update.hasMessage() && !message.getText().equals("#7777")) {
                publicController.handle(message);
                return;
            }
        }






        if (telegramUsers.getStep().equals(Step.START)) {

            if (usersService.isExsist(userId)){

                usersService.deleteByUserId(userId);
            }


            String userRole = usersService.findByPassword(message);

            if (userRole != null) {

                switch (userRole) {
                    case "NURSE" -> {
                        nurseController.handleNurse(message);
                        usersService.updateUserId(message);
                        telegramUsers.setStep(Step.MAIN);

                    }
                    case "ACCOUNTENT" -> {
                        accountentController.handle(message);
                        usersService.updateUserId(message);
                        telegramUsers.setStep(Step.MAIN);
                    }
                }
            }
            if (userRole == null) {
                usersService.sendErrorPassword(message);
                telegramUsers.setStep(Step.MAIN);
            }

        }


    }


    public TelegramUsers saveUser(Long chatId) {

        for (TelegramUsers users : usersList) {
            if (users.getChatId().equals(chatId)) {
                return users;
            }
        }
//        userController.getStep(chatId);


        TelegramUsers users = new TelegramUsers();
        users.setChatId(chatId);
        usersList.add(users);

        return users;
    }

    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendLocation sendLocation) {
        try {
            execute(sendLocation);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void send(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}

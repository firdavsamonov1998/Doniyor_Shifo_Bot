package com.example.nurse.controller;

import com.example.nurse.payload.NurseDTO;
import com.example.nurse.service.NurseService;
import com.example.step.Constant;
import com.example.step.Step;
import com.example.step.TelegramUsers;
import com.example.telegramBot.MyTelegramBot;
import com.example.util.SendMsg;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NurseController {
    private List<TelegramUsers> usersList = new ArrayList<>();
    private final NurseService nurseService;

    private final MyTelegramBot myTelegramBot;
    NurseDTO dto = new NurseDTO();

    public NurseController(NurseService nurseService, MyTelegramBot myTelegramBot) {
        this.nurseService = nurseService;
        this.myTelegramBot = myTelegramBot;
    }

    public void handleNurse(Message message) {

        TelegramUsers step = saveUser(message.getChatId());
        String text = message.getText();


        if (text.equals("/start") || step.getStep() == null) {
            nurseService.nurseMenuButton(message);
            step.setStep(Step.START);
        }

        if (step.getStep().equals(Step.START)) {

            switch (text) {

                case Constant.bemorQoshish -> {
                    nurseService.enterFullName(message);
                    step.setStep(Step.PATIENTFULLNAME);
                    return;
                }

                case Constant.bemorQidirish -> {
                    nurseService.searchPatientNameAndSurname(message);
                    step.setStep(Step.SEARCHPATIENT);
                    return;
                }

                case Constant.bemorOchirish -> {
                    nurseService.deletePatientById(message);
                    step.setStep(Step.DELETEDPATIENT);
                    return;
                }

                case Constant.bemorlarRoyhati -> {
                    nurseService.patientroyxati(message);
                    nurseService.patientList(message);
                    nurseService.nurseMenuButton2(message);
                    step.setStep(Step.START);
                    return;
                }
            }
        }

        //***************************** PATIENT REGISTRATION **************************************


        switch (step.getStep()) {

            case PATIENTFULLNAME -> {

                if (text.equals(Constant.bemorQoshish) || text.equals(Constant.bemorOchirish) ||
                        text.equals(Constant.bemorlarRoyhati) || text.equals(Constant.bemorQidirish)) {
                    myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                            "Iltimos bemor ismini tog'ri kiriting ❌"));
                    return;
                }

                dto.setFullName(text);
                nurseService.enterPhone(message);
                step.setStep(Step.PATIENTPHONE);
            }

            case PATIENTPHONE -> {


                boolean checkphone = nurseService.checkPhone(message);
                if (checkphone) {
                    dto.setPhone(text);
                    nurseService.enterFloor(message);
                    step.setStep(Step.PATIENTFLOOR);
                }
            }

            case PATIENTFLOOR -> {
                if (text.equals(Constant.bemorQoshish) || text.equals(Constant.bemorOchirish) ||
                        text.equals(Constant.bemorlarRoyhati) || text.equals(Constant.bemorQidirish)) {
                    myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                            "Iltimos qavat raqamini tog'ri kiriting ❌"));
                    return;
                }

                if (text.startsWith("2") || text.startsWith("3")) {
                    dto.setFloor(text);
                    nurseService.enterHouse(message);
                    step.setStep(Step.PATIENTHOUSE);
                    return;
                }

                myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                        "Iltimos qavat raqamini tog'ri kiriting (2-qavat yoki 3-qavat) ❌"));
                return;
            }

            case PATIENTHOUSE -> {
                if (text.equals(Constant.bemorQoshish) || text.equals(Constant.bemorOchirish) ||
                        text.equals(Constant.bemorlarRoyhati) || text.equals(Constant.bemorQidirish)) {
                    myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                            "Iltimos xona raqamini tog'ri kiriting ❌"));
                    return;
                }
                dto.setRoom(text);
                nurseService.endPatientRegistration(message);
                nurseService.nurseMenuButton2(message);
                step.setStep(Step.START);
                nurseService.creationPatient(dto);

            }
        }

        //************************** SEARCH PATIENT *******************************************

        if (step.getStep().equals(Step.SEARCHPATIENT)) {
            boolean searchpatient = nurseService.handlePatient(message);
            if (searchpatient) {
                nurseService.nurseMenuButton2(message);
                step.setStep(Step.START);

            }
            nurseService.nurseMenuButton(message);
            step.setStep(Step.START);
            return;
        }

        //************************* PATIENT DELETE ******************************************

        if (step.getStep().equals(Step.DELETEDPATIENT)) {
            boolean deleted = nurseService.deletedById(message);
            if (deleted) {
                nurseService.enddeletedPatient(message);
                nurseService.nurseMenuButton2(message);
                step.setStep(Step.START);
            }
        }

        //******************************************************************************
    }


    public TelegramUsers saveUser(Long chatId) {

        for (TelegramUsers users : usersList) {
            if (users.getChatId().equals(chatId)) {
                return users;
            }
        }


        TelegramUsers users = new TelegramUsers();
        users.setChatId(chatId);
        usersList.add(users);
        return users;
    }

}

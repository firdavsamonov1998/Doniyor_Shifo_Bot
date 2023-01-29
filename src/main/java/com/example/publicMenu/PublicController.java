package com.example.publicMenu;

import com.example.nurse.service.NurseService;
import com.example.owner.mainController.PatientController;
import com.example.publicMenu.menu.MenuController;
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
public class PublicController {

    private final MenuController menuController;

    private final MyTelegramBot myTelegramBot;

    private final NurseService service;

    private final PatientController patientController;


    private List<TelegramUsers> usersList = new ArrayList<>();


    public PublicController(MenuController menuController, MyTelegramBot myTelegramBot, NurseService service, PatientController patientController) {
        this.menuController = menuController;
        this.myTelegramBot = myTelegramBot;
        this.service = service;
        this.patientController = patientController;
    }


    public void handle(Message message) {

        TelegramUsers users = saveUser(message.getChatId());

        if (message.hasText()) {
            if (message.getText().equals("#6666")){
                service.patientList(message);
                return;
            }
        }
        if (message.getText().equals("/start") || users.getStep() == null) {
            menuController.mainController(message);
            users.setStep(Step.MAIN);
            return;
        }

        if (users.getStep().equals(Step.MAIN)) {

            String text = message.getText();

            switch (text) {
                case Constant.bemorQidirish -> {

                    menuController.searchPatient(message);
                    users.setStep(Step.SEARCHPATIENT);

                }
                case Constant.about -> {
                    myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                            "  Aloqa \n" +
                                    "📞 Call Markaz: +998 97-277-0303\n" +
                                    "📞 Hostpat: +998 95-511-03-03\n\n" +
                                    "\uD83D\uDCCD Manzil\n" +
                                    "  Sirdaryo Viloyati Sirdaryo tumani\n" +
                                    "  Ziyokor shaharchasi mo'ljal Malek burilish \n\n" +
                                    "Ijtimoy Tarmoqlar \n" +
                                    "Obuna bo'ling: \n\n" +
                                    "✅ Telegram: https://t.me/doniyorshifoclinik \n" +
                                    "✅ Instagram: https://instagram.com/doniyor_shifo?igshid=YmMyMTA2M2Y= "));
                }

                case Constant.location ->
                    myTelegramBot.send(SendMsg.sendLoc(message.getChatId(),
                            68.675979,40.799126));

            }
            return;
        }

        switch (users.getStep()) {
            case SEARCHPATIENT -> {
                patientController.handle(message);
                users.setStep(Step.MAIN);
            }
        }


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

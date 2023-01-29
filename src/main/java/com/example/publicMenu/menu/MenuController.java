package com.example.publicMenu.menu;

import com.example.step.Constant;
import com.example.telegramBot.MyTelegramBot;
import com.example.util.Button;
import com.example.util.SendMsg;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;

@Controller
public class MenuController {

    private final MyTelegramBot myTelegramBot;

    public MenuController(MyTelegramBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }


    public void mainController(Message message) {

        myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "Assalomu aleykum Doniyor Shifo Botiga xush kelibsiz ",
                Button.markup(Button.rowList(Button.row(
                                        Button.button(Constant.bemorQidirish),
                                        Button.button(Constant.about)
                                ),
                                Button.row(Button.button(Constant.location)))
                )
        ));
    }

    public void searchPatient(Message message) {
        myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "Qidirmoqchi bo'lgan Bemor Ismi va Familiyasini kiriting "));

    }
}

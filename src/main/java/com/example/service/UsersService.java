package com.example.service;

import com.example.entity.UsersEntity;
import com.example.enums.Status;
import com.example.owner.repository.UsersRepository;
import com.example.telegramBot.MyTelegramBot;
import com.example.util.SendMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Service
public class UsersService {


    private final MyTelegramBot myTelegramBot;

    private final UsersRepository usersRepository;

    public UsersService(MyTelegramBot myTelegramBot, UsersRepository usersRepository) {
        this.myTelegramBot = myTelegramBot;
        this.usersRepository = usersRepository;
    }




    public void checkPasssword(Message message) {
        myTelegramBot.send(SendMsg.sendMsg(message.getChatId()
                , "Iltimos bot dan foydalanish uchun sizga berilgan parolni kiriting : "));
    }


    public String findByPassword(Message message) {
        Optional<UsersEntity> optional = usersRepository.findByPasswordAndStatus(message.getText(),Status.ACTIVE);

        System.out.println("daw");
        if (optional.isEmpty()) {
            System.out.println("not");
            return null;

        }


        UsersEntity entity = optional.get();

        return entity.getRole().toString();
    }
    public String findByUserId(Message message) {
        Optional<UsersEntity> optional = usersRepository.findByUserIdAndStatus(message.getChatId(), Status.ACTIVE);

        if (optional.isEmpty()) {
            return null;
        }


        UsersEntity entity = optional.get();

        return entity.getRole().toString();
    }


    public void sendErrorPassword(Message message) {
        myTelegramBot.send(SendMsg.sendMsg(message.getChatId(),
                "Parol xato ! Iltimos qaytadan urinib ko'ring !"));
    }

    public boolean updateUserId(Message message){
        Optional<UsersEntity> optional = usersRepository.findByPasswordAndStatus(message.getText(),Status.ACTIVE);

        if (optional.isEmpty()) {
            return false;
        }

        UsersEntity entity = optional.get();

        entity.setUserId(message.getFrom().getId());

        usersRepository.save(entity);

        return true;

    }


    public boolean getByPassword(Message message) {
        return usersRepository.existsByPassword(message.getText());
    }

    public boolean isExsists(Long userId) {
        return usersRepository.existsByUserId(userId);
    }

    public void deleteByUserId(Long userId) {
        Optional<UsersEntity> optional = usersRepository.findByUserIdAndStatus(userId, Status.ACTIVE);

        UsersEntity users = optional.get();

        users.setStatus(Status.BLOCK);

        usersRepository.save(users);
    }

    public boolean isExsist(Long userId) {
        return usersRepository.existsByUserIdAndStatus(userId,Status.ACTIVE);
    }
}

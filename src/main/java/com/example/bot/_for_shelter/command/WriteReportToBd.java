package com.example.bot._for_shelter.command;


import com.example.bot._for_shelter.model.ReportDTO;
import com.example.bot._for_shelter.service.ReportService;

import com.example.bot._for_shelter.service.TelegramBot;
import com.example.bot._for_shelter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.Comparator;
import java.util.List;

import static com.example.bot._for_shelter.command.CommandName.WriteReportToBd1;

@Component
public class WriteReportToBd implements Command {

    private final UserService userService;
    private final ReportService reportService;
    private final SendBotMessageService sendBotMessageService;
    private final TelegramBot telegramBot;

    private final String botToken = "7325728181:AAHXOsha4hsxDQXaSY0k3c54x13dWVwLEu8";


    @Autowired
    @Lazy
    public WriteReportToBd(UserService userService, ReportService reportService, SendBotMessageService sendBotMessageService, TelegramBot bot, TelegramBot telegramBot) {
        this.userService = userService;
        this.reportService = reportService;
        this.sendBotMessageService = sendBotMessageService;

        this.telegramBot = telegramBot;
    }

    @Override
    public void execute(Update update) {
        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            userService.changeCondition(chatId, "report");
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Напиши свой текстовый отчет");
            sendBotMessageService.sendMessageWithKeyboardMarkup(message);
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            ReportDTO report = new ReportDTO();
            report.setText(text);
            report.setChatId(String.valueOf(chatId));
            reportService.addReport(report);
        }
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            long chat_id = update.getMessage().getChatId();
            List<PhotoSize> photos = update.getMessage().getPhoto();
            String f_id = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize))
                    .map(PhotoSize::getFileId)
                    .orElse("");
            System.out.println(f_id.length());
            SendPhoto msg = SendPhoto
                    .builder()
                    .chatId(String.valueOf(chat_id))
                    .photo(new InputFile(f_id))
                    .build();

            telegramBot.sendPhoto(msg);
        }
    }


    @Override
    public boolean isSupport(String command) {
        return command.equals(WriteReportToBd1.getCommandName());
    }
}


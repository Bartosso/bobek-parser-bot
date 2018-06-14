import db.DaoFactory;
import db.UserDao;
import model.Ad;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

public class TBot extends TelegramLongPollingBot {

    private UserDao ud = DaoFactory.getFactory().getUserDao();

    @Override
    public void onUpdateReceived(Update update) {

        String ci = update.hasMessage() ?
                update.getMessage().getFrom().getId().toString() :
                update.getCallbackQuery().getFrom().getId().toString();

        System.out.println("message from " + ci);

        try {

            if (!ud.checkUsersIsExist(ci)) {

                ud.addNewUser(ci);

                execute(new SendMessage(ci, "Вы успешно добавлены в рассылку"));

            } else {

                execute(new SendMessage(ci, "Вы уже добавлены в рассылку"));

            }

        } catch (SQLException | TelegramApiException e) {

            e.printStackTrace();

        }


    }

    //Todo Put username here, or whatever
    @Override
    public String getBotUsername() {
        return "";
    }

    //Todo put token here
    @Override
    public String getBotToken() {
        return "";
    }


    void sendNotifications(Ad a){

        try {

            List<String> is = ud.getUsersChatIds();
            is.forEach(i -> notifyUser(a, i));

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    private void notifyUser(Ad a, String chatId){

        try {

            this.execute(new SendMessage().setChatId(chatId).setText(
                            "<b>Новое объявление!</b>\n<b>Заголовок:</b> " + (a.getHeader().equals("") ?
                            "заголовка нет" :
                            a.getHeader())  +
                            "\n<b>Дата публикации:</b> " + a.getPubDate() +
                            "\n<b>Текст:</b>\n" + a.getText()).setParseMode(ParseMode.HTML));

        } catch (TelegramApiException e) {

            e.printStackTrace();

        }
    }

}

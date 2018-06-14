import db.ADDao;
import db.DaoFactory;
import model.Ad;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.sql.SQLException;

public class Main {

    private static ADDao adao = DaoFactory.getFactory().getADAO();
    private static TBot bot;

    public static void main(String[] args) {

        ApiContextInitializer.init();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {

            TBot bot           = new TBot();
            telegramBotsApi.registerBot(bot);
            Main.bot = bot;

            adao.initTable();
            DaoFactory.getFactory().getUserDao().initTable();

        } catch (TelegramApiRequestException e) {
            throw new RuntimeException(e);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true) {
            try {

                System.out.println("checking bobek");

                Document doc = Jsoup.connect("https://bobek.kz/%D0%BE%D0%B1%D1%8A%D1%8F%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D1%8F/#bobek").get();

                Elements ads = doc.body().select("div[class=col-md-8 col-sm-8]");

                checkElems(ads);

                Thread.sleep(300000);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }


    private static void checkElems(Elements es) {

        es.forEach(Main::checkElem);

    }

    private static void checkElem(Element e) {

        try {
            if (!adao.isExistWithThatText(e.select("p").text())) {
                Ad ad = new Ad(
                        e.selectFirst("h3").text(),
                        e.selectFirst("h4").text(),
                        e.selectFirst("p").text());
                ExecutorS.execute(() -> {

                    try {

                        adao.addAd(ad);
                        bot.sendNotifications(ad);

                    } catch (SQLException e1) {

                        e1.printStackTrace();

                    }

                });
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

    }

}

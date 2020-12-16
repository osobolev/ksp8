package ru.mirea.ksp.example8;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.staticfiles.Location;
import ru.mirea.ksp.example8.dao.GameDao;
import ru.mirea.ksp.example8.dao.LoginDao;
import ru.mirea.ksp.example8.dao.LoginUser;
import ru.mirea.ksp.example8.dao.User;
import ru.mirea.ksp.example8.rest.GameStateResponse;
import ru.mirea.ksp.example8.rest.LoginRequest;
import ru.mirea.ksp.example8.rest.LoginResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameApplication {

    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();
        config.setAutoCommit(false);
        config.setJdbcUrl("jdbc:h2:~/game_db");
        DataSource dataSource = new HikariDataSource(config);
        BCrypt.Verifyer verifyer = BCrypt.verifyer();

        Javalin app = Javalin.create(appConfig -> {
            appConfig.showJavalinBanner = false;
            appConfig.addStaticFiles("web", Location.EXTERNAL);
        });

        // Вход/выход:
        app.post("/rest/login", ctx -> {
            LoginRequest login = ctx.bodyAsClass(LoginRequest.class);
            try (Connection connection = dataSource.getConnection()) {
                LoginDao dao = new LoginDao(connection);
                LoginUser user = dao.login(login.getLogin());
                if (user != null) {
                    BCrypt.Result result = verifyer.verify(login.getPassword().toCharArray(), user.passwordHash);
                    if (result.verified) {
                        ctx.sessionAttribute(SessionUser.CURRENT_USER, new SessionUser(user.id, user.displayName));
                        ctx.json(new LoginResponse(true, null, user.displayName));
                        return;
                    }
                }
                ctx.json(new LoginResponse(false, "Неправильный пользователь или пароль", null));
            }
        });
        app.post("/rest/logout", ctx -> {
            ctx.sessionAttributeMap().remove(SessionUser.CURRENT_USER);
            ctx.status(200);
        });

        // Проверка доступа к приложению:
        app.before("/app/*", ctx -> {
            SessionUser user = ctx.sessionAttribute(SessionUser.CURRENT_USER);
            if (user == null) {
                ctx.redirect("/login.html");
            }
        });
        app.before("/rest/app/*", ctx -> {
            SessionUser user = ctx.sessionAttribute(SessionUser.CURRENT_USER);
            if (user == null) {
                throw new ForbiddenResponse();
            }
        });

        // Главные функции приложения:
        app.post("/rest/app/click", ctx -> {
            SessionUser user = ctx.sessionAttribute(SessionUser.CURRENT_USER);
            assert user != null;
            Thread.sleep(5000); // Для демонстрации прогресса загрузки
            try (Connection connection = dataSource.getConnection()) {
                GameDao dao = new GameDao(connection);
                dao.addClick(user.id);
                connection.commit();
            }
        });
        app.get("/rest/app/gameState", ctx -> {
            SessionUser user = ctx.sessionAttribute(SessionUser.CURRENT_USER);
            assert user != null;
            try (Connection connection = dataSource.getConnection()) {
                GameDao dao = new GameDao(connection);
                int clicks = dao.getClicks(user.id);
                int maxClicks = dao.getMaxClicks();
                List<User> winners = dao.getWinners(maxClicks);
                List<String> winnerNames;
                boolean winner;
                if (maxClicks > 0) {
                    winner = clicks >= maxClicks;
                    winnerNames = winners.stream().map(w -> w.displayName).collect(Collectors.toList());
                } else {
                    winner = false;
                    winnerNames = Collections.emptyList();
                }
                GameStateResponse state = new GameStateResponse(user.displayName, winner, clicks, maxClicks, winnerNames);
                ctx.json(state);
            }
        });

        app.start(8080);
    }
}

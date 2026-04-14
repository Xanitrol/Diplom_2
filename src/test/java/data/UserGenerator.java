package data;

import model.User;

public class UserGenerator {

    public static User getRandomUser() {
        long currentTime = System.currentTimeMillis();
        return new User(
                "user" + currentTime + "@yandex.ru",
                "password" + currentTime,
                "name" + currentTime
        );
    }
}
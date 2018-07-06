package api;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.requests.Requests;
import com.beust.jcommander.Parameter;

import org.testng.Assert;
import org.testng.annotations.Test;

//import hillelauto.api.requests.Requests;

    /**
     * Created by hillel on 18.08.17.
     */
    public class Tests {
        String baseURL = "http://soft.it-hillel.com.ua:3000/api/users";
        String userId = "";

        //если  строка соответствует паттерну, то она записывается в userId
        private void findUserID(String data) {
            Matcher m = Pattern.compile("\"id\":\"(\\d+)").matcher(data); // в кавычкалх m.group(1)
            if (m.find())
                userId = m.group(1);
        }

        //по заданию у нас Content-Type: application/json, проверим это:
        private void checkContentType(String headers) {
            Assert.assertTrue(headers.contains("Content-Type: application/json"));
        }

//СОБСТВЕННО ТЕСТЫ
        @Test(description = "Second requirement - getting user list")
        void getUsers() throws IOException {
             /*
            Requeste - наш отдельный файл, метод sendGet вовращает массив строк.
            Посылаем запрос и получаем массив строк (всякие данные, которые приходят по АПИ)
            */
            String[] responseData = Requests.sendGet(baseURL);
            Assert.assertTrue(responseData[1].contains("[{\"id\":\""));// проверяем является ли ID
            findUserID(responseData[1]); // проверяем, является ли ID и запоминаем это ID
            checkContentType(responseData[0]); // всегда проверяем, а то у нас по заданию так
        }

        @Parameter
        @Test(description = "Third requirement - saving users")
        void saveUser(String data, Boolean expectedResult) throws IOException {
            String[] responseData = Requests.sendPut(baseURL + userId, '{' + data + '}');
            Assert.assertEquals((Boolean) Requests.getUserInfo(baseURL, userId).contains(data), expectedResult);
            checkContentType(responseData[0]);
        }
}

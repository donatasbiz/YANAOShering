import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Main {
    private static int chekCode = -1;

    public static void main(String[] args) {
        try {
            JNISugar jniSugar = new JNISugar();
            jniSugar.Start();
            jniSugar.Wait();
        } catch (Throwable t) {
            System.out.println(t.toString());
        }

    }

    public static String callback(String req) {
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        String stringJson = "";
        jsonObject = gson.fromJson(stringJson, JsonObject.class);
        String stringRequest = jsonObject.get("req").toString();
        String response = "";
        switch (stringRequest) {
            case "\"ChekPhoneUser\"":
                response = ChekPhoneUser(stringJson, jsonObject, gson);
                break;
            case "\"ChekKodUser\"":
                response = ChekKodUser(stringJson, jsonObject, gson);
                break;
            case "\"DBdataUser\"":
                response = DBdataUser(stringJson, gson);
                break;
            case "DBdataAdmin":
                response = DBdataAdmin(stringJson, gson);
                break;
            case "\"ChekPhoneAdmin\"":
                response = ChekPhoneAdmin(stringJson, jsonObject, gson);
                break;
            case "\"ChekKodAdmin\"":
                response = ChekKodAdmin(stringJson, jsonObject, gson);
                break;
            case "\"AddNewPoint\"":
                response = AddNewPoint(stringJson, gson);
                break;
            case "\"BookingForesponceUserser\"":
                response = BookingForesponceUserser(stringJson, gson);
                break;
            case "\"AddNewInventoryForAdmin\"":
                response = AddNewInventoryForAdmin(stringJson, jsonObject, gson);
                break;
        }
        return response;
    }

    private static String DBdataUser(String stringJson, Gson gson) {
        ResUser responceUser = gson.fromJson(stringJson, ResUser.class);
        //вызываю метод куда передаю данные пользователя
        try {
            ResUser.jniSugar.CreateUser(responceUser.name, responceUser.lastName, responceUser.mail, responceUser.phone, responceUser.res);
            Response responce = new Response();
            responce.res = "DataUserAcceptet";
            String responseString = gson.toJson(responce);
            return responseString;
        } catch (Throwable e) {
            Response responce = new Response();
            responce.res = "DataUserNoAcceptet";
            String responseString = gson.toJson(responce);
            return responseString;
        }
    }

    private static String ChekPhoneUser(String stringJson, JsonObject jsonObject, Gson gson) {
        jsonObject = gson.fromJson(stringJson, JsonObject.class);
        String stringPhone = jsonObject.get("phone").toString();
        //отправляю в бд проверить есть такой или нет
        String responseString = "";
        try {

            if (ResUser.jniSugar.ChekUserPhone(stringPhone)) {
                jsonObject.addProperty("res", "phoneInDB");
                responseString = gson.toJson(jsonObject);
            } else {
                jsonObject.addProperty("res", "phoneNotInDB");
                responseString = gson.toJson(jsonObject);
            }
            chekCode = ((int) (Math.random() * 10000) + 10000) % 99999;
            System.out.println(chekCode);
            //отправляю "chekCode" на сервер
            ResUser.jniSugar.SendVerPhone(stringPhone, chekCode);
            return responseString;
        } catch (Exception e) {
            Response responce = new Response();
            responce.res = "ProblemWithKod";
            responseString = gson.toJson(responce);
            return responseString;
        }

    }

    private static String ChekKodUser(String stringJson, JsonObject jsonObject, Gson gson) {

        jsonObject = gson.fromJson(stringJson, JsonObject.class);
        String stringKod = jsonObject.get("kodUser").toString();
        String ansString = stringKod.substring(1, stringKod.length() - 1);
        int ansKod = Integer.parseInt(ansString);
        if (chekCode == ansKod) {
            Response ansJson = new Response();
            ansJson.res = "Верно";
            String resJson = gson.toJson(ansJson);
            return resJson;
        } else {
            Response ansJson = new Response();
            ansJson.res = "Неверно";
            String resJson = gson.toJson(ansJson);
            return resJson;
        }

    }

    private static String DBdataAdmin(String stringJson, Gson gson) {
        ReqAdmin requestAdmin = gson.fromJson(stringJson, ReqAdmin.class);
        //вызываю метод куда передаю данные админа
        try {
            ResUser.jniSugar.CreateAdmin(requestAdmin.name, requestAdmin.lastName, requestAdmin.mail, requestAdmin.phone, requestAdmin.nameFirm, requestAdmin.req);
            Response responce = new Response();
            responce.res = "DataAdminAcceptet";
            String responseString = gson.toJson(responce);
            return responseString;
        } catch (Exception e) {
            Response responce = new Response();
            responce.res = "DataAdminNoAcceptet";
            String responseString = gson.toJson(responce);
            return responseString;
        }


    }

    private static String ChekPhoneAdmin(String stringJson, JsonObject jsonObject, Gson gson) {
        jsonObject = gson.fromJson(stringJson, JsonObject.class);
        String stringPhone = jsonObject.get("phone").toString();
        String responseString = "";
        //отправляю в бд проверить есть такой или нет
        try {

            if (ResUser.jniSugar.CheckAdminPhone(stringPhone)) {
                jsonObject.addProperty("res", "phoneInDB");
                responseString = gson.toJson(jsonObject);


            } else {
                jsonObject.addProperty("res", "phoneNotInDB");
                responseString = gson.toJson(jsonObject);


            }
            chekCode = ((int) (Math.random() * 10000) + 10000) % 99999;
            System.out.println(chekCode);
            //отправляю "chekCode" на сервер
            ResUser.jniSugar.SendVerPhone(stringPhone, chekCode);

            return responseString;
        } catch (Exception e) {
            Response re = new Response();
            re.res = "ProblemWithKod";
            responseString = gson.toJson(re);
            return responseString;
        }
    }

    private static String ChekKodAdmin(String stringJson, JsonObject jsonObject, Gson gson) {
        jsonObject = gson.fromJson(stringJson, JsonObject.class);
        String stringKod = jsonObject.get("kodAdmin").toString();
        String ansString = stringKod.substring(1, stringKod.length() - 1);
        int ansKod = Integer.parseInt(ansString);
        if (chekCode == ansKod) {
            Response ansJson = new Response();
            ansJson.res = "Верно";
            String resJson = gson.toJson(ansJson);
            return resJson;
        } else {
            Response ansJson = new Response();
            ansJson.res = "Неверно";
            String resJson = gson.toJson(ansJson);
            return resJson;
        }
    }

    private static String AddNewPoint(String stringJson, Gson gson) {

        AdminPointProkat adminPointProkat = gson.fromJson(stringJson, AdminPointProkat.class);
        //вызываю метод куда передаю данные точки проката админа через класс
        try {
            ResUser.jniSugar.SetNewPointInfo(adminPointProkat.nameCompany, adminPointProkat.latitude, adminPointProkat.longitude,
                    adminPointProkat.timeOpen, adminPointProkat.timeClose);
            Response response = new Response();
            response.res = "DataAdminPointProkatAcceptet";
            String responseString = gson.toJson(response);
            return responseString;
        } catch (Exception e) {
            Response response = new Response();
            response.res = "DataAdminPointProkatNoAcceptet";
            String responseString = gson.toJson(response);
            return responseString;
        }

    }

    private static String BookingForesponceUserser(String stringJson, Gson gson) {
        BookingForUser bookingForUser = gson.fromJson(stringJson, BookingForUser.class);
        //вызываю метод куда передаю данные о брони
        try {
            ResUser.jniSugar.SetBookingInfo(bookingForUser.phone, bookingForUser.latitude, bookingForUser.longitude, bookingForUser.nameCompany,
                    bookingForUser.typeInventory, bookingForUser.timeBroni);
            Response response = new Response();
            response.res = "BookingWasSuccessful";
            String responseString = gson.toJson(response);
            return responseString;
        } catch (Exception e) {
            Response response = new Response();
            response.res = "BookingWasNotSuccessful";
            String responseString = gson.toJson(response);
            return responseString;
        }
    }

    private static String AddNewInventoryForAdmin(String stringJson, JsonObject jsonObject, Gson gson) {
        AddNewInventory addNewInventory = gson.fromJson(stringJson, AddNewInventory.class);
        jsonObject = gson.fromJson(stringJson, JsonObject.class);
        String stringKod = jsonObject.get("count").toString();
        String ansString = stringKod.substring(1, stringKod.length() - 1);
        int countInventory = Integer.parseInt(ansString);
//отправляю инфу в бд

        try {
            ResUser.jniSugar.SetNewInventoryInfo(addNewInventory.latitude, addNewInventory.longitude, addNewInventory.typeInventory,
                    addNewInventory.season, countInventory);
            Response response = new Response();
            response.res = "AddNewInventoryWasSuccessful";
            String responseString = gson.toJson(response);
            return responseString;
        } catch (Exception e) {
            Response response = new Response();
            response.res = "AddNewInventoryWasNotSuccessful";
            String responseString = gson.toJson(response);
            return responseString;
        }

    }
}

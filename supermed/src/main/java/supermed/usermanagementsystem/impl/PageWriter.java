package supermed.usermanagementsystem.impl;

import supermed.usermanagementsystem.user.Employee;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;

/**
 * Created by Alexander on 11.12.2016.
 */
public class PageWriter {
    private static final String HEADING = "<html><head></head><body><div class=\"container\">\n" +
            "      <div class=\"row\">\n" +
            "      <div class=\"col-md-5  toppad  pull-right col-md-offset-3 \">\n" +
            "           <a href=\"edit.html\">Edit Profile</a>\n" +
            "\n" +
            "        <a href=\"edit.html\">Logout</a>\n" +
            "       <br>\n" +
            "\n" +
            "      </div>\n" +
            "        <div class=\"col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xs-offset-0 " +
            "col-sm-offset-0 col-md-offset-3 col-lg-offset-3 toppad\">\n" +
            "   \n" +
            "   \n" +
            "          <div class=\"panel panel-info\">\n" +
            "            <div class=\"panel-heading\">\n" +
            "              \n" +
            "            </div>\n" +
            "            <div class=\"panel-body\">\n" +
            "              <div class=\"row\">\n" +
            "                <div class=\"col-md-3 col-lg-3 \" align=\"center\"> <img alt=\"User " +
            "Pic\" src=\"http://babyinfoforyou" +
            ".com/wp-content/uploads/2014/10/avatar-300x300.png\" class=\"img-circle " +
            "img-responsive\"> </div>\n" +
            "                \n" +
            "                \n" +
            "                <div class=\" col-md-9 col-lg-9 \"> \n" +
            "                  <table class=\"table table-user-information\">\n" +
            "                    <tbody>";
    private static final String ENDING = "</tbody>\n" +
            "                  </table>\n" +
            "                </div>\n" +
            "              </div>\n" +
            "            </div>          \n" +
            "          </div>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "    </div></body></html>";

    public static String printUserProfilePage(User user) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HEADING);
        stringBuilder.append(getUserLastName(user));
        stringBuilder.append(getUserFirstName(user));
        stringBuilder.append(getUserMiddleName(user));
        stringBuilder.append(getUserBirthDate(user));
        stringBuilder.append(getUserAddress(user));
        stringBuilder.append(getUserPhoneNumber(user));
        stringBuilder.append(getUserEmail(user));
        if (!user.getRole().equals(Role.PATIENT)) {
            stringBuilder.append(getEmployeeHireDate((Employee) user));
            stringBuilder.append(getEmployeePosition((Employee) user));
        }
        stringBuilder.append(ENDING);
        return stringBuilder.toString();
    }

    private static String getUserFirstName(User user) {
        return " <tr><td>Имя</td><td>" + user.getUserData().getFirstName() + "</td></tr > ";

    }

    private static String getUserMiddleName(User user) {
        return " <tr><td>Отчество</td><td>" + user.getUserData().getMiddleName() + "</td></tr > ";
    }

    private static String getUserLastName(User user) {
        return " <tr><td>Фамилия</td><td>" + user.getUserData().getLastName() + "</td></tr > ";
    }

    private static String getUserBirthDate(User user) {
        return " <tr><td>Фамилия</td><td>" + user.getUserData().getLastName() + "</td></tr > ";
    }

    private static String getUserAddress(User user) {
        return " <tr><td>Адрес</td><td>" + user.getUserData().getAddress() + "</td></tr > ";
    }

    private static String getUserEmail(User user) {
        return " <tr><td>Email</td><td>" + user.getUserData().getEmail() + "</td></tr > ";
    }

    private static String getUserPhoneNumber(User user) {
        return " <tr><td>Контактный номер</td><td>" + user.getUserData().getPhoneNumber() +
                "</td></tr > ";
    }

    private static String getEmployeeHireDate(Employee employee) {
        return " <tr><td>Дата заключения трудового договора</td><td>" + employee.getHireDate() +
                "</td></tr > ";
    }

    private static String getEmployeePosition(Employee employee) {
        return " <tr><td>Должность</td><td>" + employee.getPosition() +
                "</td></tr > ";
    }
}

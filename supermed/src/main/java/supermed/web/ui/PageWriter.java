package supermed.web.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import supermed.consultancysystem.Visit;
import supermed.datamanagementsystem.DataManager;
import supermed.datamanagementsystem.impl.DataManagerImpl;
import supermed.statisticsframework.event.Event;
import supermed.statisticsframework.event.EventStatus;
import supermed.statisticsframework.Schedule;
import supermed.usermanagementsystem.user.Employee;
import supermed.usermanagementsystem.user.Role;
import supermed.usermanagementsystem.user.User;
import supermed.usermanagementsystem.user.UserData;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander on 11.12.2016.
 */
@Component
public class PageWriter {

    @Autowired
    DataManager dataManager;

    private static final String HEADING = "<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" name=\"viewport\" " +
            "content=\"width=device-width, initial-scale=1\">\n" +
            "<link rel=\"stylesheet\" href=\"https://code.jquery.com/mobile/1.4.5/jquery" +
            ".mobile-1.4.5.min.css\">\n" +
            "<script src=\"https://code.jquery.com/jquery-1.11.3.min.js\"></script>\n" +
            "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.14.1/moment.min" +
            ".js\"></script>\n" +
            "<script src=\"https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min" +
            ".js\"></script></head><body><div class=\"container\">\n" +
            "      <div class=\"row\">\n" +
            "      <div class=\"col-md-5  toppad  pull-right col-md-offset-3 \">\n" +
            "\n" +
            "        <a href=\"../login\">Logout</a>\n" +
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
            "            </div>\n";

    private static final String ENDING = "</tbody>\n" +
            "                  </table>\n" +
            "                </div>\n" +
            "              </div>\n" +
            "            </div>          \n" +
            "          </div>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "    </div></body></html>";

    public String printUserProfilePage(User user) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HEADING);
        if (user.getRole().equals(Role.PATIENT)) {
            stringBuilder.append(insertUserMenu(user));
        } else {
            stringBuilder.append(insertUserMenu(user));
        }
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
            stringBuilder.append(getEmployeeBranch((Employee) user));
        }
        stringBuilder.append(ENDING);
        return stringBuilder.toString();
    }

    private static String getEmployeeBranch(Employee user) {
        return " <tr><td>Адрес отделения</td><td>" + user.getBranchAddress() + "</td></tr > ";
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
        return " <tr><td>Дата рождения</td><td>" + user.getUserData().getBirthDate() + "</td></tr" +
                " > ";
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

    public static String printErrorPage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<html><head></head><body>");
        String error = "<h2>Incorrect Data</h2>\n";
        stringBuilder.append(error);
        stringBuilder.append("</body></html>");
        return stringBuilder.toString();
    }

    private String insertUserMenu(User user) {
        String insertForm = "   <table align=\"right\">\n" +
                " <tr><td>  \n" +
                " <div data-role=\"main\" class=\"ui-content\" align=\"right\">\n" +
                "    " +
                "<a href=\"#myPopup\" data-rel=\"popup\" class=\"ui-btn ui-btn-inline ui-corner-all\">Запись на прием</a>\n" +
                "\n" +
                "    <div data-role=\"popup\" id=\"myPopup\" class=\"ui-content\" " +
                "style=\"min-width:250px;\">\n" +
                "      <form method=\"post\" action=\"./visits\">\n" +
                "        <div>\n" +
                "          <h3>Выберите день</h3>\n" +
                "\t\t  <p>Желаемая дата приема: <input name=\"dateOfVisit\" id=\"visitDate\" " +
                "type=\"date\" ></p>\n" +
                "<script>var m = moment().format(\"YYYY-MM-DD\");document.getElementById" +
                "('visitDate').value = m;</script>" +
                insertBranches() +
                "        </div>\n<input type=\"submit\" data-inline=\"true\" value=\"Далее\">" +
                "      </form>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  </td></tr>\n" +
                "          <tr><td>\n" +
                "            <button type=\"button\" class=\"shortButton\">Онлайн " +
                "консультации</button></tr><td></table>" + insertImage() +
                "                <div class=\" col-md-9 col-lg-9 \"> \n" +
                "                  <table class=\"table table-user-information\">\n" +
                "                    ";
        if (user.getRole() == Role.MANAGER) {
            insertForm += "<a href=\"../create_patient\"><button type=\"button\" " +
                    "class=\"shortButton\">Создать пациента</button></a>" +
                    "<a href=\"../create_employee\"><button type=\"button\" " +
                    "class=\"shortButton\">Создать работника</button></a>" +
                    "<a href=\"../show_profiles\"><button type=\"button\" " +
                    "class=\"shortButton\">Просмотр профилей</button></a>" +
                    "<a href=\"http://localhost:8080/supermed-1.0/update_yourself/" + user.getID() + "\"><button type=\"button\" " +
                    "class=\"shortButton\">Редактировать</button></a>";
        } else {
            insertForm +=
                    "<a href=\"http://localhost:8080/supermed-1.0/update_yourself/" + user.getID() + "\"><button type=\"button\" " +
                            "class=\"shortButton\">Редактировать</button></a>";
        }
        return insertForm;
    }

    private String insertBranches() {
        Map<String, String> branches = dataManager.getBranches();
        if (branches == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<p>Филиал");
        stringBuilder.append("<select name=\"branch\" id=\"currentBranch\">");
        for (Map.Entry<String, String> entry : branches.entrySet()) {
            stringBuilder.append("<option value=\"").append(entry.getKey())
                    .append("\">").append(entry.getValue())
                    .append("</option>");
        }
        stringBuilder.append("</select></p>");
        return stringBuilder.toString();
    }

    private static String insertImage() {
        return " <div class=\"panel-body\">\n" +
                "              <div class=\"row\">\n" +
                "                <div class=\"col-md-3 col-lg-3 \" align=\"center\"> <img " +
                "alt=\"User Pic\" src=\"http://fn41.n.f.f.unblog.fr/files/2011/04/anonyme.jpg\" " +
                "class=\"img-circle " +
                "img-responsive\"> </div>\n" +
                "                \n" +
                "                \n" +
                "                <div class=\" col-md-9 col-lg-9 \"> ";
    }

    public static String printLoginPage() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<style>\n" +
                "form {\n" +
                "    border: 3px solid #f1f1f1;\n" +
                "}\n" +
                "\n" +
                "input[type=text], input[type=password] {\n" +
                "    width: 100%;\n" +
                "    padding: 12px 20px;\n" +
                "    margin: 8px 0;\n" +
                "    display: inline-block;\n" +
                "    border: 1px solid #ccc;\n" +
                "    box-sizing: border-box;\n" +
                "}\n" +
                "\n" +
                "button {\n" +
                "    background-color: #4CAF50;\n" +
                "    color: white;\n" +
                "    padding: 14px 20px;\n" +
                "    margin: 8px 0;\n" +
                "    border: none;\n" +
                "    cursor: pointer;\n" +
                "    width: 100%;\n" +
                "}\n" +
                "\n" +
                ".cancelbtn {\n" +
                "    width: auto;\n" +
                "    padding: 10px 18px;\n" +
                "    background-color: #f44336;\n" +
                "}\n" +
                "\n" +
                ".imgcontainer {\n" +
                "    text-align: center;\n" +
                "    margin: 24px 0 12px 0;\n" +
                "}\n" +
                "\n" +
                "img.avatar {\n" +
                "    width: 40%;\n" +
                "    border-radius: 50%;\n" +
                "}\n" +
                "\n" +
                ".container {\n" +
                "    padding: 16px;\n" +
                "}\n" +
                "\n" +
                "span.psw {\n" +
                "    float: right;\n" +
                "    padding-top: 16px;\n" +
                "}\n" +
                "\n" +
                "/* Change styles for span and cancel button on extra small screens */\n" +
                "@media screen and (max-width: 300px) {\n" +
                "    span.psw {\n" +
                "       display: block;\n" +
                "       float: none;\n" +
                "    }\n" +
                "    .cancelbtn {\n" +
                "       width: 100%;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "<body>\n" +
                "\n" +
                "<h2>Login Form</h2>\n" +
                "\n" +
                "<form  method=\"post\" action=\"\">\n" +
                "    <div class=\"container\">\n" +
                "        <label><b>Username</b></label>\n" +
                "        <input type=\"text\" placeholder=\"Enter Username\" name=\"login\" " +
                "required>\n" +
                "\n" +
                "        <label><b>Password</b></label>\n" +
                "        <input type=\"password\" placeholder=\"Enter Password\" " +
                "name=\"password\" " +
                "required>\n" +
                "\n" +
                "        <button type=\"submit\">Login</button>\n" +
                "        <input type=\"checkbox\" checked=\"checked\"> Remember me\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"container\" style=\"background-color:#f1f1f1\">\n" +
                "        <button type=\"button\" class=\"cancelbtn\">Cancel</button>\n" +
                "        <span class=\"psw\">Forgot <a href=\"#\">password?</a></span>\n" +
                "    </div>\n" +
                "</form>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
    }

    public static String printEditForm(User user) {
        String form = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form method=\"post\" action=\"\">\n" +
                "  Адресс:<br>\n" +
                "  <input type=\"text\" name=\"address\" value=\"" + user.getUserData()
                .getAddress() + "\">\n" +
                "  <br>\n" +
                "  Контактный телефон:<br>\n" +
                "  <input type=\"text\" name=\"contact_phone\" value=\"" + user.getUserData()
                .getPhoneNumber() + "\">\n" +
                "  <br>\n" +
                "  Пароль:<br>\n" +
                "  <input type=\"text\" name=\"password\" value=\"\">\n" +
                "  <br>\n" +
                "  <input type=\"submit\" value=\"Редактировать\">\n" +
                "  <br>\n" +
                "</form> \n" +
                "</body>\n" +
                "</html>\n";

        return form;
    }

    public static String printCreatePatient() {
        String createPatient = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form method=\"post\" action=\"\">\n" +
                printUserDataFormInput()+
                "  <br>Пароль:<br>\n" +
                "  <input type=\"text\" name=\"password\" value=\"\">\n" +
                "  <br>\n" +
                "  <input type=\"submit\" value=\"Создать\">\n" +
                "</form> \n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        return createPatient;
    }

    private static String printUserDataFormInput(){
        return  "  Имя:<br>\n" +
                "  <input type=\"text\" name=\"first_name\" value=\"\">\n" +
                "  <br>\n" +
                "   Отчество:<br>\n" +
                "  <input type=\"text\" name=\"middle_name\" value=\"\">\n" +
                "  <br>Фамилия:<br>\n" +
                "  <input type=\"text\" name=\"last_name\" value=\"\">\n" +
                "  <br>Дата рождения:<br>\n" +
                "  <input type=\"text\" name=\"birth_date\" value=\"\">\n" +
                "  <br>Адрес:<br>\n" +
                "  <input type=\"text\" name=\"address\" value=\"\">\n" +
                "  <br>Телефон:<br>\n" +
                "  <input type=\"text\" name=\"contact_phone\" value=\"\">\n" +
                "  <br>E-mail:<br>\n" +
                "  <input type=\"text\" name=\"email\" value=\"\">\n";
    }
    public static String printCreateEmployeePage() {
        String createUser = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<form method=\"post\" action=\"\">\n" +
                printUserDataFormInput() +
                "  <br>Роль:<br>\n" +
                "  <input type=\"text\" name=\"role\" value=\"\">\n" +
                "  <br>Должность:<br>\n" +
                "  <input type=\"text\" name=\"position\" value=\"\">\n" +
                "  <br>Отделение клиники:<br>\n" +
                "  <input type=\"text\" name=\"branch\" value=\"\">\n" +
                "  <br>Дата найма:<br>\n" +
                "  <input type=\"text\" name=\"hireDate\" value=\"\">\n" +
                "  <br>Пароль:<br>\n" +
                "  <input type=\"text\" name=\"password\" value=\"\">\n" +
                "  <br>\n" +
                "  <input type=\"submit\" value=\"Создать\">\n" +
                "</form> \n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        return createUser;
    }

    public static String printSpecChooserForPatient(Map<String, String> specialitites) {
        StringBuilder stringBuilder = new StringBuilder();
        String heading = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<select onchange=\"this.options[this.selectedIndex].value && (window.location+=" +
                "this.options[this.selectedIndex].value);\">" +
                "   <option value=\"\">Выберите специальность</option>\n";
        stringBuilder.append(heading);
        for (Map.Entry<String, String> speciality : specialitites.entrySet()) {
            stringBuilder.append("<option value=\"/").append(speciality.getKey())
                    .append("\">").append(speciality.getValue()).append("</option>\n");
        }
        stringBuilder.append("</select>\n</body>\n</html>\n ");
        return stringBuilder.toString();
    }

    public String printShecduleForUser(List<Schedule> schedules) {
        String heading = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body><table style=\"width:100%\">\n<tr><th>Время</th>";
        String ending = "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(heading);
        if (schedules == null) {
            schedules = new LinkedList<Schedule>();
        }
        for (Schedule s : schedules) {
            stringBuilder.append("<th>").append(s.getEmployee().getUserData().getLastName())
                    .append(" ").append(s.getEmployee().getUserData().getFirstName())
                    .append(" ").append(s.getEmployee().getUserData().getMiddleName())
                    .append("</th>\n");
        }
        stringBuilder.append("</tr>");
        for (int i = 8; i < 12; i++) {
            stringBuilder.append("<tr>");
            stringBuilder.append("<td>" + i + " - " + (i + 1) + "</td>");
            for (Schedule s : schedules) {
                if (isAlreadyBooked(i, s.getEvents())) {
                    stringBuilder.append("<td></td>");
                } else {
                    stringBuilder.append("<td><button onClick=\"javascript:window" +
                            ".location='../enlist?doctorID=" + s.getEmployee().getID() +
                            "&startTime=" + (i > 9 ? "" : "0") + i + "-00&endTime=" + (i > 8 ? ""
                            : "0") + (i + 1) +
                            "-00'\">Записаться</button></td>");
                }
            }

            stringBuilder.append("</tr>");
        }
        for (int i = 13; i < 17; i++) {
            stringBuilder.append("<tr>");
            stringBuilder.append("<td>" + i + " - " + (i + 1) + "</td>");
            for (Schedule s : schedules) {
                if (isAlreadyBooked(i, s.getEvents())) {
                    stringBuilder.append("<td></td>");
                } else {
                    {
                        stringBuilder.append("<td><button onClick=\"javascript:window" +
                                ".location='../enlist?doctorID=" + s.getEmployee().getID() +
                                "&startTime=" + (i > 9 ? "" : "0") + i + "-00&endTime=" + (i > 8
                                ? "" : "0") + (i + 1) +
                                "-00'\">Записаться</button></td>");
                    }
                }
            }

            stringBuilder.append("</tr>");
        }
        stringBuilder.append(ending);
        return stringBuilder.toString();
    }

    private boolean isAlreadyBooked(int hour, List<Event> events) {
        String expectedEventStartTime;
        String expectedEventEndTime;

        int startHour = hour;
        int endHour = hour + 1;
        for (Event event : events) {
            expectedEventStartTime = event.getExpectedStartTime().substring(0, 2);
            expectedEventEndTime = event.getExpectedEndTime().substring(0, 2);
            if (expectedEventStartTime.startsWith("0")) {
                expectedEventStartTime = expectedEventStartTime.substring(1);
            }
            int eventExpectedStartHour = Integer.parseInt(expectedEventStartTime);
            if (expectedEventEndTime.startsWith("0")) {
                expectedEventEndTime = expectedEventEndTime.substring(1);
            }
            int eventExpectedEndHour = Integer.parseInt(expectedEventEndTime);

            if (event.getStatus().equals(EventStatus.PLANNED)) {
                if ((eventExpectedStartHour >= startHour) && (eventExpectedEndHour <= endHour)) {
                    return true;
                }
                if ((eventExpectedStartHour < startHour) && (eventExpectedEndHour > endHour)) {
                    return true;
                }
            }
            if (event.getStatus().equals(EventStatus.DENIED)) {
                return false;
            }

        }
        return false;
    }

    public String printUsersProfile(List<User> userList) {
        String page = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n";
        for (User user : userList) {
            UserData userData = user.getUserData();
            page += "<div >\n" +
                    "<span>\n" + userData.getFirstName() + " " + userData.getMiddleName() + " "
                    + userData.getLastName() + "</span>\n" +
                    "<a href=\"/supermed-1.0/users/" + user.getID() + "\"><button type=\"button\"" +
                    " " +
                    "class=\"\">Смотреть и редактировать</button></a>" +
                    "<a href=\"/supermed-1.0/remove/" + user.getID() + "\"><button " +
                    "type=\"button\" " +
                    "class=\"\">Удалить</button></a>" +
                    "</div >\n" +
                    "</br >\n" +
                    "</br >\n";
        }
        page += "\n" +
                "</body>\n" +
                "</html>\n";
        return page;
    }

    public String printVisits(Map<Visit, User> visits, Role role) {
        String page = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(page);
        for (Map.Entry<Visit, User> v : visits.entrySet()) {
            if (v.getKey().getStatus().equals(EventStatus.PLANNED)) {
                stringBuilder.append(printPlannedVisit(v, role));
            }
            if (v.getKey().getStatus().equals(EventStatus.FINISHED)) {
                //stringBuilder.append(printFinishedVisit(v));
            }
        }
        stringBuilder.append("\n</body>\n</html>\n");

        return stringBuilder.toString();
    }

    private String printPlannedVisit(Map.Entry<Visit, User> v, Role role) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div><p>Время: ").append(v.getKey().getExpectedStartDate()).append
                ("</p>");

        stringBuilder.append("<p>" + (role.equals(Role.PATIENT) ? "Врач: " : "Пациент: "))
                .append(v.getValue().getUserData().getFirstName())
                .append(" ").append(v.getValue().getUserData().getMiddleName()).append(" ")
                .append(v.getValue().getUserData().getLastName()).append("</p>");

        stringBuilder.append("<p><button onClick=\"javascript:window" +
                ".location='../denyEvent/" + v.getKey().getEventID() + "\">Отменить</button></p>");
        stringBuilder.append("</div>");
        return stringBuilder.toString();
    }
}

package Snapshot_0_0_1;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.Validations.CommonServiceValidations;
import org.example.constants.Actions;
import org.example.constants.Headers;
import org.example.requests.services.CommonService;
import org.example.utils.TokenGenerator;
import org.example.utils.wireMock.WireMockConstants;
import org.example.utils.wireMock.WireMockUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


@Slf4j
@Tag("Regress")
@Epic("Action")
@DisplayName("Тестирование действий")
public class ActionTests {

    CommonService request = new CommonService();
    CommonServiceValidations validations = new CommonServiceValidations();
    Response response;
    String token;

    @BeforeAll
    static void setUp() {
        WireMockUtil.getInstance(WireMockConstants.PORT_MOCK);
    }

    @BeforeEach
    void reset() {
        WireMockUtil.resetAll();
        token = TokenGenerator.generateToken();
    }

    @AfterEach
    void tearDown() {
        step("Выход из учетной записи");
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.LOGOUT);
    }

    @Step("{description}")
    protected void step(String description) {
        log.info("Шаг {}", description);
    }

    @Test
    @DisplayName("Успешное действие")
    public void successAction() {
        step("Аутентификация с валидным токеном и действием \"" + Actions.LOGIN + "\"");
        WireMockUtil.stubPost(WireMockConstants.LOGIN_ENDPOINT_MOCK, WireMockConstants.ACCESS_STATUS_MOCK,
                WireMockConstants.BODY_SUCCESS_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.LOGIN);

        step("Отправка запроса на действие с токеном, прошедшим аутентификацию и помещенным в хранилище");
        WireMockUtil.stubPost(WireMockConstants.ACTION_ENDPOINT_MOCK, WireMockConstants.ACCESS_STATUS_MOCK,
                WireMockConstants.BODY_SUCCESS_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.ACTION);

        validations.successAction(response);
    }


    @ParameterizedTest
    @DisplayName("Действие с невалидным токеном")
    @MethodSource("invalidTokensProvider")
    public void invalidTokenLogin(String description, String methodToken) {
        step("Аутентификация с валидным токеном и действием \"" + Actions.LOGIN + "\"");
        WireMockUtil.stubPost(WireMockConstants.LOGIN_ENDPOINT_MOCK, WireMockConstants.ACCESS_STATUS_MOCK,
                WireMockConstants.BODY_SUCCESS_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.LOGIN);

        step(description);
        WireMockUtil.stubPost(WireMockConstants.ACTION_ENDPOINT_MOCK, WireMockConstants.BAD_REQUEST_STATUS_MOCK,
                WireMockConstants.BODY_ERROR_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + methodToken + "&action=" + Actions.ACTION);

        validations.invalidToken(response);
    }

    static Stream<Arguments> invalidTokensProvider() {
        return Stream.of(
                Arguments.of("С пустым токеном", ""),
                Arguments.of("Токен с буквой в нижнем регистре", "6448817BFA4DC83D4A44BC6DB8B9746a"),
                Arguments.of("Токен с буквой, не входящей в диапозон A-F", "6448817BFA4DC83D4A44BC6DB8B9746G"),
                Arguments.of("Токен из 31 символа", "6448817BFA4DC83D4A44BC6DB8B9746"),
                Arguments.of("Токен из 33 символов", "6448817BFA4DC83D4A44BC6DB8B97467A")
        );
    }


    @Test
    @DisplayName("Действие с недействительным токеном")
    public void unavailableTokenLogin() {
        step("Аутентификация с валидным токеном и действием \"" + Actions.LOGIN + "\"");
        WireMockUtil.stubPost(WireMockConstants.LOGIN_ENDPOINT_MOCK, WireMockConstants.ACCESS_STATUS_MOCK,
                WireMockConstants.BODY_SUCCESS_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.LOGIN);

        step("Отправка запроса на действие с токеном, непрошедшим аутентификацию и помещенным в хранилище");
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + TokenGenerator.generateToken() + "&action=" + Actions.ACTION);

        validations.unavailableTokenInAction(response);
    }


    @ParameterizedTest
    @DisplayName("Действие с недопустимым параметром \"action\"")
    @MethodSource("invalidActionAction")
    public void invalidActionAction(String description, String action) {
        step("Аутентификация с валидным токеном и действием \"" + Actions.LOGIN + "\"");
        WireMockUtil.stubPost(WireMockConstants.LOGIN_ENDPOINT_MOCK, WireMockConstants.ACCESS_STATUS_MOCK,
                WireMockConstants.BODY_SUCCESS_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.LOGIN);

        step(description);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + action);

        validations.invalidAction(response);
    }

    static Stream<Arguments> invalidActionAction() {
        return Stream.of(
                Arguments.of("С пустым действием", ""),
                Arguments.of("С недопустимым действием", Actions.REGISTER)
        );
    }


    @Test
    @DisplayName("Повторное действие после выхода из учетной записи")
    public void testLogin() {
        step("Аутентификация с валидным токеном и действием \"" + Actions.LOGIN + "\"");
        WireMockUtil.stubPost(WireMockConstants.LOGIN_ENDPOINT_MOCK, WireMockConstants.ACCESS_STATUS_MOCK,
                WireMockConstants.BODY_SUCCESS_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.LOGIN);

        step("Отправка запроса на действие с токеном, прошедшим аутентификацию и помещенным в хранилище");
        WireMockUtil.stubPost(WireMockConstants.ACTION_ENDPOINT_MOCK, WireMockConstants.ACCESS_STATUS_MOCK,
                WireMockConstants.BODY_SUCCESS_MOCK);
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.ACTION);

        step("Выход из учетной записи");
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.LOGOUT);

        step("Повторная отправка запроса на действие после выхода из учетной записи");
        response = request.postRequest(Headers.getX_API_KEY(), "token=" + token + "&action=" + Actions.ACTION);

        validations.unavailableTokenInAction(response);


    }
}

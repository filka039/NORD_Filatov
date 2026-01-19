package org.example.Validations;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public abstract class ValidationBase {

    @Step("{description}")
    protected void step(String description) {
        log.info("Шаг {}", description);
    }

    protected void assertNotBlank(String responseBody) {
        step("Проверка того, что тело ответа не пустое и не null");
        assertNotNull(responseBody, "Ответ не должен быть null");
        assertFalse(responseBody.isEmpty(), "Ответ не должен быть пустым");
    }
}

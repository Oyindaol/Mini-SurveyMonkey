package org.example;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

public class MiniMonkeySurveyApplicationTest {

    @Test
    public void testMain() {
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            MiniMonkeySurveyApplication.main(new String[]{});
            mockedSpringApplication.verify(() -> SpringApplication.run(MiniMonkeySurveyApplication.class), times(1));
        }
    }
}
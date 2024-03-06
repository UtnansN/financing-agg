package com.example.financingagg.integration.provider;

import com.example.financingagg.integration.dto.Application;
import com.example.financingagg.integration.dto.ApplicationStatus;
import com.example.financingagg.integration.dto.OfferData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ApplicationsService.class, ApplicationsServiceTest.RetryConfig.class})
class ApplicationsServiceTest {

    private static final String URL = "url";
    private static final String COMPANY_ID = "MockCompany";

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationsService applicationsService;

    @Test
    void createApplication_shouldReturnUUIDOnSuccess() {
        // Arrange
        var applicationRequestDto = new Object();

        var uuid = UUID.randomUUID();
        var application = Application.builder().id(uuid).build();

        var responseEntity = new ResponseEntity<>(application, HttpStatus.CREATED);
        when(restTemplate.postForEntity(URL, applicationRequestDto, Application.class)).thenReturn(responseEntity);

        // Act
        var returnUUID = applicationsService.createApplication(URL, applicationRequestDto);

        // Assert
        assertThat(returnUUID).isEqualTo(uuid);
    }

    @Test
    void getFinalizedOffer_successOnRetriedCalls_returnsOffer() {
        // Arrange
        var applicationId = UUID.randomUUID();

        var draftApplication = draft(applicationId);
        var application = processed(applicationId);
        var offer = application.getOffer();

        // Success on 3rd fetch
        when(restTemplate.getForEntity("%s/%s".formatted(URL, applicationId), Application.class))
                .thenReturn(ResponseEntity.ok(draftApplication), ResponseEntity.ok(draftApplication), ResponseEntity.ok(application));

        // Act
        var result = applicationsService.getFinalizedOffer(URL, COMPANY_ID, applicationId);

        // Assert
        verify(restTemplate, times(3)).getForEntity("%s/%s".formatted(URL, applicationId), Application.class);

        assertThat(result.getCompany()).isEqualTo(COMPANY_ID);
        assertThat(result.getMessage()).isNull();
        assertThat(result.getOfferData()).isEqualTo(offer);
    }

    @Test
    void getFinalizedOffer_noApplicationId_errorMessage() {
        // Arrange

        // Act
        var result = applicationsService.getFinalizedOffer(URL, COMPANY_ID, null);

        // Assert
        assertThat(result.getCompany()).isEqualTo(COMPANY_ID);
        assertThat(result.getMessage()).isEqualTo("There was an issue applying for the offer.");
        assertThat(result.getOfferData()).isNull();
    }

    @Test
    void getFinalizedOffer_noResponseBody_errorMessage() {
        // Arrange
        var applicationId = UUID.randomUUID();

        when(restTemplate.getForEntity("%s/%s".formatted(URL, applicationId), Application.class))
                .thenReturn(ResponseEntity.ok(null));

        // Act
        var result = applicationsService.getFinalizedOffer(URL, COMPANY_ID, applicationId);

        // Assert
        assertThat(result.getCompany()).isEqualTo(COMPANY_ID);
        assertThat(result.getMessage()).isEqualTo("There was an issue applying for the offer.");
        assertThat(result.getOfferData()).isNull();
    }

    @Test
    void getFinalizedOffer_badResponseCode_errorMessage() {
        // Arrange
        var applicationId = UUID.randomUUID();

        when(restTemplate.getForEntity("%s/%s".formatted(URL, applicationId), Application.class))
                .thenReturn(ResponseEntity.internalServerError().body(processed(applicationId)));

        // Act
        var result = applicationsService.getFinalizedOffer(URL, COMPANY_ID, applicationId);

        // Assert
        assertThat(result.getCompany()).isEqualTo(COMPANY_ID);
        assertThat(result.getMessage()).isEqualTo("There was an issue applying for the offer.");
        assertThat(result.getOfferData()).isNull();
    }

    @Test
    void getFinalizedOffer_timeout_errorMessage() {
        // Arrange
        var applicationId = UUID.randomUUID();

        var draftApplication = draft(applicationId);

        // Success on 3rd fetch
        when(restTemplate.getForEntity("%s/%s".formatted(URL, applicationId), Application.class)).thenReturn(ResponseEntity.ok(draftApplication));

        // Act
        var result = applicationsService.getFinalizedOffer(URL, COMPANY_ID, applicationId);

        // Assert
        // Configured to try 5 times
        verify(restTemplate, times(5)).getForEntity("%s/%s".formatted(URL, applicationId), Application.class);

        assertThat(result.getCompany()).isEqualTo(COMPANY_ID);
        assertThat(result.getMessage()).isEqualTo("Offer request to company %s timed out".formatted(COMPANY_ID));
        assertThat(result.getOfferData()).isNull();
    }

    private Application draft(UUID applicationId) {
        return Application.builder()
                .id(applicationId)
                .status(ApplicationStatus.DRAFT)
                .build();
    }

    private Application processed(UUID applicationId) {
        return Application.builder()
                .id(applicationId)
                .status(ApplicationStatus.PROCESSED)
                .offer(OfferData.builder()
                        .annualPercentageRate(BigDecimal.valueOf(12))
                        .numberOfPayments(1)
                        .totalRepaymentAmount(BigDecimal.valueOf(1010))
                        .firstRepaymentDate(LocalDate.of(2025, 1, 1))
                        .build())
                .build();
    }

    @Configuration
    @EnableRetry
    @Import(ApplicationsService.class)
    public static class RetryConfig {}
}
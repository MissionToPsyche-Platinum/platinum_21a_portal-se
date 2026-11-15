
package com.psyche.portal_backend.repository;

import com.psyche.portal_backend.model.GameUploadRequest;
import com.psyche.portal_backend.model.RequestStatus;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameUploadPersistenceIntegrationTest {

    @Autowired
    private GameUploadRequestRepository requestRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void tenRequestsPersistWithCorrectIdsAndStatuses() {

        List<Long> requestIds = new ArrayList<>();

        //create and save 10 pending requests
        for (int i = 1; i <= 10; i++) {

            GameUploadRequest request =
                new GameUploadRequest(null, null);

            request.setGameJson(
                "{\"title\":\"sprint 12 test Game " + i + "\"}"
            );

            GameUploadRequest saved =
                requestRepository.save(request);

            assertTrue(saved.getRequestId() > 0);
            assertEquals(
                RequestStatus.PENDING,
                saved.getRequestStatus()
            );

            requestIds.add(saved.getRequestId());
        }

        //write changes to PostgreSQL
        entityManager.flush();

        //remove entities from JPA's memory cache
        entityManager.clear();

        //retrieve all 10 requests from PostgreSQL
        for (int i = 0; i < requestIds.size(); i++) {

            Long id = requestIds.get(i);

            GameUploadRequest reloaded =
                requestRepository.findById(id).orElseThrow();

            assertEquals(id.longValue(), reloaded.getRequestId());

            assertEquals(
                RequestStatus.PENDING,
                reloaded.getRequestStatus()
            );

            assertTrue(
                reloaded.getGameJson().contains(
                    "sprint 12 test Game " + (i + 1)
                )
            );
        }
    }

    @Test
    void approvedAndDeniedStatusesPersistAfterReload() {

        GameUploadRequest approved =
            new GameUploadRequest(null, null);

        approved.setGameJson("{\"title\":\"Approved Game\"}");

        GameUploadRequest denied =
            new GameUploadRequest(null, null);

        denied.setGameJson("{\"title\":\"Denied Game\"}");

        //save requests
        approved = requestRepository.save(approved);
        denied = requestRepository.save(denied);

        Long approvedId = approved.getRequestId();
        Long deniedId = denied.getRequestId();

        //change statuses
        approved.setRequestStatus(RequestStatus.APPROVED);
        denied.setRequestStatus(RequestStatus.DENIED);

        requestRepository.save(approved);
        requestRepository.save(denied);

        //clear JPA persistence context
        entityManager.flush();
        entityManager.clear();

        //reload from PostgreSQL
        GameUploadRequest reloadedApproved =
            requestRepository.findById(approvedId).orElseThrow();

        GameUploadRequest reloadedDenied =
            requestRepository.findById(deniedId).orElseThrow();

        assertEquals(
            RequestStatus.APPROVED,
            reloadedApproved.getRequestStatus()
        );

        assertEquals(
            RequestStatus.DENIED,
            reloadedDenied.getRequestStatus()
        );
    }
}

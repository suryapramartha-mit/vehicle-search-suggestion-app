package com.vehicle.suggestion.app.repository;

import com.vehicle.suggestion.app.dto.OperationSearchResult;
import com.vehicle.suggestion.app.entity.Operations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operations, Long>, JpaSpecificationExecutor<Operations> {
    @Query("""
        SELECT new com.vehicle.suggestion.app.dto.OperationSearchResult(
            o.id, o.brand, o.model, o.engine, o.yearStart, o.yearEnd,
            o.distanceStart, o.distanceEnd, o.name, o.approxCost, o.description, o.time
        )
        FROM Operations o
        WHERE (:brand IS NULL OR LOWER(o.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
          AND (:model IS NULL OR LOWER(o.model) LIKE LOWER(CONCAT('%', :model, '%')))
          AND (:engine IS NULL OR LOWER(o.engine) = LOWER(:engine))
          AND (:yearStart IS NULL OR o.yearStart = :yearStart)
          AND (:yearEnd IS NULL OR o.yearEnd = :yearEnd)
          AND (:distanceStart IS NULL OR o.distanceStart >= :distanceStart)
          AND (:distanceEnd IS NULL OR o.distanceEnd <= :distanceEnd)
    """)
    Page<OperationSearchResult> searchOperations(@Param("brand") String brand,
                                                 @Param("model") String model,
                                                 @Param("engine") String engine,
                                                 @Param("yearStart") Integer yearStart,
                                                 @Param("yearEnd") Integer yearEnd,
                                                 @Param("distanceStart") Double distanceStart,
                                                 @Param("distanceEnd") Double distanceEnd,
                                                 Pageable pageable);

    @Query("""
            SELECT new com.vehicle.suggestion.app.dto.OperationSearchResult(
              o.id, o.brand, o.model, o.engine, o.yearStart, o.yearEnd,
                o.distanceStart, o.distanceEnd, o.name, o.approxCost, o.description, o.time)
            FROM Operations o
            WHERE (:brand IS NULL OR LOWER(o.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
              AND (:model IS NULL OR LOWER(o.model) LIKE LOWER(CONCAT('%', :model, '%')))
              AND (:engine IS NULL OR LOWER(o.engine) = LOWER(:engine))
              AND (:makeYear IS NULL OR :makeYear BETWEEN o.yearStart AND o.yearEnd)
              ORDER BY o.approxCost ASC    
    """)
    List<OperationSearchResult> suggestOperations(
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("engine") String engine,
            @Param("makeYear") Integer makeYear
    );

}

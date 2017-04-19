package za.ac.sanbi.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import za.ac.sanbi.domain.NeoStudy;
import za.ac.sanbi.domain.NeoUserDetails;

/**
 * Created by hocine on 2017/04/17.
 */
public interface UploadRepository extends PagingAndSortingRepository<NeoStudy, Long> {}

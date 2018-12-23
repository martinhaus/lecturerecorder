package com.martinhaus.lecture_recorder.repositories;

import com.martinhaus.lecture_recorder.model.RecordingDownload;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordingDownloadRepository extends CrudRepository<RecordingDownload, Long> {
    RecordingDownload findByUuid(String uuid);
}

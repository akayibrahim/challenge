package org.chl.intf;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by ibrahim on 15/07/2018.
 */
public interface IProofService {
    String uploadImage(MultipartFile file, String challengeId, String memberId, Boolean wide) throws IOException;
}

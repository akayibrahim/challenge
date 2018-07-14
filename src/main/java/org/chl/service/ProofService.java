package org.chl.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.chl.intf.IProofService;
import org.chl.model.Challenge;
import org.chl.model.JoinAttendance;
import org.chl.model.Proof;
import org.chl.repository.JoinAndProofAttendanceRepository;
import org.chl.repository.ProofRepository;
import org.chl.util.Constant;
import org.chl.util.Exception;
import org.chl.util.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by ibrahim on 07/15/2018.
 */
@Service
public class ProofService implements IProofService {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private JoinAndProofAttendanceRepository joinAndProofRepo;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ProofRepository proofRepository;

    @Override
    public String uploadImage(MultipartFile file, String challengeId, String memberId) throws IOException {
        String imageFileId = "";
        JoinAttendance joinAttendance = joinAndProofRepo.findByChallengeIdAndMemberId(challengeId, memberId);
        if (joinAttendance.getProof())
            Exception.throwCannotAddProofAgain();
        DBObject metaData = new BasicDBObject();
        metaData.put("challengeId", challengeId);
        metaData.put("memberId", memberId);
        InputStream inputStream = file.getInputStream();
        metaData.put("type", "image");
        imageFileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(), metaData).toString();
        addProof(challengeId, memberId, imageFileId);
        return "Done " + imageFileId;
    }

    private void addProof(String challengeId, String memberId, String objectId) {
        JoinAttendance joinAttendance = joinAndProofRepo.findByChallengeIdAndMemberId(challengeId, memberId);
        joinAttendance.setJoin(false);
        joinAttendance.setProof(true);
        joinAndProofRepo.save(joinAttendance);
        Proof proof = new Proof();
        proof.setProofObjectId(objectId);
        proof.setChallengeId(challengeId);
        proof.setMemberId(memberId);
        proof.setInsertDate(new Date());
        proofRepository.save(proof);
        Challenge challenge = challengeService.getChallengeById(challengeId);
        if (!memberId.equals(challenge.getChallengerId()))
            activityService.createActivity(Mappers.prepareActivity(joinAttendance.getId(), challengeId, memberId, challenge.getChallengerId(), Constant.ACTIVITY.PROOF));
    }
}

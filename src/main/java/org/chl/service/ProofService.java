package org.chl.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.chl.intf.IProofService;
import org.chl.model.Challenge;
import org.chl.model.Proof;
import org.chl.repository.ProofRepository;
import org.chl.util.Constant;
import org.chl.util.DateUtil;
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
    private ActivityService activityService;
    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ProofRepository proofRepository;

    @Override
    public String uploadImage(MultipartFile file, String challengeId, String memberId, Boolean wide) throws IOException {
        Challenge challenge = challengeService.getChallengeById(challengeId);
        challenge.getJoinAttendanceList().stream().filter(chl -> chl.getMemberId().equals(memberId)).findFirst()
                .ifPresent(joinAttendance -> {
                    if (joinAttendance.getProof())
                        Exception.throwCannotAddProofAgain();
                    DBObject metaData = new BasicDBObject();
                    metaData.put("challengeId", challengeId);
                    metaData.put("memberId", memberId);
                    InputStream inputStream = null;
                    try {
                        inputStream = file.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String type[] = file.getContentType().split("/");
                    metaData.put("type", type[0]);
                    String imageFileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(), metaData).toString();
                    joinAttendance.setJoin(false);
                    joinAttendance.setProof(true);
                    joinAttendance.setProvedWithImage("image".equals(type[0]) ? true : false);
                    joinAttendance.setWide(wide);
                    addProof(challengeId, memberId, imageFileId);
                });
        challengeService.save(challenge);
        return "Done ";
    }

    private void addProof(String challengeId, String memberId, String objectId) {
        Proof proof = new Proof();
        proof.setProofObjectId(objectId);
        proof.setChallengeId(challengeId);
        proof.setMemberId(memberId);
        proof.setInsertDate(DateUtil.getCurrentDatePlusThreeHour());
        proofRepository.save(proof);
        Challenge challenge = challengeService.getChallengeById(challengeId);
        if (!memberId.equals(challenge.getChallengerId()))
            activityService.createActivity(Mappers.prepareActivity(null, challengeId, memberId, challenge.getChallengerId(), Constant.ACTIVITY.PROOF));
    }
}

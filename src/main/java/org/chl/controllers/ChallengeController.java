package org.chl.controllers;

import org.chl.models.Challenge;
import org.chl.models.ChallengeAttendance;
import org.chl.models.Like;
import org.chl.services.ChallengeService;
import org.chl.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChallengeController {
    @Autowired
    private ChallengeService chlService;

    public ChallengeService getChlService() {
        return chlService;
    }

    public void setChlService(ChallengeService chlService) {
        this.chlService = chlService;
    }

    @RequestMapping(value = "/addChl")
    public String addChallenge(@RequestBody Challenge chl) {
        Challenge challenge = chlService.addChallenge(chl);
        if(chl.getType().equals("private") && chl.getAttendanceRequestList().size() != 0)
            for (String attandance: chl.getAttendanceRequestList()
                 ) {
                ChallengeAttendance  chlAtt = new ChallengeAttendance();
                chlAtt.setMemberId(attandance);
                chlAtt.setAskAcceptOrReject("true");
                chlAtt.setChallengeId(chl.getId());
                chlService.joinOrInviteForAttendaceToChallenge(chlAtt);
                sendNotificationToMemberForAttendance();
            }
        return "1";
    }

    private void sendNotificationToMemberForAttendance() {
    }

    @RequestMapping(value = "/getChls")
    public Iterable<Challenge> getChallenges() {
        Iterable<Challenge> challenges = chlService.getChallenges();
        for (Challenge chl: challenges) {
            if(Constant.TYPE.PRIVATE.equals(chl.getType())) {
                List<String> attendanceAcceptList = new ArrayList<>();
                for (String request: chl.getAttendanceRequestList()) {
                    ChallengeAttendance chlAtt = chlService.getAcceptAttendanceMemberId(request, chl.getId());
                    if(Constant.ANSWER.ACCEPT.getAnswer().equals(chlAtt.getAcceptOrReject()))
                        attendanceAcceptList.add(chlAtt.getMemberId());
                }
                chl.setAttendanceAcceptList(attendanceAcceptList);
            } else if(Constant.TYPE.PUBLIC.equals(chl.getType())) {
                List<ChallengeAttendance> chlAttList =  chlService.getJoinsToChallenge(chl.getId());
                List<String> joinList = new ArrayList<>();
                for (ChallengeAttendance chlAtt: chlAttList) {
                    joinList.add(chlAtt.getMemberId());
                }
                chl.setJoinToChallengeList(joinList);
            }
        }
        return challenges;
    }

    @RequestMapping(value = "/getChallengeLikes")
    public Iterable<Like> getChallengeLikes(String challengeId) {
        Iterable<Like> likes = chlService.getChallangeLikes(challengeId);
        return likes;
    }

    @RequestMapping(value = "/likeChallenge")
    public void likeChallenge(@RequestBody Like like) {
        chlService.likeChallange(like);
    }

    @RequestMapping(value = "/joinToChallenge")
    public void joinToChallenge(@RequestBody ChallengeAttendance chlAtt) {
        chlService.joinOrInviteForAttendaceToChallenge(chlAtt);
    }

    @RequestMapping(value = "/getAttendanceOfChls")
    public List<ChallengeAttendance> getAttendanceOfChls() {
        return chlService.getAttendanceOfChls();
    }

    @RequestMapping(value = "/acceptOrRejectChl")
    public void acceptOrRejectChl(@RequestBody ChallengeAttendance chlAtt) {
        chlService.acceptOrRejectChl(chlAtt);
    }
}

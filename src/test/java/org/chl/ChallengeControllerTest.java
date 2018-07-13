package org.chl;

import org.chl.controller.ChallengeController;
import org.chl.model.*;
import org.chl.repository.*;
import org.chl.service.ChallengeService;
import org.chl.service.MemberService;
import org.chl.service.NotificationService;
import org.chl.util.Constant;
import org.chl.util.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ChallengeService.class)
@SpringBootTest
public class ChallengeControllerTest {
    public static final String VERSUS_CHALLENGER_ID = "5a81b0f0f8b8e43e70325d3d";
    public static final String CHALLENGER_ID = "5a81b0f0f8b8e43e70325d3d";
    public static final String CHALLENGE_ID = "5a81b0f0f8b8e43e70325d3d";
    public static final String CHALLENGER_FB_ID = "HIOANSJDHAODPASMNDASUDASL";
    public static final String CHALLANGER_NAME = "ibrahim akay";
    public static final String memberId = "5a81b0f0f8b8e43e70325d3d";
    
    @MockBean
    private ChallengeService challengeService;

    @MockBean
    private ChallengeRepository challengeRepository;

    @MockBean
    private SupportRepository likeRepo;

    @MockBean
    private VersusAttendanceRepository versusRepo;

    @MockBean
    private JoinAndProofAttendanceRepository joinAndProofRepo;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    private ChallengeController challengeController;

    @Before
    public void setup() throws Exception {
        List<Challenge> chls = new ArrayList<>();
        VersusChallenge versusChl = prepareVersusChlModel();
        chls.add(versusChl);
        Mockito.when(challengeController.getChallenges(memberId)).thenReturn(chls);
    }

    @Test
    public void verifyAddVersusChl() throws  Exception{
        challengeController.addVersusChallenge(prepareVersusChlModel());
    }

    @Test
    public void verifyAddJoinChl() throws  Exception{
        challengeController.addJoinChallenge(prepareJoinChlModel());
    }

    @Test
    public void verifyAddSelfChl() throws  Exception{
        challengeController.addSelfChallenge(prepareSelfChlModel());
    }

    @Test
    public void verifyLikeChallenge() throws  Exception{
        Support support = new Support();
        support.setChallengeId(VERSUS_CHALLENGER_ID);
        support.setDate(new Date());
        support.setMemberId(memberId);
        challengeController.likeChallenge(support);
    }

    @Test
    public void verifyGetChallenges() throws  Exception{
        Iterable<Challenge> found =  challengeController.getChallenges(memberId);
        for (Challenge chl : found) {
            assertThat(chl.getChallengerId()).isEqualTo(VERSUS_CHALLENGER_ID);
            assertThat(chl.getChallengerFBId()).isEqualTo(CHALLENGER_FB_ID);
            assertThat(chl.getName()).isEqualTo(CHALLANGER_NAME);
            // TODO
        }
    }

    @Test
    public void verifyUpdateProgressOrDoneForSelf() throws  Exception{
        challengeController.updateProgressOrDoneForSelf(CHALLENGE_ID, "20", true);
    }

    @Test
    public void verifyUpdateResultsOfVersus() throws  Exception{
        challengeController.updateResultsOfVersus(CHALLENGE_ID, "3", "2");
    }

    @Test
    public void verifyDeleteChallenge() throws  Exception{
        challengeController.deleteChallenge(CHALLENGE_ID);
    }

    @Test
    public void verifyJoinToChallenge() throws  Exception{
        JoinToChallenge att = new JoinToChallenge();
        att.setMemberId(memberId);
        att.setChallengeId(CHALLENGE_ID);
        att.setJoin(true);
        challengeController.joinToChallenge(att);
    }

    @Test
    public void verifyAcceptOrRejectChl() throws  Exception{
        VersusAttendance att = new VersusAttendance();
        att.setMemberId(memberId);
        att.setFacebookID(CHALLENGER_FB_ID);
        att.setChallengeId(CHALLENGE_ID);
        att.setSecondTeamMember(false);
        att.setFirstTeamMember(true);
        att.setAccept(true);
        challengeController.acceptOrRejectChl(att);
    }

    @Test
    public void verifyCommentToChallange() throws  Exception{
        TextComment comment = new TextComment();
        comment.setComment("wonderfull!");
        comment.setChallengeId(CHALLENGE_ID);
        comment.setDate(new Date());
        comment.setMemberId(memberId);
        challengeController.commentToChallange(comment);
    }

    private SelfChallenge prepareSelfChlModel() {
        SelfChallenge selfChl = new SelfChallenge();
        selfChl.setChallengerFBId("HIOANSJDHAODPASMNDASUDASL");
        selfChl.setChallengerId("lkasjdaiskdalsdsaid");
        selfChl.setChlDate(new Date());
        selfChl.setDone(false);
        selfChl.setFirstTeamCount("0");
        selfChl.setSecondTeamCount("0");
        selfChl.setName("ibrahim akay");
        selfChl.setType(Constant.TYPE.SELF);
        selfChl.setSubject(Subject.READING.toString());
        selfChl.setThinksAboutChallenge("read 10 book!");
        selfChl.setUntilDateStr("Last 14 days!");
        return selfChl;
    }
    private JoinAndProofChallenge prepareJoinChlModel() {
        JoinAndProofChallenge joinChl = new JoinAndProofChallenge();
        List<JoinAttendance> joinAttList = new ArrayList<>();
        JoinAttendance joinAtt = new JoinAttendance();
        joinAtt.setChallenger(true);
        joinAtt.setJoin(false);
        joinAtt.setProof(false);
        joinAtt.setChallengeId("jbasdıasndnuasdıjasmd");
        joinAtt.setFacebookID("kdnıashasufhasuşfa");
        joinAtt.setMemberId("njaksdnaslfşasfas");
        joinAttList.add(joinAtt);
        JoinAttendance joinAttOne = new JoinAttendance();
        joinAttOne.setChallenger(false);
        joinAttOne.setJoin(false);
        joinAttOne.setProof(false);
        joinAttOne.setChallengeId("jbasdıasndnuasdıjasmd");
        joinAttOne.setFacebookID("kdnıashasufhasuşfa");
        joinAttOne.setMemberId("njaksdnaslfşasfas");
        joinAttList.add(joinAttOne);
        JoinAttendance joinAttTwo = new JoinAttendance();
        joinAttTwo.setChallenger(false);
        joinAttTwo.setJoin(false);
        joinAttTwo.setProof(false);
        joinAttTwo.setChallengeId("jbasdıasndnuasdıjasmd");
        joinAttTwo.setFacebookID("kdnıashasufhasuşfa");
        joinAttTwo.setMemberId("njaksdnaslfşasfas");
        joinAttList.add(joinAttTwo);
        joinChl.setJoinAttendanceList(joinAttList);
        joinChl.setChallengerFBId("HIOANSJDHAODPASMNDASUDASL");
        joinChl.setChallengerId("lkasjdaiskdalsdsaid");
        joinChl.setChlDate(new Date());
        joinChl.setDone(false);
        joinChl.setFirstTeamCount("1");
        joinChl.setSecondTeamCount("2");
        joinChl.setName("ibrahim akay");
        joinChl.setType(Constant.TYPE.PUBLIC);
        joinChl.setSubject(Subject.READING.toString());
        joinChl.setThinksAboutChallenge("read 10 book!");
        joinChl.setUntilDateStr("Last 14 days!");
        return joinChl;
    }

    private VersusChallenge prepareVersusChlModel() {
        VersusChallenge versusChl = new VersusChallenge();
        List<VersusAttendance> versusAttendanceList = new ArrayList<>();
        VersusAttendance attendanceOne = new VersusAttendance();
        attendanceOne.setAccept(false);
        attendanceOne.setFirstTeamMember(true);
        attendanceOne.setSecondTeamMember(false);
        attendanceOne.setChallengeId("mıjansduıekjasıdjasiod");
        attendanceOne.setFacebookID("daısdjasjdbasıdjasl");
        attendanceOne.setMemberId("aösdnasşdmkaslndas");
        versusAttendanceList.add(attendanceOne);
        VersusAttendance attendanceTwo = new VersusAttendance();
        attendanceTwo.setAccept(false);
        attendanceTwo.setFirstTeamMember(true);
        attendanceTwo.setSecondTeamMember(false);
        attendanceTwo.setChallengeId("mıjansduıekjasıdjasiod");
        attendanceTwo.setFacebookID("klınadasşdjsa");
        attendanceTwo.setMemberId("kjahsdşasdka.s");
        versusAttendanceList.add(attendanceTwo);
        VersusAttendance attendanceThree = new VersusAttendance();
        attendanceThree.setAccept(false);
        attendanceThree.setFirstTeamMember(false);
        attendanceThree.setSecondTeamMember(true);
        attendanceThree.setChallengeId("mıjansduıekjasıdjasiod");
        attendanceThree.setFacebookID("daısdjasjdbasıdjasl");
        attendanceThree.setMemberId("aösdnasşdmkaslndas");
        versusAttendanceList.add(attendanceThree);
        VersusAttendance attendanceFour = new VersusAttendance();
        attendanceFour.setAccept(false);
        attendanceFour.setFirstTeamMember(false);
        attendanceFour.setSecondTeamMember(true);
        attendanceFour.setChallengeId("mıjansduıekjasıdjasiod");
        attendanceFour.setFacebookID("sdgfgdhghsdfga");
        attendanceFour.setMemberId("asdasdasdasdas");
        versusAttendanceList.add(attendanceFour);
        versusChl.setVersusAttendanceList(versusAttendanceList);
        versusChl.setChallengerFBId(CHALLENGER_FB_ID);
        versusChl.setChallengerId(VERSUS_CHALLENGER_ID);
        versusChl.setChlDate(new Date());
        versusChl.setDone(false);
        versusChl.setFirstTeamCount("2");
        versusChl.setSecondTeamCount("2");
        versusChl.setName(CHALLANGER_NAME);
        versusChl.setType(Constant.TYPE.PRIVATE);
        versusChl.setSubject(Subject.READING.toString());
        versusChl.setThinksAboutChallenge("read 10 book!");
        versusChl.setUntilDateStr("Last 14 days!");
        return versusChl;
    }

}

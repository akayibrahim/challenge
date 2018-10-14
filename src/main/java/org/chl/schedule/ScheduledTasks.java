package org.chl.schedule;

import org.chl.model.Challenge;
import org.chl.repository.ChallengeRepository;
import org.chl.service.ActivityService;
import org.chl.util.Constant;
import org.chl.util.DateUtil;
import org.chl.util.Mappers;
import org.chl.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ibrahim on 09/12/2017.
 */
@EnableScheduling
@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    ActivityService activityService;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void scheduleTaskWithFixedRate() {
        // logger.info("Fixed Rate Task :: Execution Time - {}", dateFormat.format(new Date()) );
        // for notification : before challenge ending, warm for enter score or proof challenge.
        List<Challenge> challengeList = challengeRepository.findUpcomingChallenges(DateUtil.addHours(new Date(), 11),
                DateUtil.addHours(new Date(), 12), false);
        challengeList.stream().forEach(this::createActivityForUpcoming);
        // for notification : notify for time's up.
        List<Challenge> timesUpChallengeList = challengeRepository.findUpcomingChallenges(DateUtil.addHours(new Date(), -1),
                DateUtil.addHours(new Date(), 0), true);
        timesUpChallengeList.stream()
                .filter(chl -> Util.isTimesUp(chl.getType(), chl.getDone(), chl.getHomeWin(), chl.getAwayWin(),
                        chl.getJoined(), chl.getProofedByChallenger(), chl.getDateOfUntil()))
                .forEach(this::createActivityForTimesUp);
    }

    @Scheduled(fixedDelay = 2000)
    public void scheduleTaskWithFixedDelay() {
        // logger.info("Fixed Delay Task :: Execution Time - {}", dateFormat.format(new Date()));
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            logger.error("Ran into an error {}", ex);
            throw new IllegalStateException(ex);
        }
    }

    @Scheduled(fixedRate = 2000, initialDelay = 5000)
    public void scheduleTaskWithInitialDelay() {
        // logger.info("Fixed Rate Task with Initial Delay :: Execution Time - {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        // logger.info("Cron Task :: Execution Time - {}", dateFormat.format(new Date()));
    }

    private void createActivityForUpcoming(Challenge challenge) {
        createActivity(challenge, Constant.ACTIVITY.UPCOMING_WARMING);
    }

    private void createActivityForTimesUp(Challenge challenge) {
        createActivity(challenge, Constant.ACTIVITY.TIMES_UP);
    }

    private void createActivity(Challenge challenge, Constant.ACTIVITY type) {
        logger.info(challenge.getId());
        if (challenge.isJoin()) {
            challenge.getJoinAttendanceList().stream()
                    .filter(join -> join.getJoin() != null && join.getJoin() && !join.getProof())
                    .forEach(attendance -> {
                        activityService.createActivity(Mappers.prepareActivity(null, challenge.getId(), challenge.getChallengerId(),
                                attendance.getMemberId(), type));
                    });
        } else if (challenge.isVersus()) {
            challenge.getVersusAttendanceList().stream()
                    .forEach(attendance -> {
                        activityService.createActivity(Mappers.prepareActivity(null, challenge.getId(), challenge.getChallengerId(),
                                attendance.getMemberId(), type));
                    });
        } else if (challenge.isSelf()) {
            activityService.createActivity(Mappers.prepareActivity(null, challenge.getId(), challenge.getChallengerId(),
                    challenge.getChallengerId(), type));
        }
    }
}

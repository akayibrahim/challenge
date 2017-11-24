package org.chl.intf;

import org.chl.models.Challenge;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface IChallengeService {
    Challenge addChallenge(Challenge chl);

    Iterable<Challenge> getChallenges();
}

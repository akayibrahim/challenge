package org.chl.repos;

import org.chl.models.Challenge;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;

/**
 * Created by ibrahim on 11/24/2017.
 */
public interface ChallengeRepository extends CrudRepository<Challenge, BigInteger> {
}

package com.example.beacon.interfac.domain.repository;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.beacon.interfac.infra.PulseEntity;

@Repository
public class PulsesRepositoryImpl implements PulsesQueries {

    @PersistenceContext
    private EntityManager manager;

    @Transactional(readOnly = true)
    public PulseEntity last(Long chainIndex){
        Long lastPulseId = (Long) manager.createQuery(
                "select max(p.id) from PulseEntity p where p.chainIndex = :chainIndex")
                .setParameter("chainIndex", chainIndex)
                .getSingleResult();
    	
        if (lastPulseId==null){
            return null;
        } else {
            return findByChainAndPulseId(chainIndex, lastPulseId);
        }
    }

    @Transactional(readOnly = true)
    public PulseEntity first(Long chainIndex){
        Long firstPulseIndex = (Long) manager.createQuery(
                "select min(p.pulseIndex) from PulseEntity p where p.chainIndex = :chainIndex")
                .setParameter("chainIndex", chainIndex)
                .getSingleResult();

        if (firstPulseIndex==null){
            return null;
        } else {
            return findByChainAndPulseId(chainIndex, firstPulseIndex);
        }
    }

    @Transactional(readOnly = true)
    public List<PulseEntity> obterTodos(Integer chainIndex){
        return Collections.unmodifiableList(manager
                .createQuery("from PulseEntity where chainIndex = :chainIndex order by id desc")
                .setParameter("chainIndex", chainIndex.toString())
                .setMaxResults(20)
                .getResultList());
    }

    @Transactional(readOnly = true)
    public PulseEntity findByChainAndPulseId(Long chainIndex, Long pulseId){
        try {
            PulseEntity recordEntity = (PulseEntity) manager
                    .createQuery("from PulseEntity p " +
                            "join fetch p.listValueEntities lve " +
                            "where p.chainIndex = :chainIndex and p.id = :pulseId")
                    .setParameter("chainIndex", chainIndex)
                    .setParameter("pulseId", pulseId)
                    .getSingleResult();

            return recordEntity;
        } catch (NoResultException e){
            return null;
        } catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
		return null;
    }

//    @Transactional(readOnly = true)
//    public Pulse findOldPulses(Long chainIndex, ZonedDateTime timeStamp){
//        try {
//            PulseEntity pulseEntity = (PulseEntity) manager
//                    .createQuery("from PulseEntity where chainIndex = :chainIndex and timeStamp >= :timeStamp order by timeStamp")
//                    .setParameter("chainIndex", chainIndex)
//                    .setParameter("timeStamp", timeStamp)
//                    .setMaxResults(1)
//                    .getSingleResult();
//
//            return Pulse.BuilderFromEntity(pulseEntity);
//        } catch (NoResultException e){
//            return null;
//        }
//    }

    @Transactional(readOnly = true)
    public PulseEntity findByTimestamp(ZonedDateTime timeStamp){
        try {
            PulseEntity pulseEntity = (PulseEntity) manager
                    .createQuery("from PulseEntity p " +
                            "join fetch p.listValueEntities lve " +
                            "where p.timeStamp = :timeStamp")
                    .setParameter("timeStamp", timeStamp)
                    .getSingleResult();
            return pulseEntity;
        } catch (NoResultException e){
            return null;
        }
    }

}

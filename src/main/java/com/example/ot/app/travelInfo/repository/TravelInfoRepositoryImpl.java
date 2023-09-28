package com.example.ot.app.travelInfo.repository;

import com.example.ot.app.travelInfo.entity.TravelInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.ot.app.travelInfo.entity.QTravelInfo.travelInfo;

@RequiredArgsConstructor
public class TravelInfoRepositoryImpl implements TravelInfoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TravelInfo> findByContentTypeId(String contentTypeId){
        String[] contentTypeIdsString = contentTypeId.split(",");

        Integer[] contentTypeIds = new Integer[contentTypeIdsString.length];
        for (int i = 0; i < contentTypeIdsString.length; i++) {
            contentTypeIds[i] = Integer.parseInt(contentTypeIdsString[i].trim());
        }

        return jpaQueryFactory
                .selectFrom(travelInfo)
                .where(travelInfo.contentTypeId.in(contentTypeIds))
                .fetch();
    }
}

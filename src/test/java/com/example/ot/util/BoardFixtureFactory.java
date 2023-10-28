package com.example.ot.util;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

public class BoardFixtureFactory {

    static public EasyRandom getBoard() {
        Predicate<Field> idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(TravelBoard.class));

        EasyRandomParameters param = new EasyRandomParameters()
                .excludeField(idPredicate);
        return new EasyRandom(param);
    }
}

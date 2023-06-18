package com.example.ot.app.host.dto;

import com.example.ot.app.region.entity.City;
import com.example.ot.app.region.entity.State;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RegionDTO {

    private List<State> stateList;
    private List<City> cityList;

}

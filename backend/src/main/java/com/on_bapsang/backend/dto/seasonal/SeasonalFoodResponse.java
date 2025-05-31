package com.on_bapsang.backend.dto.seasonal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "Grid_20171128000000000572_1")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeasonalFoodResponse {
    @XmlElement(name = "row")
    private List<SeasonalFoodItem> rows;
}

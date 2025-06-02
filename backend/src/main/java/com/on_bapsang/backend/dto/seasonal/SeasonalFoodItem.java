package com.on_bapsang.backend.dto.seasonal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;


@Data
@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class SeasonalFoodItem {
    private String IDNTFC_NO;
    private String PRDLST_NM;
    private String M_DISTCTNS;
    private String M_DISTCTNS_ITM;
    private String PRDLST_CL;
    private String MTC_NM;
    private String PRDCTN__ERA;
    private String MAIN_SPCIES_NM;
    private String EFFECT;
    private String PURCHASE_MTH;
    private String COOK_MTH;
    private String TRT_MTH;
    private String URL;
    private String IMG_URL;
    private String REGIST_DE;
}

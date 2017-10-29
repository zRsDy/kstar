package com.ibm.kstar.entity.product;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPriceDiscountVO {

	private String headId;
	
	private List<ProductPriceLine> lines = new ArrayList<>();

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public List<ProductPriceLine> getLines() {
		return lines;
	}

	public void setLines(List<ProductPriceLine> lines) {
		this.lines = lines;
	}
	
}

package com.customer.dto;

import java.util.List;

public record CustomerInformation(Integer Id, 
		String name, Integer balance, List<Holding> holdings) {

}

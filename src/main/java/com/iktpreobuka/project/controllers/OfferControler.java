package com.iktpreobuka.project.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.OfferEntity;
import com.iktpreobuka.project.entities.OfferType;

@RestController
@RequestMapping("/project/offers")
public class OfferControler {

	
	// 3.2
	private List<OfferEntity> getDB() {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 5);
		
		OfferEntity o1 = new OfferEntity(1, "2 tickets for Killers concert", "Enjoy!!!",
				new Date(), cal.getTime(), 100_000.00, 6_500.00, " ", 10, 0, OfferType.WAIT_FOR_APPROVING);
		
		OfferEntity o2 = new OfferEntity(2, "VIVAX 24LE76T2", "Don't miss this fantastic offer!",
				new Date(), cal.getTime(), 200_000.00, 16_500.00, " ", 5, 0, OfferType.WAIT_FOR_APPROVING);
		
		OfferEntity o3 = new OfferEntity(3, "Dinner for two in Aqua Doria", "Excellent offer",
				new Date(), cal.getTime(), 6_000.00, 3_500.00, " ", 4, 0, OfferType.WAIT_FOR_APPROVING);
		
		return Stream.of(o1, o2, o3).collect(Collectors.toList());
	}
	
	
	// 3.3
	@RequestMapping(method = RequestMethod.GET)
	public List<OfferEntity> getAll() {;
		return getDB();
	}
	
	
	// 3.4
	@RequestMapping(method = RequestMethod.POST)
	public OfferEntity add(@RequestBody OfferEntity o) {
		
		/*
		 * {
		 *	    "id": 4,
		 *	    "name": "NAME",
		 *	    "desc": "DESC",
		 *		"created": 2021-05-10,
		 *		"expires": 2021-05-30,
		 *	    "regPrice": 10000.00,
		 *		"actPrice": 1000.00,
		 *		"imgPath": "//",
		 *		"numAvailable": 15,
		 *		"numBought": 0
		 *	}
		 */
		
		OfferEntity o0 = new OfferEntity();
		o0.setId(o.getId());
		o0.setName(o.getName());
		o0.setDesc(o.getDesc());
		o0.setCreated(o.getCreated());
		o0.setExpires(o.getExpires());
		o0.setRegPrice(o.getRegPrice());
		o0.setActPrice(o.getActPrice());
		o0.setImgPath(o.getImgPath());
		o0.setNumAvailable(o.getNumAvailable());
		o0.setNumBought(o.getNumBought());
		o0.setType(OfferType.WAIT_FOR_APPROVING);
		
		getDB().add(o0);
		
		return o0;
	}
	
	
	// 3.5
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public OfferEntity changeOffer(@PathVariable Integer id, @RequestBody OfferEntity o) {
		
		for (OfferEntity offer : getDB())
			if (offer.getId().equals(o.getId())) {
				
				if (o.getName() != null)
					offer.setName(o.getName());
				
				if (o.getDesc() != null)
					offer.setDesc(o.getDesc());
				
				if (o.getCreated() != null)
					offer.setCreated(o.getCreated());
				
				if (o.getExpires() != null)
					offer.setExpires(o.getExpires());
				
				if (o.getRegPrice() != null)
					offer.setRegPrice(o.getRegPrice());
				
				if (o.getActPrice() != null)
					offer.setActPrice(o.getActPrice());
				
				if (o.getImgPath() != null)
					offer.setImgPath(o.getImgPath());
				
				if (o.getNumAvailable() != null)
					offer.setNumAvailable(o.getNumAvailable());
				
				if (o.getNumBought() != null)
					offer.setNumBought(o.getNumBought());
				
				return offer;
			}
		
		return null;
	}
	
	
	// 3.6
	@RequestMapping(method = RequestMethod.DELETE, value ="/{id}")
	public OfferEntity delete(@PathVariable Integer id) {
		
		OfferEntity o = getDB().stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
		
		if (o == null) {
			return null;
		} else {
			getDB().remove(o);
			return o;
		}
	}
	
	
	// 3.7
	@RequestMapping(method = RequestMethod.GET, value ="/by-id/{id}")
	public OfferEntity getById(@PathVariable Integer id) {
		
		for (OfferEntity o : getDB())
			if (o.getId().equals(id))
				return o;
		
		return null;
	}
	
	
	// 3.8
	@RequestMapping(method = RequestMethod.PUT, value = "/changeOffer/{id}/status/{status}")
	public OfferEntity changeStatus(@PathVariable Integer id, @PathVariable String status) {
		
		for (OfferEntity o : getDB())
			if (o.getId().equals(id)) {
				
				o.setType(OfferType.fromString(status));
				
				return o;
			}
		
		return null;
	}
	
	
	// 3.9
	@RequestMapping(method = RequestMethod.GET, value ="/findByPrice/{lowerPrice}/and/{upperPrice}")
	public List<OfferEntity> getById(@PathVariable Integer lowerPrice, @PathVariable Integer upperPrice) {
		
		return getDB().stream()
				.filter(o -> lowerPrice <= o.getRegPrice() && o.getRegPrice() <= upperPrice)
				.collect(Collectors.toList());
	}
}
